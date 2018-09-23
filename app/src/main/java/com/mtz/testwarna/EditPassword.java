package com.mtz.testwarna;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rengwuxian.materialedittext.MaterialEditText;

import se.simbio.encryption.Encryption;

public class EditPassword extends AppCompatActivity {
    Button btnEditPs;
    MaterialEditText passwordlama, passwordbaru, confpass;

    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
    Username user;
    String key;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
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
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal loading ke database!", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditPs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //belum di
                String keys = "YourKey";
                String salt = "YourSalt";
                byte[] iv = new byte[16];
                Encryption encryption = Encryption.getDefault(keys, salt, iv);
                String encrypted = encryption.encryptOrNull(passwordbaru.getText().toString());

                if(!encryption.encryptOrNull(passwordlama.getText().toString()).equals(user.getPassword())) {
                    Toast.makeText(getApplicationContext(), "Recent password invalid!", Toast.LENGTH_SHORT).show();
                }

                else
                {

                    if(!passwordbaru.getText().toString().equals(confpass.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Confirmation password invalid!", Toast.LENGTH_SHORT).show();
                    }

                    else {
                        database.child(key).child("password").setValue(encrypted);
                        Toast.makeText(getApplicationContext(), "Password successfully edited!", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAttribute() {
        btnEditPs = (Button) findViewById(R.id.btnEditPs);
        passwordlama = (MaterialEditText) findViewById(R.id.passwordlama);
        passwordbaru = (MaterialEditText) findViewById(R.id.passwordbaru);
        confpass = (MaterialEditText) findViewById(R.id.confirmpassbaru);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarEditPs);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
