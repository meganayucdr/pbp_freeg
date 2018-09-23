package com.mtz.testwarna;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vansuita.materialabout.builder.AboutBuilder;
import com.vansuita.materialabout.views.AboutView;
import com.vansuita.materialabout.views.CircleImageView;

import java.io.IOException;
import java.net.URL;

public class FragmentUserProfile extends Fragment{
    View myView;
    SessionManager session;
    SharedPreferences sp;
    Button btnGoEdit, btnGoEditPs;
    Username user;
    String uid;
    String urlimage;
    ImageView circleImg;
    String link;
    String key;
    int i=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_my_profile, container, false);
        btnGoEdit = (Button) myView.findViewById(R.id.btnGoEdit);
        btnGoEditPs = (Button) myView.findViewById(R.id.btnGoEditPs);
        circleImg = (ImageView) myView.findViewById(R.id.profile_depan);
        session = new SessionManager(myView.getContext());

        final FrameLayout flHolder = myView.findViewById(R.id.about);
        if(session.pref.getString("photo", "-").equals("-")) {
            Glide.with(myView.getContext())
                    .load(R.mipmap.profile_picture)
                    .into(circleImg);
        }

        else {
            Glide.with(myView.getContext())
                    .load(session.pref.getString("photo", "-"))
                    .into(circleImg);
        }

        AboutBuilder builder = AboutBuilder.with(myView.getContext())
                .setLinksAnimated(true)
                .setDividerDashGap(13)
                .setName(session.pref.getString("fullname", ""))
                .setSubTitle(session.pref.getString("username", ""))
                .setLinksColumnsCount(1)
                .setBrief(session.pref.getString("email", ""))
                .addEmailLink(session.pref.getString("email", ""))
                .setWrapScrollView(true)
                .setShowAsCard(true);
        AboutView view = builder.build();

        flHolder.addView(view);
        btnGoEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditActivity.class);
                startActivity(i);
            }
        });

        btnGoEditPs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditPassword.class);
                startActivity(i);
            }
        });
        return myView;
    }
}
