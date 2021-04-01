package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.fitness.FitnessOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    VideoView back_video;
    ImageView relicon_h;
    Button next,yes_miband5,no_miband5;
    TextView info_miband5;
    ImageView miband5;
    Animation mFadeInAnim, mFadeOutAnim, comboAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        miband5 = findViewById(R.id.miband5);
        miband5.setVisibility(View.INVISIBLE);
        info_miband5 = findViewById(R.id.info_miband5);
        info_miband5.setVisibility(View.INVISIBLE);
        yes_miband5 = findViewById(R.id.yes_miband5);
        yes_miband5.setVisibility(View.INVISIBLE);
        no_miband5 = findViewById(R.id.no_miband5);
        no_miband5.setVisibility(View.INVISIBLE);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();

        relicon_h = findViewById(R.id.relicon_h);

        next = findViewById(R.id.nextToBracletQuestion);
        next.setBackgroundResource(R.drawable.inset_ripped);
        //just comment
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        mFadeInAnim = AnimationUtils.loadAnimation(this,R.anim.fadein);
        mFadeOutAnim = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        comboAnim = AnimationUtils.loadAnimation(this,R.anim.comboanim);
        relicon_h.startAnimation(mFadeInAnim);

        yes_miband5.setBackgroundResource(R.drawable.inset_ripped);
        no_miband5.setBackgroundResource(R.drawable.inset_ripped);


        next.startAnimation(mFadeInAnim);

        next.setOnClickListener(v -> {
            reliconAnimatorStart();
            miband5.setVisibility(View.VISIBLE);
            miband5.startAnimation(comboAnim);
            next.startAnimation(mFadeOutAnim);
            next.setVisibility(View.INVISIBLE);
            info_miband5.setVisibility(View.VISIBLE);
            info_miband5.startAnimation(comboAnim);
            yes_miband5.setVisibility(View.VISIBLE); no_miband5.setVisibility(View.VISIBLE);
            yes_miband5.startAnimation(mFadeInAnim); no_miband5.startAnimation(mFadeInAnim);

            yes_miband5.setClickable(true);
            no_miband5.setClickable(true);

            yes_miband5.setOnClickListener(this);
            no_miband5.setOnClickListener(this);
        });


    }

    private void reliconAnimatorStart() {
        relicon_h.startAnimation(mFadeOutAnim);
        relicon_h.setVisibility(View.INVISIBLE);
        relicon_h.destroyDrawingCache();
    }


    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onResume() {
        back_video.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        back_video.suspend();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        back_video.stopPlayback();
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.yes_miband5){
            Intent intent = new Intent(this, TryingConnectionActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.no_miband5){
            Intent intent2 = new Intent(this, SleepCountChanger.class);
            startActivity(intent2);
        }
    }
}