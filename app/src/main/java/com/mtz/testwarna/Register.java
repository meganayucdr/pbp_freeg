package com.mtz.testwarna;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import se.simbio.encryption.Encryption;

public class Register extends AppCompatActivity {
    EditText username, password, email, fullname;
    Button btnCreate;
    Username data;
    List<Username> listUser;
    Username user;
    private String encryptedPassword;
    private FirebaseAuth mAuth=FirebaseAuth.getInstance();
    boolean checkDatabase = true;
    boolean connected = false;
    int indexOfList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseUsername = database.getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setAttribute();

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else
            connected = false;

        if(connected==true) {
            listUser = new ArrayList<Username>();
            databaseUsername.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> userChildren = dataSnapshot.getChildren();
                    listUser.clear();
                    for(DataSnapshot dataUser : userChildren) {
                        user = dataUser.getValue(Username.class);
                        listUser.add(user);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        else {
            Toast.makeText(getApplicationContext(), "Gagal loading ke database!", Toast.LENGTH_SHORT).show();
            checkDatabase = false;
        }
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRegister();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean validationFieldCheck(String field, String input, int min, int max) {
        boolean valid = true;

        if(TextUtils.isEmpty(input)) {
            valid = false;
            Toast.makeText(getApplicationContext(), field + " masih kosong", Toast.LENGTH_SHORT).show();
        }

        else {
            if(input.length()<min) {
                valid = false;
                Toast.makeText(getApplicationContext(), field + " minimal " + min + " karakter", Toast.LENGTH_SHORT).show();
            }

            else if(input.length()>max) {
                valid = false;
                Toast.makeText(getApplicationContext(), field + " maximal " + max + " karakter", Toast.LENGTH_SHORT).show();
            }
        }

        return valid;
    }

    public boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public void onClickRegister() {
        boolean userCheck, passCheck, emailCheck;
        userCheck = validationFieldCheck("Username", username.getText().toString(), 5, 12);
        passCheck = validationFieldCheck("Password", password.getText().toString(), 5, 12);
        emailCheck = isValidEmail(email.getText().toString());
        if(emailCheck==false) {
            Toast.makeText(getApplicationContext(), "Email tidak valid!", Toast.LENGTH_SHORT).show();
        }

        Log.d("Email valid : ", String.valueOf(emailCheck));

        if(userCheck==true && passCheck==true && emailCheck==true) {
            if(checkDatabase == true) {
                Log.d("Size", "Total data : " + listUser.size());
                indexOfList=-1;

                if(listUser.size()>0) {
                    for(int i=0; i<listUser.size(); i++) {
                        if(listUser.get(i).getUsername().equals(username.getText().toString())) {
                            indexOfList = i;
                        }
                    }
                }
                //Checking the index, if its still -1
                //-1 here means EMPTY COMPARISON
                if(indexOfList==-1) {
                    String key = "YourKey";
                    String salt = "YourSalt";
                    byte[] iv = new byte[16];
                    Encryption encryption = Encryption.getDefault(key, salt, iv);
                    String encrypted = encryption.encryptOrNull(password.getText().toString());
                    encryptedPassword=encrypted;
                    Log.d("ENCRYPTED TEXT", encryptedPassword);
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), encryptedPassword)
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String id = databaseUsername.push().getKey();
                                        data = new Username(fullname.getText().toString(),
                                                username.getText().toString(),
                                                encryptedPassword, email.getText().toString(), "-", "-", "-");
                                        databaseUsername.child(id).setValue(data);
                                        sendEmailVerification();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("FAIL", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(getApplicationContext(), "Register Gagal", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                }

                else {
                    Toast.makeText(getApplicationContext(), "Username telah terpakai", Toast.LENGTH_SHORT).show();
                }
            }

            else {
                Toast.makeText(getApplicationContext(), "Kamu butuh koneksi internet untuk mendaftar!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Register Sukses!\nCek Email untuk verifikasi",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }


    public void setAttribute() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        fullname = (EditText) findViewById(R.id.fieldregName);
        username = (EditText) findViewById(R.id.fieldregUsername);
        password = (EditText) findViewById(R.id.fieldregPassword);
        email = (EditText) findViewById(R.id.fieldregEmail);
        btnCreate = (Button) findViewById(R.id.btnregCreate);
    }
}

/*
String key = "YourKey";
String salt = "YourSalt";
byte[] iv = new byte[16];
Encryption encryption = Encryption.getDefault(key, salt, iv);
String encrypted = encryption.encryptOrNull("Text to be encrypt");
String decrypted = encryption.decryptOrNull(encrypted);
 */