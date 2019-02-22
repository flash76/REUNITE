package guavamangos.reunitemobile;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private Button createPostButton;

    private EditText postTitle;
    private EditText postBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createPostButton =findViewById(R.id.mainActivityCreatePostButton);

        postTitle = findViewById(R.id.titleET);
        postBody = findViewById(R.id.messageBodyET);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("message");

        createPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Creating post....", Toast.LENGTH_LONG).show();
                myRef.setValue("Title: " + postTitle.getText().toString() + " Message: " + postBody.getText().toString())
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Code here to handle failure
                        Toast.makeText(MainActivity.this, "post creation failure", Toast.LENGTH_LONG).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "post creation successful", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
