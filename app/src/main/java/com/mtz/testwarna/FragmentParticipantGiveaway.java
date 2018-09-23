package com.mtz.testwarna;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
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
import com.mtz.testwarna.adapter.ParticipantListAdapter;
import com.mtz.testwarna.api.GiveawayParticipantsApi;
import com.mtz.testwarna.dao.GiveawayDAO;
import com.mtz.testwarna.dao.GiveawayParticipantDAO;
import com.mtz.testwarna.network.RetrofitInstance;
import com.mtz.testwarna.value.GiveawayParticipantValue;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentParticipantGiveaway extends Fragment {
    private View view;
    private List<GiveawayParticipantDAO> participantList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ParticipantListAdapter participantListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    FirebaseAuth firebaseAuth;
    private int id;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt("id");
        }
        view = inflater.inflate(R.layout.activity_participants, container, false);
        recyclerView = view.findViewById(R.id.rec_participants);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));
        participantListAdapter = new ParticipantListAdapter(view.getContext(), participantList);
        layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(participantListAdapter);

        firebaseAuth = FirebaseAuth.getInstance();

        getData();

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getData()  {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GiveawayParticipantsApi apiClient = retrofit.create(GiveawayParticipantsApi.class);

        Call<GiveawayParticipantValue> giveawayParticipantValueCall = apiClient.getGiveawayParticipantsList(id);
        giveawayParticipantValueCall.enqueue(new Callback<GiveawayParticipantValue>() {
            @Override
            public void onResponse(Call<GiveawayParticipantValue> call, Response<GiveawayParticipantValue> response) {
                participantListAdapter.notifyDataSetChanged();
                List<GiveawayParticipantDAO> participantDAOList = response.body().getData();

                if (participantDAOList.isEmpty())   {
                    Log.d("ERROR", "Empty");
                } else {
                    participantListAdapter = new ParticipantListAdapter(view.getContext(), participantDAOList);
                    recyclerView.setAdapter(participantListAdapter);
                    Toast.makeText(view.getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GiveawayParticipantValue> call, Throwable t) {
                Toast.makeText(view.getContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
