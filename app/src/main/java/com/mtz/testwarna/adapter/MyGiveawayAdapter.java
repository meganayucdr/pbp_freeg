package com.mtz.testwarna.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mtz.testwarna.EditActivity;
import com.mtz.testwarna.R;
import com.mtz.testwarna.api.GiveawayApi;
import com.mtz.testwarna.dao.GiveawayDAO;
import com.mtz.testwarna.network.RetrofitInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
        giveawayViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                onClickDelete(giveawayDAO.getId());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickDelete(int giveawayId)   {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GiveawayApi giveawayApi = retrofit.create(GiveawayApi.class);
        Call<String> giveawayDAOCall = giveawayApi.deleteGiveaway("Non Active", giveawayId);
        giveawayDAOCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, "Network Connection Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
