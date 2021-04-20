package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

public class MenuActivity extends AppCompatActivity {

    VideoView back_video;
    ImageButton proection_icon;
    Button extra;

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


        /*proection_icon = (ImageButton) findViewById(R.id.proection_icon);
        Uri uriIcon1 = Uri.parse(path + getPackageName() + "/" + R.drawable.proection_icon);
        proection_icon.setImageURI(uriIcon1);
        */
        extra = (Button) findViewById(R.id.extra);
        extra.setBackgroundResource(R.drawable.inset_ripped);
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