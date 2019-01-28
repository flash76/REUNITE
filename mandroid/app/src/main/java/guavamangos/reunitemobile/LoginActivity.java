package guavamangos.reunitemobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button loginButton;
    private FirebaseAuth mAuth;
    private EditText emailLogin;
    private EditText passwordLogin;
    private TextView notifyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.loginButton);

        notifyTextView = findViewById(R.id.textView2);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailLogin.getText().toString();
                String password = passwordLogin.getText().toString();

                Boolean emailisgood = false;
                Boolean passisgood = false;

                if (TextUtils.isEmpty(email)) {
                    notifyTextView.setText("plz enter da email");
                    emailisgood = false;
                } else {
                    emailisgood = true;
                }
                if (TextUtils.isEmpty(password)) {
                    notifyTextView.setText("plz enter da password");
                    passisgood = false;
                } else {
                    passisgood = true;
                }

                if (passisgood && emailisgood) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}
