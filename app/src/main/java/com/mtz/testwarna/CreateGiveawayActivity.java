package com.mtz.testwarna;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;
import com.mtz.testwarna.api.GiveawayApi;
import com.mtz.testwarna.network.RetrofitInstance;

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
    private RetrofitInstance retrofitInstance = new RetrofitInstance();
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_giveaway);

        firebaseAuth = FirebaseAuth.getInstance();

        setAttribute();
        assignUser();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onClickPost();
            }
        });
    }

    private void setAttribute() {
        txtUser = (TextView) findViewById(R.id.txtUser);
        spinnerRegion = (CountryCodePicker) findViewById(R.id.ccpcreate);
        editParticipants = (EditText) findViewById(R.id.txtParticipants);
        editContent = (EditText) findViewById(R.id.editContent);
        btnPost = (Button) findViewById(R.id.btnPost);
    }
    private void assignUser()   {
        if (firebaseAuth.getCurrentUser() != null)  {
            txtUser.setText(firebaseAuth.getCurrentUser().getDisplayName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickPost()  {
        if (editParticipants.getText().toString().isEmpty() ||
                editContent.getText().toString().isEmpty())   {
            Toast.makeText(CreateGiveawayActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            //Post data into API
            //Buid retrofit
            Retrofit retrofit = retrofitInstance.getRetrofitInstance();
            GiveawayApi giveawayApi = retrofit.create(GiveawayApi.class);
            Call<String> giveawayDAOCall = giveawayApi.addGiveaway(firebaseAuth.getCurrentUser().getUid() ,editContent.getText().toString(),
                    "https://res.cloudinary.com/dhzln70wz/image/upload/v1537276689/etude-house-treats-for-my-sweets-items.jpg",
                    Integer.parseInt(editParticipants.getText().toString()), "Active");
            giveawayDAOCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(CreateGiveawayActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(CreateGiveawayActivity.this, HomeActivity.class);
//                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(CreateGiveawayActivity.this, "Network Connection Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
