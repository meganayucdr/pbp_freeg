package com.mtz.testwarna;

import android.app.Fragment;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.Toast;

import com.mtz.testwarna.adapter.AllGiveawayAdapter;
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

public class FragmentShow extends Fragment {
    View myView;
    SessionManager session;
    SharedPreferences sp;
    Button btnGoEdit, btnGoEditPs;
    private List<GiveawayDAO> mListStudent=new ArrayList<>();
    private RecyclerView recyclerView;
    private AllGiveawayAdapter allGiveawayAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_show, container, false);
        recyclerView=myView.findViewById(R.id.recV);
        recyclerView.addItemDecoration(new DividerItemDecoration(myView.getContext(), LinearLayoutManager.VERTICAL));
        allGiveawayAdapter =new AllGiveawayAdapter(myView.getContext(), mListStudent);
        RecyclerView.LayoutManager mlayoutManager= new LinearLayoutManager(myView.getContext());
        recyclerView.setLayoutManager(mlayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(allGiveawayAdapter);

        //SET REC V

        /*Retrofit.Builder builder= new Retrofit.Builder()
                .baseUrl("http://freeg.thekingcorp.org/freeg_api/public/api/")
                .addConverterFactory(GsonConverterFactory.create());*/

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GiveawayApi apiClient = retrofit.create(GiveawayApi.class);
        Call<GiveawayValue> gaDAOcall = apiClient.getAllGiveaways();

        gaDAOcall.enqueue(new Callback<GiveawayValue>() {
            @Override
            public void onResponse(Call<GiveawayValue> call, Response<GiveawayValue> response) {
                allGiveawayAdapter.notifyDataSetChanged();
                List<GiveawayDAO> giveawayList = response.body().getData();
                Log.d("aw", giveawayList.toString());
                if (giveawayList.isEmpty()) {
                    Log.d("ERROR", "Empty");
                } else {
                    allGiveawayAdapter =new AllGiveawayAdapter(myView.getContext(), giveawayList);
                    recyclerView.setAdapter(allGiveawayAdapter);
                    Toast.makeText(myView.getContext(), "Welcome", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GiveawayValue> call, Throwable t) {
                Toast.makeText(myView.getContext(), "Network Connection Error", Toast.LENGTH_SHORT).show();
            }
        });

        return myView;
    }

}