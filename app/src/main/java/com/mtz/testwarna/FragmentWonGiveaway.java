package com.mtz.testwarna;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mtz.testwarna.adapter.WonGiveawayAdapter;
import com.mtz.testwarna.api.GiveawayApi;
import com.mtz.testwarna.dao.GiveawayDAO;
import com.mtz.testwarna.network.RetrofitInstance;
import com.mtz.testwarna.value.GiveawayValue;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentWonGiveaway extends Fragment {
    private View view;
    private List<GiveawayDAO> giveawayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private WonGiveawayAdapter wonGiveawayAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_won_giveaway, container, false);
        recyclerView = view.findViewById(R.id.rec_won_giveaway);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        wonGiveawayAdapter = new WonGiveawayAdapter(view.getContext(), giveawayList);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(wonGiveawayAdapter);

        getData();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData()  {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GiveawayApi apiClient = retrofit.create(GiveawayApi.class);

        Call<GiveawayValue> giveawayValueCall = apiClient.getWonGiveaway(FirebaseAuth.getInstance().getCurrentUser().getUid());

        giveawayValueCall.enqueue(new Callback<GiveawayValue>() {
            @Override
            public void onResponse(Call<GiveawayValue> call, Response<GiveawayValue> response) {
                wonGiveawayAdapter.notifyDataSetChanged();
                List<GiveawayDAO> giveawayDAOList = response.body().getData();

                if (giveawayDAOList.isEmpty())  {
                    Log.d("ERROR", "Empty");
                } else  {
                    wonGiveawayAdapter = new WonGiveawayAdapter(view.getContext(), giveawayDAOList);
                    recyclerView.setAdapter(wonGiveawayAdapter);
                    Toast.makeText(view.getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GiveawayValue> call, Throwable t) {
                Toast.makeText(view.getContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
