package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    VideoView back_video;
    ImageButton proection_icon;
    Button extra;
    TextView sleepInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        sleepInfo = (TextView) findViewById(R.id.sleepInfo);
        
        SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
        String inf = sp.getString(MainActivity.APP_PREFERENCES_DEFAULT_SLEEP,"");

        if (sp.contains(MainActivity.APP_PREFERENCES_DEFAULT_SLEEP)){
            if (sp.getString(MainActivity.APP_PREFERENCES_TODAY_SLEEP,"").equals("Default")) {
                sleepInfo.setText("Вы спали ~ " + inf + " часов");
            }
            if (sp.getString(MainActivity.APP_PREFERENCES_TODAY_SLEEP,"").equals("Less")) {
                sleepInfo.setText("Вы спали меньше " + inf + " часов");
                sleepInfo.setTextColor(getResources().getColor(R.color.bad_red));
            }
            if (sp.getString(MainActivity.APP_PREFERENCES_TODAY_SLEEP,"").equals("More")) {
                sleepInfo.setText("Вы спали больше " + inf + " часов");
                sleepInfo.setTextColor(getResources().getColor(R.color.khaki));
            }
        }
        //sleepInfo.setText(inf);
        //sleepInfo.setText("Вы спали " + sp.getString(MainActivity.APP_PREFERENCES_TODAY_SLEEP, "0" + " часов"));

        /*proection_icon = (ImageButton) findViewById(R.id.proection_icon);
        Uri uriIcon1 = Uri.parse(path + getPackageName() + "/" + R.drawable.proection_icon);
        proection_icon.setImageURI(uriIcon1);
        */
        extra = (Button) findViewById(R.id.extra);
        extra.setBackgroundResource(R.drawable.inset_ripped); extra.setOnClickListener(this);
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
        if (v.getId() == R.id.extra){
            Intent intent = new Intent(this, ExtraActivity.class);
            startActivity(intent);
        }
    }
}