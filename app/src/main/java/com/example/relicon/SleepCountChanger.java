package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

public class SleepCountChanger extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    VideoView back_video;
    Button toMenu; TextView tw;
    SeekBar seekBar;
    static String myProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_count_changer);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        toMenu = (Button) findViewById(R.id.toMenu);
        toMenu.setOnClickListener(this);
        toMenu.setBackgroundResource(R.drawable.inset_ripped);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setBackgroundResource(R.drawable.inset_ripped);
        seekBar.setOnSeekBarChangeListener(this);
        tw = (TextView) findViewById(R.id.defaultSleep);
        myProgress = String.valueOf(seekBar.getProgress());
        tw.setText(String.valueOf(seekBar.getProgress()));
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
        if (v.getId() == R.id.toMenu){
            Intent intent = new Intent(this, MenuActivity.class);
            WriteSleepDef wsf = new WriteSleepDef();
            wsf.execute();
            startActivity(intent);
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tw.setText(String.valueOf(seekBar.getProgress()));
        myProgress = String.valueOf(seekBar.getProgress());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    class WriteSleepDef extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MainActivity.APP_PREFERENCES_DEFAULT_SLEEP,myProgress);
            editor.apply();
            editor.putString(MainActivity.APP_PREFERENCES_TODAY_SLEEP,"Default");
            editor.apply();


            return null;
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //tw.setText(String.valueOf(seekBar.getProgress()));
        myProgress = String.valueOf(seekBar.getProgress());
    }
}