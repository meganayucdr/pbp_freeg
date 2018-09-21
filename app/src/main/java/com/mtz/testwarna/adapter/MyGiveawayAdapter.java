package com.mtz.testwarna.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mtz.testwarna.EditActivity;
import com.mtz.testwarna.R;
import com.mtz.testwarna.dao.GiveawayDAO;

import java.util.List;

public class MyGiveawayAdapter extends RecyclerView.Adapter<MyGiveawayAdapter.GiveawayViewHolder> {

    private Context context;
    private List<GiveawayDAO> result;

    public MyGiveawayAdapter(Context context, List<GiveawayDAO> result) {
        this.context = context;
        this.result = result;
    }

    @NonNull
    @Override
    public MyGiveawayAdapter.GiveawayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_my_giveaway, viewGroup, false);
        final GiveawayViewHolder holder = new GiveawayViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyGiveawayAdapter.GiveawayViewHolder giveawayViewHolder, int i) {
        final GiveawayDAO giveawayDAO = result.get(i);
        giveawayViewHolder.txtContent.setText(giveawayDAO.getContent());
        giveawayViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("image", giveawayDAO.getImage());
                intent.putExtra("giveawayId", giveawayDAO.getId());
                intent.putExtra("content", giveawayDAO.getContent());
                intent.putExtra("location", giveawayDAO.getLocation());
                intent.putExtra("participants", giveawayDAO.getParticipants());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class GiveawayViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContent;
        //private ImageView imageGiveaway;
        private Button btnEdit;
        private Button btnDelete;
        public GiveawayViewHolder(@NonNull View itemView) {
            super(itemView);

            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            //imageGiveaway =
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
