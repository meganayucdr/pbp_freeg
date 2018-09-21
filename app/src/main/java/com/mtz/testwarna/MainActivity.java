package com.mtz.testwarna;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.List;
import se.simbio.encryption.Encryption;

public class MainActivity extends AppCompatActivity {
    private Button btnCreate, btnLogin;
    private Username user;
    private TextView btnForgot;
    boolean emailVerified;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    List<Username> listUser;
    EditText username, password;
    private SharedPreferences sp;
    final DatabaseReference database = FirebaseDatabase.getInstance().getReference("User");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAttribute();
        /*--------------------------------------------
        FUNGSI UNTUK REGISTER
         */
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Register.class);
                startActivity(i);
            }
        });
        /*--------------------------------------------*/


        /*--------------------------------------------
        FUNGSI UNTUK LOGIN
         */
        sp = getSharedPreferences("login",MODE_PRIVATE);

        if(sp.getBoolean("isLogged",false)==true){

            //belom set lokasi
            if(sp.getString("location", "").equals("-")) {
                Intent i = new Intent(MainActivity.this, LocationSetter.class);
                startActivity(i);
            }

            else {
                Intent i = new Intent(MainActivity.this, Navigation.class);
                startActivity(i);
            }
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickLogin();
            }
        });
        /*--------------------------------------------*/

        /*--------------------------------------------
        FUNGSI UNTUK RESET PASSWORD
         */
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Forgot.class);
                startActivity(i);
            }
        });
        /*--------------------------------------------*/



    }

    public void onClickLogin() {
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int sukses=0;
                Iterable<DataSnapshot> userChildren = dataSnapshot.getChildren();
                for(DataSnapshot dataUser : userChildren) {
                    user = dataUser.getValue(Username.class);
                    if (user.getUsername().equals(username.getText().toString())) {
                        checkEmailAuth(user);
                        sukses=1;
                        break;
                    }
                }

                if(sukses==0) {
                    Toast.makeText(getApplicationContext(), "Ga ada", Toast.LENGTH_LONG).show();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Gagal loading ke database!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkEmailAuth(final Username user) {
        String key = "YourKey";
        String salt = "YourSalt";
        byte[] iv = new byte[16];
        Encryption encryption = Encryption.getDefault(key, salt, iv);
        String encrypted = encryption.encryptOrNull(password.getText().toString());
        Log.d("ENCRYPTED TO FIREBASE", encrypted);
        mAuth.signInWithEmailAndPassword(user.getEmail(), encrypted)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser userver = mAuth.getCurrentUser();
                            if (userver.isEmailVerified() == false) {
                                Toast.makeText(getApplicationContext(), "Belom di verif.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                sp.edit().putString("email", user.getEmail()).apply();
                                sp.edit().putString("username", user.getUsername()).apply();
                                sp.edit().putString("fullname", user.getName()).apply();
                                sp.edit().putString("location", user.getLocation()).apply();
                                sp.edit().putBoolean("isLogged", true).apply();

                                if (user.getLocation().equals("-")) {
                                    Intent i = new Intent(MainActivity.this, LocationSetter.class);
                                    startActivity(i);
                                } else {
                                    Intent i = new Intent(MainActivity.this, Navigation.class);
                                    startActivity(i);
                                }
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }

    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    public void setAttribute() {
        btnLogin = (Button) findViewById(R.id.btnLogin);
        username = (EditText) findViewById(R.id.fieldUsername);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnForgot = (TextView) findViewById(R.id.btnForgot);
        password = (EditText) findViewById(R.id.fieldPassword);
    }

}
