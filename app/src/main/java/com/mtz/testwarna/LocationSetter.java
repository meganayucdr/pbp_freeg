package com.mtz.testwarna;

import android.content.SharedPreferences;
import android.support.v4.os.ConfigurationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.Locale;

import javax.mail.Quota;

public class LocationSetter extends AppCompatActivity {
    CountryCodePicker ccp;
    Button btnGetLoc;
    Locale locale;
    SharedPreferences sp;
    Username user;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_setter);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        btnGetLoc = (Button) findViewById(R.id.btnGetLoc);
        btnGetLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), ccp.getSelectedCountryName(), Toast.LENGTH_SHORT).show();

                database.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> userChildren = dataSnapshot.getChildren();
                        for(DataSnapshot dataUser : userChildren) {
                            user = dataUser.getValue(Username.class);
                            if(user.getUsername().equals(sp.getString("username", ""))) {
                                String key = dataUser.getKey();
                                database.child(key).child("location").setValue(ccp.getSelectedCountryName());
                                database.child(key).child("location_id").setValue(ccp.getSelectedCountryNameCode());
                                sp.edit().putString("location", ccp.getSelectedCountryName()).apply();
                                break;
                            }
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Gagal loading ke database!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
