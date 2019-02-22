package guavamangos.reuniteapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <pre>{@code9
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class ProfileCreatorActivity extends Activity implements ProfileCreatorPersonInfo.OnFragmentInteractionListener, ProfileCreatorOtherInfo.OnFragmentInteractionListener {

    private int profileCreatorScreensIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creator);

        ImageButton profileCreatorForward = findViewById(R.id.profileCreatorForwardButton);
        ImageButton profileCreatorBackward = findViewById(R.id.profileCreatorBackButton);

        final Fragment profileCreatorPersonInfo = new ProfileCreatorPersonInfo();
        final Fragment profileCreatorOtherInfo = new ProfileCreatorPersonInfo();

        final FragmentTransaction ft = getFragmentManager().beginTransaction();

        final android.app.Fragment[] profileCreatorScreens = {profileCreatorPersonInfo, profileCreatorOtherInfo};



        String[] welcomeMessages = getResources().getStringArray(R.array.profile_creator_welcome_messages);
        String welcomeMessage = welcomeMessages[new Random().nextInt(welcomeMessages.length)];

        TextView profileCreatorLogo = findViewById(R.id.profileCreatorLogo);
        TextView profileCreatorWelcome = findViewById(R.id.profileCreatorWelcome);


        profileCreatorLogo.setText(Html.fromHtml(getString(R.string.guavalogo)));
        profileCreatorWelcome.setText(welcomeMessage);

        ft.replace(R.id.profileCreatorFragmentWindow, profileCreatorScreens[profileCreatorScreensIndex]);

        profileCreatorForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Change screen when button is clicked using transaction.replace()
                ft.replace(R.id.profileCreatorFragmentWindow, profileCreatorScreens[profileCreatorScreensIndex + 1]);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        profileCreatorBackward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Change stuff
                ft.replace(R.id.profileCreatorFragmentWindow, profileCreatorScreens[profileCreatorScreensIndex - 1]);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    private void writeRFID() {
        // TODO: Write data to RFID tag...

        // Data that will be written
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
