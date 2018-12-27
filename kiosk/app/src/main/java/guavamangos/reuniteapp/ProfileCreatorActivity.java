package guavamangos.reuniteapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.Random;

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
public class ProfileCreatorActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        String[] welcomeMessages = getResources().getStringArray(R.array.profile_creator_welcome_messages);
        String welcomeMessage = welcomeMessages[new Random().nextInt(welcomeMessages.length)];

        TextView profileCreatorLogo = findViewById(R.id.profileCreatorLogo);
        TextView profileCreatorWelcome = findViewById(R.id.profileCreatorWelcome);


        profileCreatorLogo.setText(Html.fromHtml(getString(R.string.guavalogo)));
        profileCreatorWelcome.setText(welcomeMessage);
    }

    private void writeRFID() {
        // TODO: Write data to RFID tag...

        // Data that will be written
        byte[] newData = {0x0F,0x0E,0x0D,0x0C,0x0B,0x0A,0x09,0x08,0x07,0x06,0x05,0x04,0x03,0x02,0x01,0x00};
    }
}
