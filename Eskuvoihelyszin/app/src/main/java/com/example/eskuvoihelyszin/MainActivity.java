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

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private static final String LOG_TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
    }

    public void login(View view) {
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        try{
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.i(LOG_TAG, "Sikeres belepes");
                        startVenues();
                    }else{
                        Log.i(LOG_TAG, "Sikertelen belepes");
                        Toast.makeText(MainActivity.this, "Hibás email cím vagy jelszó", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Hibás email cím vagy jelszó", Toast.LENGTH_SHORT).show();
        }
    }

    public void reg(View view) {
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
    }

    public void venues(View view) {
        startVenues();
    }

    private void startVenues(){
        Intent intent = new Intent(this, VenuesActivity.class);
        startActivity(intent);
    }
}