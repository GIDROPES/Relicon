package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.concurrent.TimeUnit;

public class BraceletReadSleep extends AppCompatActivity {
    VideoView back_video; TextView sleepCount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet_read_sleep);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        SleepTextAnim sleepTextAnim = new SleepTextAnim();
        sleepTextAnim.execute();
    }

    class SleepTextAnim extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            Animation mFadeInAnim, mFadeOutAnim;
            mFadeInAnim = AnimationUtils.loadAnimation(BraceletReadSleep.this,R.anim.fadein);
            mFadeOutAnim = AnimationUtils.loadAnimation(BraceletReadSleep.this,R.anim.fadeout);

            sleepCount = (TextView) findViewById(R.id.sleepCount);
            sleepCount.setText("Вы спали " + TryingConnectionActivity.SLEEP_GOOGLE_FIT_HOURS + " часов");      //Вытащил из другой активности информацию о часах сна
            Intent intent = new Intent(BraceletReadSleep.this, MenuActivity.class);

            SharedPreferences data = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = data.edit();

            editor.putString(MainActivity.APP_PREFERENCES_BRACELET_SLEEP_CHECK, TryingConnectionActivity.SLEEP_GOOGLE_FIT_HOURS); //Сделали запись с информацией по сегодняшнему сну из гугл фита

            for(int i =0; i < 2; i++){
                sleepCount.startAnimation(mFadeInAnim);
                try {
                    TimeUnit.MILLISECONDS.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sleepCount.startAnimation(mFadeOutAnim);
            }
            BraceletReadSleep.this.startActivity(intent);
            return null;
        }
    }
}