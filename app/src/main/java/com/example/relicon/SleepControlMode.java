package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.Task;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SleepControlMode extends AppCompatActivity {
    public static MediaPlayer mediaPlayer;
    VideoView back_video; TextView wakeup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_control_mode);

        back_video = (VideoView) findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.background2);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));
        SoundPlayingTask soundPlayingTask = new SoundPlayingTask();
        soundPlayingTask.execute();


    }

    class SoundPlayingTask extends AsyncTask<Void,Void,Void>{

        String wakeupPhrases[] = {"Ваши засыпаний под контролем", "Я позабочусь о вашей безопасности", "Будьте внимательны","Не спать"};
        Integer sounds[] = { R.raw.sound2, R.raw.sound3, R.raw.sound4, R.raw.sound5};
        int times[] = {22000, 30000, 15000, 17000, 25000, 10000};
        int exit = 0;
        Random random = new Random();
        int index = random.nextInt(wakeupPhrases.length);

        @Override
        protected Void doInBackground(Void... voids) {


            Animation mFadeInAnim, mFadeOutAnim;
            wakeup = (TextView) findViewById(R.id.wakeup);
            int exit = 0;
            mFadeInAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadein);
            mFadeOutAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadeout);

            wakeup.setText( wakeupPhrases[ index ] );

            while (true) {
                wakeup.startAnimation(mFadeInAnim);
                wakeup.startAnimation(mFadeOutAnim);
                try {
                    TimeUnit.MILLISECONDS.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Random randomS = new Random();
                Random randomT = new Random();
                mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[randomS.nextInt(sounds.length)]);
                mediaPlayer.start();
                try {
                    TimeUnit.MILLISECONDS.sleep((times[randomT.nextInt(times.length)]));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (exit == 1) break;
            }
            return null;
        }
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
}