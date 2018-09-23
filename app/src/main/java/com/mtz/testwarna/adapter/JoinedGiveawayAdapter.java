package com.mtz.testwarna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mtz.testwarna.R;
import com.mtz.testwarna.dao.GiveawayDAO;

import org.w3c.dom.Text;

import java.util.List;

//
public class JoinedGiveawayAdapter extends RecyclerView.Adapter<JoinedGiveawayAdapter.JoinedGiveawayHolder> {

    private Context context;
    private List<GiveawayDAO> result;

    public JoinedGiveawayAdapter(Context context, List<GiveawayDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public JoinedGiveawayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_joined_giveaway, viewGroup, false);
        final JoinedGiveawayHolder holder = new JoinedGiveawayHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull JoinedGiveawayHolder joinedGiveawayHolder, int i) {
        final GiveawayDAO giveawayDAO = result.get(i);
        joinedGiveawayHolder.txtContent.setText(giveawayDAO.getDescription());
        joinedGiveawayHolder.txtContent.setText(giveawayDAO.getStatus());
        joinedGiveawayHolder.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class JoinedGiveawayHolder extends RecyclerView.ViewHolder {
        private TextView txtContent;
        private TextView txtStatus;
        private Button btnQuit;

        public JoinedGiveawayHolder(@NonNull View itemView) {
            super(itemView);

            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            txtStatus = (TextView) itemView.findViewById(R.id.txtStatus);
            btnQuit = (Button) itemView.findViewById(R.id.btnQuit);
        }
    }
}
