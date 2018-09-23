package com.mtz.testwarna.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtz.testwarna.R;
import com.mtz.testwarna.dao.GiveawayDAO;

import java.util.List;

public class WonGiveawayAdapter extends RecyclerView.Adapter<WonGiveawayAdapter.WonGiveawayHolder> {

    private Context context;
    private List<GiveawayDAO> result;

    public WonGiveawayAdapter(Context context, List<GiveawayDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public WonGiveawayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_won_giveaway, viewGroup, false);
        final WonGiveawayHolder holder = new WonGiveawayHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WonGiveawayHolder wonGiveawayHolder, int i) {
        final GiveawayDAO giveawayDAO = result.get(i);
        wonGiveawayHolder.txtContent.setText(giveawayDAO.getDescription());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class WonGiveawayHolder extends RecyclerView.ViewHolder {
        private ImageView imageContent;
        private TextView txtContent;
        public WonGiveawayHolder(@NonNull View itemView) {
            super(itemView);
            imageContent = itemView.findViewById(R.id.giveawayImage);
            txtContent = itemView.findViewById(R.id.txtContent);
        }
    }
}
