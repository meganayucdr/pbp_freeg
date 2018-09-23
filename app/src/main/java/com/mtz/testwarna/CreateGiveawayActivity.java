package com.mtz.testwarna;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.mtz.testwarna.api.GiveawayApi;
import com.mtz.testwarna.dao.GiveawayDAO;
import com.mtz.testwarna.network.RetrofitInstance;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CreateGiveawayActivity extends AppCompatActivity {

    private TextView txtUser;
    private CountryCodePicker spinnerRegion;
    private EditText editParticipants;
    private EditText editContent;
    private Button btnPost;
    private ImageView imageGiveaway;
    FirebaseAuth firebaseAuth;
    public static int RESULT_CODE = 120;
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_giveaway);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        setAttribute();
        assignUser();
//        imageGiveaway.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, RESULT_CODE);
//            }
//        });
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)    {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageGiveaway.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(CreateGiveawayActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CreateGiveawayActivity.this, "You haven't picked image", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void setAttribute() {
        txtUser = (TextView) findViewById(R.id.txtUser);
        spinnerRegion = (CountryCodePicker) findViewById(R.id.ccpcreate);
        editParticipants = (EditText) findViewById(R.id.txtParticipants);
        editContent = (EditText) findViewById(R.id.editContent);
        btnPost = (Button) findViewById(R.id.btnPost);
        imageGiveaway = (ImageView) findViewById(R.id.giveawayImage);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onClickPost();
            }
        });
    }

    private void assignUser() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("User");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue() != null) {
                    Username username = dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).getValue(Username.class);
                    Log.d("user", username.getUsername());
                    txtUser.setText(username.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("ERROR", databaseError.getMessage());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickPost() {
        if (editParticipants.getText().toString().isEmpty() ||
                editContent.getText().toString().isEmpty()) {
            Toast.makeText(CreateGiveawayActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            //Post data into API
            //Buid retrofit
            Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
            GiveawayApi giveawayApi = retrofit.create(GiveawayApi.class);
            Call<GiveawayDAO> giveawayDAOCall = giveawayApi.addGiveaway(firebaseAuth.getCurrentUser().getUid(),
                    editContent.getText().toString(),
                    "https://res.cloudinary.com/dhzln70wz/image/upload/v1537276689/etude-house-treats-for-my-sweets-items.jpg",
                    spinnerRegion.getSelectedCountryName(),
                    Integer.parseInt(editParticipants.getText().toString()), "Active");
            giveawayDAOCall.enqueue(new Callback<GiveawayDAO>() {
                @Override
                public void onResponse(Call<GiveawayDAO> call, Response<GiveawayDAO> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(CreateGiveawayActivity.this, Navigation.class));
                        Toast.makeText(CreateGiveawayActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<GiveawayDAO> call, Throwable t) {
                    Toast.makeText(CreateGiveawayActivity.this, "Network Connection Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void uploadImage(Bitmap bitmap, Uri imageUri) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child(imageUri.toString());

        Log.d("URI", imageUri.toString());

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(CreateGiveawayActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });
    }
}
