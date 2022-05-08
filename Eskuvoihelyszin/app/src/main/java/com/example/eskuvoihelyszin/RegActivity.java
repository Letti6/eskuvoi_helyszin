package com.example.eskuvoihelyszin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    private static final String LOG_TAG = RegActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void reg(View view) {
        EditText userNameEditText = findViewById(R.id.username);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        EditText passwordAgainEditText = findViewById(R.id.password_again);
        EditText telEditText = findViewById(R.id.telnumber);

        String username = userNameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();
        String tel = telEditText.getText().toString();

        if(!password.equals(passwordAgain)){
            Toast.makeText(this, "Nem egyezik a jelszó a jelszó megerősítésével!", Toast.LENGTH_SHORT).show();
        }else{
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        user = firebaseAuth.getCurrentUser();
                        user.updateProfile(profileUpdates);
                        startVenues();
                    }else{
                        Log.i(LOG_TAG, task.getException().toString());
                    }
                }
            });
        }
    }

    public void cancel(View view) {
        finish();
    }

    private void startVenues(){
        Intent intent = new Intent(this, VenuesActivity.class);
        startActivity(intent);
    }
}