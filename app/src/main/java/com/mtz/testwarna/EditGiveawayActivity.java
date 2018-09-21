package com.mtz.testwarna;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class EditGiveawayActivity extends AppCompatActivity {

    private TextView txtUser2;
    private CountryCodePicker spinnerRegion2;
    private EditText editParticipants2;
    private EditText editContent2;
    private Button btnUpdate;
    private ImageView img, imgUser;
    private int giveawayId;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_giveaway);

        firebaseAuth = FirebaseAuth.getInstance();

        setAttribute();
        //assignFromIntent();
        //assignUser();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onClickUpdate();
            }
        });
    }

    private void setAttribute() {
        txtUser2 = (TextView) findViewById(R.id.txtUser2);
        spinnerRegion2 = (CountryCodePicker) findViewById(R.id.ccpedit);
        editParticipants2 = (EditText) findViewById(R.id.editParticipants2);
        editContent2 = (EditText) findViewById(R.id.editContent2);
        img = (ImageView) findViewById(R.id.imageContent2);
        imgUser = (ImageView) findViewById(R.id.imageUser2);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
    }


    private void assignUser()   {
        if (firebaseAuth.getCurrentUser() != null)  {
            txtUser2.setText(firebaseAuth.getCurrentUser().getDisplayName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickUpdate()    {
        if (editParticipants2.getText().toString().isEmpty() ||
                editContent2.getText().toString().isEmpty())   {
            Toast.makeText(EditGiveawayActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
        } else {
            //Post data into API
            //Buid retrofit
            Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
            GiveawayApi giveawayApi = retrofit.create(GiveawayApi.class);
            Call<String> giveawayDAOCall = giveawayApi.updateGiveaway(firebaseAuth.getCurrentUser().getUid() ,editContent2.getText().toString(),
                    "https://res.cloudinary.com/dhzln70wz/image/upload/v1537276689/etude-house-treats-for-my-sweets-items.jpg",
                    Integer.parseInt(editParticipants2.getText().toString()), "Active", giveawayId);
            giveawayDAOCall.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Toast.makeText(EditGiveawayActivity.this, "Success", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(EditGiveawayActivity.this, HomeActivity.class);
//                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(EditGiveawayActivity.this, "Network Connection Fail", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void assignFromIntent() {
        Intent intent = getIntent();
        editContent2.setText(intent.getStringExtra("content"));
        editParticipants2.setText(intent.getStringExtra("participant"));
        //spinnerRegion2.setSelection(Integer.parseInt(intent.getStringExtra("selectedRegion")));
        giveawayId = Integer.parseInt(intent.getStringExtra("giveawayId"));
    }
}
