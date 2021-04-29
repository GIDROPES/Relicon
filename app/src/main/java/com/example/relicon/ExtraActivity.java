package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class ExtraActivity extends AppCompatActivity implements View.OnClickListener {
    VideoView back_video; Button clear, toMirror;
    public static int now = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        clear = (Button) findViewById(R.id.clear); clear.setBackgroundResource(R.drawable.inset_ripped);
        clear.setOnClickListener(this);

        toMirror = (Button) findViewById(R.id.toMirror); toMirror.setBackgroundResource(R.drawable.inset_ripped);
        toMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (now == 0) {
                    ProectionMode.normalRotationMode = 1; now++;
                }
                else { ProectionMode.normalRotationMode = 0; now--; }
            }
        });
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
        if (v.getId() == R.id.clear){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            //Toast toast = Toast.makeText(this,"Перезагрузите приложение", Toast.LENGTH_LONG);
            //toast.show();
            Intent intent = new Intent(ExtraActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}