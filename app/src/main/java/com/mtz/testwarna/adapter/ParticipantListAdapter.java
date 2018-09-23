package com.mtz.testwarna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.mtz.testwarna.dao.GiveawayParticipantDAO;

import java.util.List;

public class ParticipantListAdapter extends RecyclerView.Adapter<ParticipantListAdapter.ParticipantViewHolder> {

    private Context context;
    private List<GiveawayParticipantDAO> result;

    public ParticipantListAdapter(Context context, List<GiveawayParticipantDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public ParticipantListAdapter.ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_participants, viewGroup, false);
        final ParticipantViewHolder holder = new ParticipantViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ParticipantListAdapter.ParticipantViewHolder participantViewHolder, int i) {
        final GiveawayParticipantDAO giveawayParticipantDAO = result.get(i);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Username username = dataSnapshot.child(giveawayParticipantDAO
                        .getUserId()).getValue(Username.class);
                if (username != null) {
                    Log.d("USERNAME", username.getUsername());
                    participantViewHolder.txtUser.setText(username.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ParticipantViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageUser;
        private TextView txtUser;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = (TextView) itemView.findViewById(R.id.txtUserParticipation);
            imageUser = (ImageView) itemView.findViewById(R.id.imageUser);
        }
    }
}
