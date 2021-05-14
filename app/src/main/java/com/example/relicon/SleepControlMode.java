package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.Task;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class SleepControlMode extends AppCompatActivity {

    String wakeupPhrases[] = {"Ваши засыпаний под контролем", "Я позабочусь о вашей безопасности", "Будьте внимательны"
            ,"Не спать","Сосредоточьтесь на дороге","Осторожнее на дорогах"};
    Integer sounds[] = { R.raw.sound2, R.raw.sound3, R.raw.sound4, R.raw.sound1};
    Integer results[] = {2,1,78,14,6,7};
    //int times[] = {22000, 30000, 15000, 17000, 25000, 10000};

    Random random = new Random();
    int index = random.nextInt(wakeupPhrases.length);


    Animation mFadeInAnim, mFadeOutAnim;
    public static MediaPlayer mediaPlayer;
    VideoView back_video; TextView wakeup;  Button backToMenuSleep;
    public static int exit = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_control_mode);
        exit = 0;
        SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);

        back_video = (VideoView) findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.background2);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        backToMenuSleep = findViewById(R.id.backToMenuSleep);


        //SoundPlayingTask soundPlayingTask = new SoundPlayingTask();
        //soundPlayingTask.execute();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                wakeup = (TextView) findViewById(R.id.wakeup);
                wakeup.setText( wakeupPhrases[ index ] );

                mFadeInAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadein);
                mFadeOutAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadeout);

                try {
                    TimeUnit.MILLISECONDS.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wakeup.startAnimation(mFadeInAnim);


                Random randomT = new Random();
                int result = results[randomT.nextInt(results.length)];
                if(result == 1) {
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("0")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[0]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("1")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[1]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("2")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[2]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("3")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[3]);
                        mediaPlayer.start();
                    }
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wakeup.startAnimation(mFadeOutAnim);
            }
        };

        Timer timer = new Timer();
        timer.schedule(timerTask,0,2600);

        backToMenuSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SleepControlMode.this, MenuActivity.class);
                exit = 1;
                timer.cancel();
                startActivity(intent);
                finish();
            }
        });
    }

    class SoundPlayingTask extends AsyncTask<Void,Void,Void>{

        String wakeupPhrases[] = {"Ваши засыпаний под контролем", "Я позабочусь о вашей безопасности", "Будьте внимательны"
                ,"Не спать","Сосредоточьтесь на дороге","Осторожнее на дорогах"};
        Integer sounds[] = { R.raw.sound2, R.raw.sound3, R.raw.sound4, R.raw.sound1};
        Integer results[] = {2,1,78,14,6,7};
        //int times[] = {22000, 30000, 15000, 17000, 25000, 10000};

        Random random = new Random();
        int index = random.nextInt(wakeupPhrases.length);



        @Override
        protected void onPreExecute() {
            wakeup = (TextView) findViewById(R.id.wakeup);
            wakeup.setText( wakeupPhrases[ index ] );
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);


           // wakeup = (TextView) findViewById(R.id.wakeup);

            //mFadeInAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadein);
            //mFadeOutAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadeout);

            //wakeup.setText( wakeupPhrases[ index ] );

            while (true) {
                //wakeup.startAnimation(mFadeInAnim);
                //wakeup.startAnimation(mFadeOutAnim);
                mFadeInAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadein);
                mFadeOutAnim = AnimationUtils.loadAnimation(SleepControlMode.this,R.anim.fadeout);

                try {
                    TimeUnit.MILLISECONDS.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wakeup.startAnimation(mFadeInAnim);


                Random randomT = new Random();
                int result = results[randomT.nextInt(results.length)];
                if(result == 1) {
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("0")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[0]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("1")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[1]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("2")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[2]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("3")) {
                        mediaPlayer = MediaPlayer.create(SleepControlMode.this, sounds[3]);
                        mediaPlayer.start();
                    }
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wakeup.startAnimation(mFadeOutAnim);

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