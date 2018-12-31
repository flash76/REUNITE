package guavamangos.reuniteapp;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class ProfileCreatorActivity extends Activity implements ProfileCreatorPersonInfo.OnFragmentInteractionListener {

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
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
