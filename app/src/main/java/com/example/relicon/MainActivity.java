package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
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

    public static final String APP_PREFERENCES = "sleeperdata";              //название файла
    public static final String APP_PREFERENCES_DEFAULT_SLEEP = "DefaultSleepCount";   //значение сна по умолчанию
    public static final String APP_PREFERENCES_HAS_BRACELET = "HasBracelet";        //наличие браслета
    public static final String APP_PREFERENCES_TODAY_SLEEP = "SleepTimeToday";     //сон в конкретно сегодняшний день
    public static final String APP_PREFERENCES_BRACELET_SLEEP_CHECK = "BraceleteSleepTimeToday";     //сон в конкретно сегодняшний день по браслету
    public static final String APP_PREFERENCES_ROTATION_NORMAL = "RotationSpeedCounter";     //отзеркаливание скорости
    public static final String APP_PREFERENCES_USABLE_SOUND = "Sound";     //хранит выбранный звук для режима сна и смешанного режима
    public static final String APP_PREFERENCES_COLOR_PREFERED = "ColorSpeed";     //цвет проекции
    public static final String APP_PREFERENCES_PROECTION_THEME = "ProectionTheme";     //фон для проекции
    public static final String APP_PREFERENCES_SPEED_NOTIFICATION = "ProectionSounds";     //Содержит информацию о наличии оповещений об ограничениях скорости
    public static final String APP_PREFERENCES_MULTI_MODE = "MultiModeInfo";     //Содержит информацию о режиме работы

    SharedPreferences myData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


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

        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        mFadeInAnim = AnimationUtils.loadAnimation(this,R.anim.fadein);
        mFadeOutAnim = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        comboAnim = AnimationUtils.loadAnimation(this,R.anim.comboanim);
        relicon_h.startAnimation(mFadeInAnim);

        yes_miband5.setBackgroundResource(R.drawable.inset_ripped);
        no_miband5.setBackgroundResource(R.drawable.inset_ripped);


        next.startAnimation(mFadeInAnim);
        next.setOnClickListener(this);
        /*next.setOnClickListener(v -> {
            if(!myData.contains(APP_PREFERENCES_HAS_BRACELET)) {
                reliconAnimatorStart();
                miband5.setVisibility(View.VISIBLE);
                miband5.startAnimation(comboAnim);
                next.startAnimation(mFadeOutAnim);
                next.setVisibility(View.INVISIBLE);
                info_miband5.setVisibility(View.VISIBLE);
                info_miband5.startAnimation(comboAnim);
                yes_miband5.setVisibility(View.VISIBLE);
                no_miband5.setVisibility(View.VISIBLE);
                yes_miband5.startAnimation(mFadeInAnim);
                no_miband5.startAnimation(mFadeInAnim);

                yes_miband5.setClickable(true);
                no_miband5.setClickable(true);

                yes_miband5.setOnClickListener(this);
                no_miband5.setOnClickListener(this);
            }
            else {
                Intent intent = new Intent(this, TodaySleepHours.class);
                startActivity(intent);
            }
        });
            */

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

    class WriteFalseMultiModeMain extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            myData = getSharedPreferences(APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = myData.edit();
            editor.putString(APP_PREFERENCES_MULTI_MODE,"false");
            editor.apply();
            return null;
        }
    }
    class noBracelet extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            myData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = myData.edit();
            editor2.putString(APP_PREFERENCES_HAS_BRACELET,"NO");
            editor2.apply();

            return null;
        }
    }

    class setRotation extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            myData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myData.edit();
            editor.putString(APP_PREFERENCES_ROTATION_NORMAL,"0");
            editor.apply();
            return null;
        }
    }

    class yesBracelet extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            myData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor2 = myData.edit();
            editor2.putString(APP_PREFERENCES_HAS_BRACELET,"YES");
            editor2.apply();
            return null;
        }
    }

    class SoundWritingFirst extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            myData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myData.edit();
            editor.putString(APP_PREFERENCES_USABLE_SOUND,"0");
            editor.apply();
            editor.putString(APP_PREFERENCES_SPEED_NOTIFICATION,"true");
            editor.apply();
            editor.putString(APP_PREFERENCES_MULTI_MODE,"true");
            editor.apply();
            editor.putString(APP_PREFERENCES_COLOR_PREFERED,"Оранжевый");
            editor.apply();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        myData = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myData.edit();
        String info = myData.getString(APP_PREFERENCES_HAS_BRACELET,"");
        if (v.getId() == R.id.nextToBracletQuestion){
            if(!myData.contains(APP_PREFERENCES_HAS_BRACELET)) {
                reliconAnimatorStart();
                miband5.setVisibility(View.VISIBLE);
                miband5.startAnimation(comboAnim);
                next.startAnimation(mFadeOutAnim);
                next.setVisibility(View.INVISIBLE);
                info_miband5.setVisibility(View.VISIBLE);
                info_miband5.startAnimation(comboAnim);
                yes_miband5.setVisibility(View.VISIBLE);
                no_miband5.setVisibility(View.VISIBLE);
                yes_miband5.startAnimation(mFadeInAnim);
                no_miband5.startAnimation(mFadeInAnim);
                setRotation setRotation = new setRotation();
                setRotation.execute();
                WriteFalseMultiModeMain write = new WriteFalseMultiModeMain();
                write.execute();
                yes_miband5.setClickable(true);
                no_miband5.setClickable(true);

                yes_miband5.setOnClickListener(this);
                no_miband5.setOnClickListener(this);
            }
            else {
                if(myData.getString(APP_PREFERENCES_HAS_BRACELET,"").equals("YES")){
                    Intent intent = new Intent(this, TryingConnectionActivity.class);
                    //setRotation sr = new setRotation();
                    //sr.execute();
                    startActivity(intent);
                }
                if(myData.getString(APP_PREFERENCES_HAS_BRACELET,"").equals("NO")) {
                    Intent intent = new Intent(this, TodaySleepHours.class);
                    //noBracelet noBracelet = new noBracelet();
                    //noBracelet.execute();
                    startActivity(intent);
                }
            }
        }

        if (v.getId() == R.id.yes_miband5){
            Intent intent = new Intent(this, TryingConnectionActivity.class);
            yesBracelet yb = new yesBracelet();
            yb.execute();
            SoundWritingFirst soundWritingFirst = new SoundWritingFirst();
            soundWritingFirst.execute();
            startActivity(intent);
        }
        if (v.getId() == R.id.no_miband5){
            Intent intent2 = new Intent(this, SleepCountChanger.class);
            noBracelet noBracelet = new noBracelet();
            noBracelet.execute();
            SoundWritingFirst soundWritingFirst = new SoundWritingFirst();
            soundWritingFirst.execute();
            startActivity(intent2);
        }
    }
}