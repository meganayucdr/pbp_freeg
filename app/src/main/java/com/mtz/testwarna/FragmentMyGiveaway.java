package com.mtz.testwarna;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mtz.testwarna.adapter.MyGiveawayAdapter;
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

public class FragmentMyGiveaway extends Fragment{
    View view;
    SessionManager sessionManager;
    private List<GiveawayDAO> giveawayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MyGiveawayAdapter myGiveawayAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseAuth firebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_my_giveaway, container, false);
        recyclerView = view.findViewById(R.id.rec_my_giveaway);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        myGiveawayAdapter = new MyGiveawayAdapter(view.getContext(), giveawayList);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myGiveawayAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        getData();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData()  {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GiveawayApi apiClient = retrofit.create(GiveawayApi.class);
        Call<GiveawayValue> giveawayValueCall = apiClient.getGiveawaysByUser(firebaseAuth.getCurrentUser().getUid());

        giveawayValueCall.enqueue(new Callback<GiveawayValue>() {
            @Override
            public void onResponse(Call<GiveawayValue> call, Response<GiveawayValue> response) {
                myGiveawayAdapter.notifyDataSetChanged();
                List<GiveawayDAO> giveawayDAOList = response.body().getData();

                if (giveawayList.isEmpty()) {
                    Log.d("ERROR", "Empty");
                } else {
                    myGiveawayAdapter = new MyGiveawayAdapter(view.getContext(), giveawayDAOList);
                    recyclerView.setAdapter(myGiveawayAdapter);
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
