package com.mtz.testwarna.adapter;


import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mtz.testwarna.R;
import com.mtz.testwarna.Username;
import com.mtz.testwarna.api.GiveawayParticipantsApi;
import com.mtz.testwarna.dao.GiveawayDAO;
import com.mtz.testwarna.dao.GiveawayParticipantDAO;
import com.mtz.testwarna.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


// Semua giveaway
public class AllGiveawayAdapter extends RecyclerView.Adapter<AllGiveawayAdapter.MyViewHolder> {

    private Context context;
    private List<GiveawayDAO> result;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    public AllGiveawayAdapter(Context context, List<GiveawayDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_recycle_adapter, viewGroup, false);
        final MyViewHolder holder = new MyViewHolder(v);

        return holder;
    }

    public void onBindViewHolder(@NonNull final AllGiveawayAdapter.MyViewHolder myViewHolder, int i) {
        final GiveawayDAO ga = result.get(i);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(ga.getUserId()).getValue() != null)  {
                    myViewHolder.userid.setText(dataSnapshot.child(ga.getUserId()).getValue(Username.class).getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        myViewHolder.userid.setText(ga.getUserId());
        myViewHolder.desc.setText(ga.getDescription());
        myViewHolder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                onClickJoin(ga.getId());
            }
        });
    }

    public int getItemCount() {
        return result.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView userid, desc;
        private ImageView user, photo;
        private Button btnJoin;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userid = (TextView) itemView.findViewById(R.id.userID);
            desc = (TextView) itemView.findViewById(R.id.descGA);
            btnJoin = (Button) itemView.findViewById(R.id.btnJoin);
            user = (ImageView) itemView.findViewById(R.id.imageUser);
            photo = (ImageView) itemView.findViewById(R.id.imageGiveaway);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Hey You Clicked On Me", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickJoin(int giveawayId)  {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GiveawayParticipantsApi giveawayParticipantsApi = retrofit
                .create(GiveawayParticipantsApi.class);
        Call<GiveawayParticipantDAO> joinGiveaway = giveawayParticipantsApi
                .joinGiveaway(firebaseAuth.getCurrentUser().getUid(), giveawayId);
        joinGiveaway.enqueue(new Callback<GiveawayParticipantDAO>() {
            @Override
            public void onResponse(Call<GiveawayParticipantDAO> call, Response<GiveawayParticipantDAO> response) {
                String message = response.message();
                if (response.isSuccessful()) {
                    Log.d("yay", message);
                }
            }

            @Override
            public void onFailure(Call<GiveawayParticipantDAO> call, Throwable t) {
                Log.d("yay", "full");
            }
        });
    }
}
