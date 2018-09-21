package com.mtz.testwarna;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import se.simbio.encryption.Encryption;

public class Forgot extends AppCompatActivity {

    private EditText fieldEmail;
    private Button btnReset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        setAttribute();

        /*--------------------------------------------
        FUNGSI UNTUK RESET PASSWORD
         */
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendResetPassword();

            }
        });
        /*--------------------------------------------*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void setAttribute() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.backbtn_reset);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnReset = (Button) findViewById(R.id.btnReset);
        fieldEmail = (EditText) findViewById(R.id.fieldReset_Email);
    }

    public void sendResetPassword() {
        FirebaseAuth.getInstance().sendPasswordResetEmail(fieldEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Email has been sent to your mail.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Email not valid.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    public void makePDF(){}
}
