package com.example.eskuvoihelyszin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class FoglalasActivity extends AppCompatActivity {
    private FirebaseUser user;

    private TextView venueNameCity;
    private EditText name;
    private EditText email;
    private EditText date;
    private EditText telNumber;
    private Venue venue;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private FirebaseFirestore firestore;
    private CollectionReference vFoglalasok;

    private NotificationHandler notificationHandler;

    private static final String LOG_TAG = FoglalasActivity.class.getName();
    private static final String PREF_KEY = FoglalasActivity.class.getPackage().toString();

    private SharedPreferences preferences;
    String venueID;
    int foglalas_e = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foglalas);

        firestore = FirebaseFirestore.getInstance();
        vFoglalasok = firestore.collection("Foglalasok");

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        user = FirebaseAuth.getInstance().getCurrentUser();

        venue = (Venue) getIntent().getParcelableExtra("EXTRA_VENUE");
        venueID = preferences.getString("venueid", "");

        name = findViewById(R.id.name);
        if(user.getDisplayName() != null){
            name.setText(user.getDisplayName());
        }

        email = findViewById(R.id.email);
        if(user.getEmail() != null){
            email.setText(user.getEmail());
        }

        venueNameCity = findViewById(R.id.venue_name_city);
        venueNameCity.setText(venue.getName()+", "+venue.getCity());
        date = findViewById(R.id.date);
        telNumber = findViewById(R.id.telnumber);

        if(venueID.equals(venueNameCity.getText().toString())) {

            if(name.getText().toString().equals("")){
                name.setText(preferences.getString("name", ""));
            }

            String venueDate = preferences.getString("date", "");
            date.setText(venueDate);

            String venueTel = preferences.getString("tel", "");
            telNumber.setText(venueTel);
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        date.setText(i+"-"+(i1+1)+"-"+i2);
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(FoglalasActivity.this,dateSetListener, year, month, day);
                dialog.show();
            }
        });

        notificationHandler = new NotificationHandler(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(foglalas_e == 0 && (!date.getText().toString().equals("") || !telNumber.getText().toString().equals(""))){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("venueid", venueNameCity.getText().toString());
            editor.putString("name", name.getText().toString());
            editor.putString("date", date.getText().toString());
            editor.putString("tel", telNumber.getText().toString());
            editor.apply();
        }

        foglalas_e = 0;
    }

    public void foglalas(View view) {

        if(!name.getText().toString().equals("") && !date.getText().toString().equals("") && !email.getText().toString().equals("")) {
            Foglalas foglalas = new Foglalas(venueNameCity.getText().toString(), name.getText().toString(), email.getText().toString(), date.getText().toString(), telNumber.getText().toString());
            vFoglalasok.add(foglalas);

            notificationHandler.send("Foglalást adtál le!");

            Toast.makeText(this, "Sikeres foglalás!", Toast.LENGTH_SHORT).show();
            if(venueID.equals(venueNameCity.getText().toString())){
                preferences.edit().remove("venueid").commit();
            }

            foglalas_e = 1;

            finish();
        }

    }

    public void cancel(View view) {finish();}
}