package com.mtz.testwarna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class Navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView nama, email;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        android.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.framelay, new FragmentUserProfile()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_myprofile) {
            fragmentManager.beginTransaction().replace(R.id.framelay, new FragmentUserProfile()).commit();
        } else if (id == R.id.nav_giveaways) {
            fragmentManager.beginTransaction().replace(R.id.framelay, new FragmentShow()).commit();
        } else if (id == R.id.nav_mygiveaway) {
            fragmentManager.beginTransaction().replace(R.id.framelay, new FragmentMyGiveaway()).commit();
        } else if (id == R.id.nav_jgiveaway) {
            fragmentManager.beginTransaction().replace(R.id.framelay, new FragmentJoinedGiveaway()).commit();
        } else if (id == R.id.nav_create) {
            Intent i = new Intent(Navigation.this, CreateGiveawayActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_signout) {
            SessionManager session = new SessionManager(Navigation.this);
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
