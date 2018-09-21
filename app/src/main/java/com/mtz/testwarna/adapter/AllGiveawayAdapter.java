package com.mtz.testwarna.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mtz.testwarna.R;
import com.mtz.testwarna.dao.GiveawayDAO;

import java.util.List;

public class AllGiveawayAdapter extends RecyclerView.Adapter<AllGiveawayAdapter.MyViewHolder> {

    private Context context;
    private List<GiveawayDAO> result;


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
        myViewHolder.userid.setText(ga.getUserId());
        myViewHolder.desc.setText(ga.getContent());
        myViewHolder.btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

}
