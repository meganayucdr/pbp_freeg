package com.mtz.testwarna.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import com.mtz.testwarna.EditGiveawayActivity;
import com.mtz.testwarna.MainParticipant;
import com.mtz.testwarna.Navigation;
import com.mtz.testwarna.R;
import com.mtz.testwarna.api.GiveawayApi;
import com.mtz.testwarna.dao.GiveawayDAO;
import com.mtz.testwarna.network.RetrofitInstance;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//Giveaway yang dibuat
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
        giveawayViewHolder.txtContent.setText(giveawayDAO.getDescription());
        giveawayViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditGiveawayActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("content", giveawayDAO.getDescription());
                bundle.putString("participants", giveawayDAO.getParticipants());
                bundle.putString("image", giveawayDAO.getImage());
                bundle.putInt("id", giveawayDAO.getId());

                intent.putExtras(bundle);
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

        giveawayViewHolder.btnParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainParticipant.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", giveawayDAO.getId());
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class GiveawayViewHolder extends RecyclerView.ViewHolder {
        private TextView txtContent;
        //private ImageView imageGiveaway;
        private Button btnEdit;
        private Button btnDelete;
        private Button btnParticipants;

        public GiveawayViewHolder(@NonNull View itemView) {
            super(itemView);

            txtContent = (TextView) itemView.findViewById(R.id.txtContent);
            //imageGiveaway =
            btnEdit = (Button) itemView.findViewById(R.id.btnEdit);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            btnParticipants = (Button) itemView.findViewById(R.id.btnParticipants);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onClickDelete(int giveawayId)   {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GiveawayApi giveawayApi = retrofit.create(GiveawayApi.class);
        Call<ResponseBody> giveawayDAOCall = giveawayApi.deleteGiveaway(giveawayId);
        giveawayDAOCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                context.startActivity(new Intent(context, Navigation.class));
                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network Connection Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
