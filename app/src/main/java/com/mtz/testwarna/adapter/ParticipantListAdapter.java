package com.mtz.testwarna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.mtz.testwarna.R;
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
        View v = LayoutInflater.from(context).inflate(R.layout.activity_participants, viewGroup, false);
        final ParticipantViewHolder holder = new ParticipantViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantListAdapter.ParticipantViewHolder participantViewHolder, int i) {
        final GiveawayParticipantDAO giveawayParticipantDAO = result.get(i);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ParticipantViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageUser;
        private TextView txtUser;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);

            txtUser = (TextView) itemView.findViewById(R.id.txtUser);
            imageUser = (ImageView) itemView.findViewById(R.id.imageUser);
        }
    }
}
