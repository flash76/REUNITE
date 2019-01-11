package guavamangos.reuniteapp.components;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.widget.Button;

import java.lang.reflect.Method;

import guavamangos.reuniteapp.R;

public class GButton extends Activity {
    public void applyGButtonStyle(@NonNull Button gButton) {
        GradientDrawable gradientDrawable = new GradientDrawable();

        gradientDrawable.setColor(getColor(R.color.colorPrimary));
        gradientDrawable.setCornerRadius(3);

        gButton.setBackgroundDrawable(gradientDrawable);
    }

//    public Button createGButton(String text, Method onClickListener,  )
}
