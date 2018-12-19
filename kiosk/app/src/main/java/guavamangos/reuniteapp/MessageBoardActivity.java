package guavamangos.reuniteapp;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.Calendar;
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
public class MessageBoardActivity extends Activity {

    private Calendar calendar = Calendar.getInstance(Locale.getDefault());
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_board);

        // Find our widgets
        TextView messageBoardTitle = findViewById(R.id.messageBoardTitle);
        TextView messageBoardScanTagInfo = findViewById(R.id.messageBoardScanTagInfo);
        TextView messageBoardTime = findViewById(R.id.messageBoardTime);

        // Set text
        messageBoardTitle.setText(Html.fromHtml(getString(R.string.message_board_title)));
        messageBoardScanTagInfo.setText(Html.fromHtml(getString(R.string.message_board_scan_tag_info)));

        messageBoardTime.setText(Html.fromHtml(getString(R.string.message_board_time, hour, minute)));
    }
}
