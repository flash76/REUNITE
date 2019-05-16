package guavamangos.kiosk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private Bundle extraProfileOptions;
    byte[] nameData = new byte[16];
    private ImageView backgroundWallpaper;

    private TextView timeLabel;
    private TextView dateLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backgroundWallpaper = findViewById(R.id.background_wallpaper_profile);

        extraProfileOptions = getIntent().getExtras();

        Picasso.get().load("https://source.unsplash.com/1080x1920/?dark-nature").into(backgroundWallpaper);

        if (backgroundWallpaper.getDrawable() == getResources().getDrawable(R.drawable.Placeholderimagefordashboard)) {

        }

        if (extraProfileOptions != null) {

        }
    }
}
