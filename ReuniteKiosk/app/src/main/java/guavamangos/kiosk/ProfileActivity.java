package guavamangos.kiosk;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {

    private Bundle extraProfileOptions;
    byte[] nameData = new byte[16];
    private ImageView backgroundWallpaper;

    private TextView timeLabel;
    private TextView dateLabel;

    private static String[] months = {"January", "February",
            "March", "April", "May", "June", "July",
            "August", "September", "October", "November",
            "December"};

    private int HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    private String MONTH = months[Calendar.getInstance().get(Calendar.MONTH)];
    private int DAY_OF_MONTH = Calendar.getInstance().get(Calendar.DAY_OF_WEEK_IN_MONTH);
    private String DAY_OF_WEEK = new DateFormatSymbols().getShortWeekdays()[Calendar.getInstance().get(Calendar.DAY_OF_WEEK)];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backgroundWallpaper = findViewById(R.id.background_wallpaper_profile);
        timeLabel = findViewById(R.id.profile_time_tv);
        dateLabel = findViewById(R.id.profile_date_tv);

        extraProfileOptions = getIntent().getExtras();

        Picasso.get().load("https://source.unsplash.com/1080x1920/?dark-nature").into(backgroundWallpaper);

        final Handler constantTimeAndRFIDChanges = new Handler(getMainLooper());
        constantTimeAndRFIDChanges.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeLabel.setText(new SimpleDateFormat("h:mm", Locale.US).format(new Date()));
                if (HOUR > 11) {
                    timeLabel.append(" pm");
                } else {
                    timeLabel.append(" am");
                }
                dateLabel.setText(String.format(Locale.getDefault(), "%1$s, %2$s %3$d, %4$d", DAY_OF_WEEK, MONTH, DAY_OF_MONTH, YEAR));
                constantTimeAndRFIDChanges.postDelayed(this, 1000);
            }
        }, 10);

        if (backgroundWallpaper.getDrawable() == getResources().getDrawable(R.drawable.placeholderimagefordashboard)) {

        }

        if (extraProfileOptions != null) {

        }
    }
}
