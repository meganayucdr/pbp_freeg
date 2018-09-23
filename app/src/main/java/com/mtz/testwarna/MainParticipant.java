package com.mtz.testwarna;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mtz.testwarna.adapter.TabAdapter;

public class MainParticipant extends AppCompatActivity {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_participant);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        adapter = new TabAdapter(getSupportFragmentManager());
        FragmentParticipantGiveaway fragmentParticipantGiveaway = new FragmentParticipantGiveaway();
        fragmentParticipantGiveaway.setArguments(bundle);
        adapter.addFragment(fragmentParticipantGiveaway, "Participants");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
