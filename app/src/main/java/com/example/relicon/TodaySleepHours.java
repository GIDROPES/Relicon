package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.MailTo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class TodaySleepHours extends AppCompatActivity implements View.OnClickListener {
    VideoView back_video;
    Button aDef, lessDef, moreDef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_sleep_hours);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        aDef = (Button) findViewById(R.id.aDef); aDef.setBackgroundResource(R.drawable.inset_ripped); aDef.setOnClickListener(this);
        lessDef = (Button) findViewById(R.id.lessDef); lessDef.setBackgroundResource(R.drawable.inset_ripped); lessDef.setOnClickListener(this);
        moreDef = (Button) findViewById(R.id.moreDef); moreDef.setBackgroundResource(R.drawable.inset_ripped); moreDef.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        back_video.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        back_video.suspend();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        back_video.stopPlayback();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, MenuActivity.class);
        SharedPreferences myData = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = myData.edit();
        if (v.getId() == R.id.aDef){
            editor.putString(MainActivity.APP_PREFERENCES_TODAY_SLEEP, "Default");
            editor.apply();
            startActivity(intent);
        }
        if (v.getId() == R.id.lessDef){
            editor.putString(MainActivity.APP_PREFERENCES_TODAY_SLEEP, "Less");
            editor.apply();
            startActivity(intent);
        }
        if (v.getId() == R.id.moreDef){
            editor.putString(MainActivity.APP_PREFERENCES_TODAY_SLEEP, "More");
            editor.apply();
            startActivity(intent);
        }
    }
}