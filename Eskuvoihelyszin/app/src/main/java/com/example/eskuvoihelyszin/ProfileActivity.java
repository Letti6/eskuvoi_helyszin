package com.example.eskuvoihelyszin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private EditText editTextProfileName;
    private EditText editTextProfilePassword;
    private EditText editTextProfilePasswordAgain;
    private ImageView imageView;

    private FirebaseFirestore firestore;
    private CollectionReference pictures;

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    int tag = 1;
    boolean changePicture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        firestore = FirebaseFirestore.getInstance();
        pictures = firestore.collection("Pictures");

        editTextProfileName = findViewById(R.id.profile_name);
        editTextProfilePassword = findViewById(R.id.profile_password);
        editTextProfilePasswordAgain = findViewById(R.id.profile_password_again);
        imageView = findViewById(R.id.profile_image_view);

        try{
            editTextProfileName.setText(user.getDisplayName());
            pictures.whereEqualTo("email", user.getEmail()).get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                    Map<String, Object> p = doc.getData();
                    byte[] bytes=Base64.decode(p.get("pic").toString(),Base64.DEFAULT);
                    Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    imageView.setImageBitmap(bitmap);
                }
            });

        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

    public void back(View view) {finish();}

    public void updateProfile(View view) {
        if(editTextProfileName.getText().toString() != "") {
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    .setDisplayName(editTextProfileName.getText().toString())
                    .build();

            user.updateProfile(profileUpdate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProfileActivity.this, "Név módosítva", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileActivity.this, "Név módosítása nem sikerült!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    public void updatePassword(View view) {
        if (editTextProfilePassword.getText().toString().equals(editTextProfilePasswordAgain.getText().toString())) {
            if (editTextProfilePassword.getText().toString() != "" && editTextProfilePasswordAgain.getText().toString() != "") {
                user.updatePassword(editTextProfilePassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ProfileActivity.this, "Jelszó módosítva", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ProfileActivity.this, "Jelszó módosítása nem sikerült!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    public void updatePicture(View view) {
        if(changePicture == true){
            Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            String p = Base64.encodeToString(data, Base64.DEFAULT);

            Map<String, String> pic = new HashMap<>();
            pic.put("email", user.getEmail());
            pic.put("pic", p);

            pictures.add(pic).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Kép módosítva", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileActivity.this, "Kép módosítása nem sikerült!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePicture();
                }else{
                    Toast.makeText(this, "Engedély elutasítva", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void openCamera(View view) {
        checkUserPermission();
    }

    void checkUserPermission(){
        if(Build.VERSION.SDK_INT >= 23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
                return;
            }
        }

        takePicture();
    }

    private void takePicture() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, tag );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == tag && resultCode == RESULT_OK){
            Bundle b = data.getExtras();
            Bitmap img = (Bitmap) b.get("data");
            imageView.setImageBitmap(img);
            changePicture = true;
        }
    }
}