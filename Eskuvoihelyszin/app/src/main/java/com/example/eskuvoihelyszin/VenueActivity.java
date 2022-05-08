package com.example.eskuvoihelyszin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VenueActivity extends AppCompatActivity {
    private FirebaseUser user;

    private TextView name;
    private TextView city;
    private TextView info;
    private TextView price;
    private ImageView imageView;

    private Venue venue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue);

        user = FirebaseAuth.getInstance().getCurrentUser();

        venue = (Venue) getIntent().getParcelableExtra("EXTRA_VENUE");

        name = findViewById(R.id.vname);
        name.setText(venue.getName());

        city = findViewById(R.id.vcity);
        city.setText(venue.getCity());

        info = findViewById(R.id.vinfo);
        info.setText(venue.getInfo());

        price = findViewById(R.id.vprice);
        price.setText(venue.getPrice());

        imageView = findViewById(R.id.vimage);
        imageView.setImageResource(venue.getImageResource());
    }

    public void foglalas(View view) {
        Intent intent = new Intent(this, FoglalasActivity.class);
        intent.putExtra("EXTRA_VENUE", venue);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.venue_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        menuItem.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.log_out_button:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
                return true;
            case R.id.home:
                Intent intent2 = new Intent(this,MainActivity.class);
                startActivity(intent2);
                return true;
            case R.id.profile_menu:
                if(user != null){
                    Intent intent3 = new Intent(this, ProfileActivity.class);
                    startActivity(intent3);
                }else{
                    Toast.makeText(this, "Nincs bejelentkezve!", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.foglalas_menu:
                if (user != null) {
                    Intent intent4 = new Intent(this, FoglalasByUserActivity.class);
                    startActivity(intent4);
                } else {
                    Toast.makeText(this, "Nincs bejelentkezve!", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    public void back(View view) {finish();}
}