package guavamangos.reuniteapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import com.galarzaa.androidthings.Rc522;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 *
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MessageBoardActivity extends Activity {

    private Calendar calendar = Calendar.getInstance(Locale.getDefault());

    private int hour = calendar.get(Calendar.HOUR_OF_DAY);
    private int minute;
    private int year = calendar.get(Calendar.YEAR);
    private int dayNumber = calendar.get(Calendar.DATE);

    private String month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    private String day = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());

    private String weatherText = "";
    private String weatherTextExtended = "";

    private double temperature = 0.0;

    private final String API_KEY = "AccuWeather API Key Here";
    private String IP_ADDRESS = getIP();
    private final String GET_LOCATION_KEY_FROM_IP = "http://dataservice.accuweather.com/locations/v1/cities/ipaddress?apikey=%1$s&q=%2$d";

    private Rc522 mRc522;

    private boolean cardReadData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PeripheralManager pioService = PeripheralManager.getInstance();

        setContentView(R.layout.activity_message_board);

        try {
            SpiDevice spiDevice = pioService.openSpiDevice("SPI0.0");
            Gpio resetPin = pioService.openGpio("BCM25");
            mRc522 = new Rc522(this, spiDevice, resetPin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Some weird bug screws up the value if it is set while being created as an instance variable
        minute = calendar.get(Calendar.MINUTE);

        // Find our widgets
        TextView messageBoardLogo = findViewById(R.id.messageBoardLogo);
        TextView messageBoardScanTagInfo = findViewById(R.id.messageBoardScanTagInfo);
        TextView messageBoardTime = (TextView) findViewById(R.id.messageBoardTime);
        TextView messageBoardDate = findViewById(R.id.messageBoardDate);

        // Set text
        messageBoardLogo.setText(Html.fromHtml(getString(R.string.guavalogo)));
        //messageBoardScanTagInfo.setText(Html.fromHtml(getString(R.string.message_board_scan_tag_info)));
        messageBoardScanTagInfo.setText(IP_ADDRESS);
        messageBoardDate.setText(Html.fromHtml(getString(R.string.message_board_date, day, month, dayNumber, year)));

        messageBoardTime.setText("LALALALA");

        if (hour >= 13) {
            messageBoardTime.setText(Html.fromHtml(getString(R.string.message_board_time, hour - 12, minute)));
            messageBoardTime.append(" pm");
        } else {
            messageBoardTime.setText(Html.fromHtml(getString(R.string.message_board_time, hour, minute)));
            messageBoardTime.append(" am");
        }


        RunRFIDInBackground runRFIDInBackgroundTask = new RunRFIDInBackground();
        runRFIDInBackgroundTask.execute();

        // Send a GET request to retrieve weather data from AccuWeather
        RequestQueue queue = Volley.newRequestQueue(this);
    }

    // Code retrieved from https://itekblog.com/android-get-mobile-ip-address/
    private String getIP() {
        try {
            WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return String.format(Locale.getDefault(), "%d.%d.%d.%d",
                    (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        } catch (Exception ex) {
            Log.e("What went wrong when getting IP Address", ex.getMessage());
            return null;
        }
    }

    private void readRFid(){
        while(true){
            boolean success = mRc522.request();
            if(!success){
                continue;
            }
            success = mRc522.antiCollisionDetect();
            if(!success){
                continue;
            }
            byte[] uid = mRc522.getUid();
            mRc522.selectTag(uid);
            break;
        }
        // Factory Key A:
        byte[] key = {(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        // Get the address of the desired block
        byte block = Rc522.getBlockAddress(3, 2);
        //We need to authenticate the card, each sector can have a different key
        boolean result = mRc522.authenticateCard(Rc522.AUTH_A, block, key);
        if (!result) {
            //Authentication failed
            return;
        }
        //Buffer to hold read data
        final byte[] buffer = new byte[16];
        //Since we're still using the same block, we don't need to authenticate again
        result = mRc522.readBlock(block, buffer);
        if(!result){
            //Could not read card
            showDialog(MessageBoardActivity.this, "Uh oh...", "Your ID card could not be read. \n Try re-scanning, and see if that works.",
                    "OK", null, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // dismiss dialog and try again
                            readRFid();
                        }
                    }, null);
            return;

        } else {
            for (int i = 0; i <= 15; i++) {
                if (buffer[i] == 0) {
                    cardReadData = true;
                } else if (buffer[i] != 0) {
                    cardReadData = false;
                }
            }
            if (!cardReadData) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MessageBoardActivity.this, "This card isn't in the format we were expecting. Let's create a new profile.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(MessageBoardActivity.this, ProfileCreatorActivity.class));
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        final Toast toast =
                                Toast.makeText(MessageBoardActivity.this, "Welcome, " + Arrays.toString(buffer), Toast.LENGTH_LONG);
                        toast.show();
                        // TODO: Create an activity that will take the user to their profile
                    }
                });
            }
        }
        //Stop crypto to allow subsequent readings
        mRc522.stopCrypto();


    }

    public static AlertDialog showDialog(@NonNull Context context, @NonNull String title, @NonNull String msg,
                                         @NonNull String positiveBtnText, @Nullable String negativeBtnText,
                                         @NonNull DialogInterface.OnClickListener positiveBtnClickListener,
                                         @Nullable DialogInterface.OnClickListener negativeBtnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(positiveBtnText, positiveBtnClickListener);
        if (negativeBtnText != null)
            builder.setNegativeButton(negativeBtnText, negativeBtnClickListener);
        AlertDialog alert = builder.create();
        alert.show();
        return alert;
    }

    private class RunRFIDInBackground extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            readRFid();
            return null;
        }
    }


}
