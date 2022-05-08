package com.example.eskuvoihelyszin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class VenuesActivity extends AppCompatActivity {
    private RecyclerView rec;
    private ArrayList<Venue> venues;
    private VenueAdapter vAdapter;
    private FirebaseUser user;

    private FirebaseFirestore firestore;
    private CollectionReference vItems;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venues);

        user = FirebaseAuth.getInstance().getCurrentUser();

        rec = findViewById(R.id.recyclerView);
        rec.setLayoutManager(new GridLayoutManager(this, gridNumber));
        venues = new ArrayList<>();

        vAdapter = new VenueAdapter(venues, this);
        rec.setAdapter(vAdapter);

        firestore = FirebaseFirestore.getInstance();
        vItems = firestore.collection("Venues");

        queryData();

    }

    private void queryData() {

        venues.clear();

        vItems.orderBy("name").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                Venue venue = doc.toObject(Venue.class);
                venues.add(venue);
            }

            if(venues.size() == 0){
                intializeData();
                queryData();
            }

            vAdapter.notifyDataSetChanged();
        });
    }

    private void intializeData() {
        String[] itemsName = getResources().getStringArray(R.array.venue_item_names);
        String[] itemsCity = getResources().getStringArray(R.array.venue_item_city);
        String[] itemsInfo = getResources().getStringArray(R.array.venue_item_infos);
        String[] itemsPrice = getResources().getStringArray(R.array.venue_item_prices);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.venue_item_kepek);

        for(int i=0;i<itemsName.length; i++){
            vItems.add(new Venue(itemsName[i], itemsCity[i], itemsInfo[i], itemsPrice[i], itemsImageResource.getResourceId(i, 0)));
        }

        itemsImageResource.recycle();

    }

    public void visit(Venue venue){
        Intent intent = new Intent(this, VenueActivity.class);
        intent.putExtra("EXTRA_VENUE", (Parcelable) venue);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.venue_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) { return false; }

            @Override
            public boolean onQueryTextChange(String s) {
                vAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
                if (user != null) {
                    Intent intent3 = new Intent(this, ProfileActivity.class);
                    startActivity(intent3);
                } else {
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
}