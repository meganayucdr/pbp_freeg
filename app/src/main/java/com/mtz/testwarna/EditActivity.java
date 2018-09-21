package com.mtz.testwarna;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends AppCompatActivity {

    MaterialEditText editUsername, editFullname, editEmail;
    CountryCodePicker ccp;
    Button btnEdit;
    SharedPreferences sp;
    Username user;
    String uid;
    String urlimage;
    String link;
    String key;
    private CircleImageView btnImage;
    private Uri filePath;

    FirebaseStorage storage;
    StorageReference storageReference;

    private final int PICK_IMAGE_REQUEST = 71;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference("images");
        setAttribute();
        sp = getSharedPreferences("login", MODE_PRIVATE);


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> userChildren = dataSnapshot.getChildren();
                for(DataSnapshot dataUser : userChildren) {
                    user = dataUser.getValue(Username.class);
                    if(user.getUsername().equals(sp.getString("username", ""))) {
                        key = dataUser.getKey();
                        editUsername.setText(user.getUsername());
                        editFullname.setText(user.getName());
                        editEmail.setText(user.getEmail());
                        urlimage = user.getPhoto_uri();
                        Glide.with(EditActivity.this)
                                .load(urlimage)
                                .into(btnImage);
                        ccp.setCountryForNameCode(user.getLocation_id());
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal loading ke database!", Toast.LENGTH_SHORT).show();
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child(key).child("username").setValue(editUsername.getText().toString());
                database.child(key).child("fullname").setValue(editFullname.getText().toString());
                database.child(key).child("email").setValue(editEmail.getText().toString());
                database.child(key).child("location").setValue(ccp.getSelectedCountryName());
                database.child(key).child("location_id").setValue(ccp.getSelectedCountryNameCode());
                uploadImage();


            }
        });

        //UPLOAD IMAGE BUTTON
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAttribute() {
        editUsername = (MaterialEditText) findViewById(R.id.fieldeditUsername);
        editUsername.setFocusable(false);
        editUsername.setFocusableInTouchMode(false);
        editFullname = (MaterialEditText) findViewById(R.id.fieldeditName);
        editEmail = (MaterialEditText) findViewById(R.id.fieldeditEmail);
        btnImage = (CircleImageView) findViewById(R.id.profile_image);
        ccp = (CountryCodePicker) findViewById(R.id.ccpedit);
        btnEdit = (Button) findViewById(R.id.btnEdit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEdit);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            uid = UUID.randomUUID().toString();

            final StorageReference ref = storageReference.child(uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            storageReference.child(uid).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    link = uri.toString();
                                    Toast.makeText(getApplicationContext(), "MASOK PAK EKO", Toast.LENGTH_LONG).show();
                                    database.child(key).child("photo_uri").setValue(link);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                btnImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}
