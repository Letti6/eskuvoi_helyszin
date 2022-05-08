package com.example.eskuvoihelyszin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FoglalasByUserActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private CollectionReference fItems;

    private RecyclerView rec;
    private ArrayList<Foglalas> foglalasok;
    private FoglalasAdapter fAdapter;

    private static final String LOG_TAG = FoglalasByUserActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foglalas_by_user);

        user = FirebaseAuth.getInstance().getCurrentUser();
        foglalasok = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        fItems = firestore.collection("Foglalasok");

        rec = findViewById(R.id.foglalasRecyclerView);
        rec.setLayoutManager(new GridLayoutManager(this, 1));

        fAdapter = new FoglalasAdapter(foglalasok, this);
        rec.setAdapter(fAdapter);

        queryData();
    }

    private void queryData() {

        foglalasok.clear();

        fItems.whereEqualTo("userEmail", user.getEmail()).orderBy("date").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                Foglalas foglalas = doc.toObject(Foglalas.class);
                foglalas._setId(doc.getId());
                foglalasok.add(foglalas);
            }

            if(foglalasok.size() == 0){
                findViewById(R.id.foglalas).setVisibility(View.VISIBLE);
            }else{
                findViewById(R.id.foglalas).setVisibility(View.INVISIBLE);
            }

            fAdapter.notifyDataSetChanged();
        });
    }

    public void deleteFoglalas(Foglalas foglalas){
        DocumentReference ref = fItems.document(foglalas._getId());

        ref.delete().addOnSuccessListener(success -> {
            Log.i(LOG_TAG, "Sikeres törlés");
        })
        .addOnFailureListener(failure -> {
            Toast.makeText(this, "Sikertelen törlés", Toast.LENGTH_SHORT).show();
        });

        queryData();
    }
}