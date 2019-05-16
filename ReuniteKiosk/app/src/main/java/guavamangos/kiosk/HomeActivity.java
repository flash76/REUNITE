package guavamangos.kiosk;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.galarzaa.androidthings.Rc522;
import com.google.android.things.device.TimeManager;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.SpiDevice;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
public class HomeActivity extends Activity {

    // Widgets in app
    private TextView guavamangosLogo;
    private TextView timeView;
    private TextView dateView;
    private ImageView kioskWallpaper;

    // RFID Sensor Object
    public static Rc522 mRc522;

    // Stuff with the dates and times
    private static String[] months = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};
    private int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private String MONTH = months[Calendar.getInstance().get(Calendar.MONTH)];
    private int DAY_OF_MONTH = Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
    private String DAY_OF_WEEK = new DateFormatSymbols().getShortWeekdays()[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)];
    private int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

    // RFID variables
    private byte[] buffer = new byte[16];
    private int blockNum = 1;
    boolean result = false;
    private Intent profileActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileActivityIntent = new Intent(HomeActivity.this, ProfileActivity.class);

        // Instantiate RFID stuff
        PeripheralManager pioService = PeripheralManager.getInstance();
        try {
            SpiDevice spiDevice = pioService.openSpiDevice("SPI0.0");
            Gpio resetPin = pioService.openGpio("BCM25");
            mRc522 = new Rc522(this, spiDevice, resetPin);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set up device for user use (timezone, screen rotation)
        setTimeZone("America/New_York");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        guavamangosLogo = findViewById(R.id.guavamangosLogo);
        dateView = findViewById(R.id.home_date_real_text);
        timeView = findViewById(R.id.home_time);
        kioskWallpaper = findViewById(R.id.home_wallpaper);

        // branding
        guavamangosLogo.setText(Html.fromHtml(getString(R.string.guavamangos)));


        // Set time
        final Handler constantTimeAndRFIDChanges = new Handler(getMainLooper());
        constantTimeAndRFIDChanges.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeView.setText(new SimpleDateFormat("h:mm", Locale.US).format(new Date()));
                if (HOUR > 11) {
                    timeView.append(" pm");
                } else {
                    timeView.append(" am");
                }
                dateView.setText(String.format(Locale.getDefault(), "%1$s, %2$s %3$d, %4$d", DAY_OF_WEEK, MONTH, DAY_OF_MONTH, YEAR));
                constantTimeAndRFIDChanges.postDelayed(this, 1000);
            }
        }, 10);


        RFIDTasks backgroundRFIDTasks = new RFIDTasks();
        backgroundRFIDTasks.execute();

        // Get a nice image from the Unsplash Source API
        // Thanks to the very helpful people at StackOverflow
        // (https://stackoverflow.com/questions/55583075/how-do-i-get-an-image-from-a-remote-source-url-and-show-it-in-imageview - the question I asked;) )
        Picasso.get().load("https://source.unsplash.com/1080x1920/?dark-nature").into(kioskWallpaper);


    }

    private void setTimeZone(String timeZoneName) {
        TimeManager timeManager = TimeManager.getInstance();
        timeManager.setTimeZone(timeZoneName);
    }


    private void passToRFIDCheck() {
        try {
            switch (blockNum) {
                case 1: {
                    blockNum += 1;
                    profileActivityIntent.putExtra("Block" + blockNum + "Data", buffer);
                }

                case 2: {
                    blockNum += 1;
                    profileActivityIntent.putExtra("Block" + blockNum + "Data", buffer);
                }

                case 3: {
                    blockNum += 1;
                    profileActivityIntent.putExtra("Block" + blockNum + "Data", buffer);
                }

                case 4: {
                    blockNum += 1;
                    profileActivityIntent.putExtra("Block" + blockNum + "Data", buffer);
                }

                case 5: {
                    // Done with passing 64 bytes of data into Bundle
                    Toast.makeText(HomeActivity.this, "Data Pass Complete.", Toast.LENGTH_LONG).show();

                    startActivity(new Intent(profileActivityIntent));
                }
            }
        } catch (Exception e) {
            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void RFIDCheck() {
        while (true) {
            boolean success = mRc522.request();
            if (!success) {
                continue;
            }
            success = mRc522.antiCollisionDetect();
            if (!success) {
                continue;
            }
            byte[] uid = mRc522.getUid();
            mRc522.selectTag(uid);
            break;
        }
        // Factory Key A:
        byte[] key = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
        // Data that will be written
        byte[] newData = {0x0F, 0x0E, 0x0D, 0x0C, 0x0B, 0x0A, 0x09, 0x08, 0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};
        // Get the address of the desired block
        byte block = Rc522.getBlockAddress(3, 2);
        //We need to authenticate the card, each sector can have a different key
        boolean result = mRc522.authenticateCard(Rc522.AUTH_A, block, key);
        if (!result) {
            //Authentication failed
            return;
        }
        result = mRc522.writeBlock(block, newData);
        if (!result) {
            //Could not write, key might have permission to read but not write
            return;
        }
        //Buffer to hold read data
        final byte[] buffer = new byte[16];
        //Since we're still using the same block, we don't need to authenticate again
        result = mRc522.readBlock(block, buffer);
        if (!result) {
            //Could not read card
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    passToRFIDCheck();
                }
            });
        }
        //Stop crypto to allow subsequent readings
        mRc522.stopCrypto();
    }

    private class RFIDTasks extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            RFIDCheck();
            return null;
        }
    }
}