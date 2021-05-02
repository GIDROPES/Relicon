package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

public class ExtraActivity extends AppCompatActivity implements View.OnClickListener {
    VideoView back_video; Button clear, saveToMenu; CheckBox toMirror; Spinner soundsSpinner;
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

        saveToMenu = (Button) findViewById(R.id.saveToMenu);
        saveToMenu.setOnClickListener(this);

        toMirror = (CheckBox) findViewById(R.id.toMirror); // toMirror.setBackgroundResource(R.drawable.inset_ripped);
        SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if(sp.contains(MainActivity.APP_PREFERENCES_ROTATION_NORMAL)){
            if (sp.getString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"").equals("0"))
                toMirror.setChecked(true);
        }  //код для проверки чек бокса


        toMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (toMirror.isChecked()) {
                    editor.putString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"0");
                    editor.apply();
                }
                else { editor.putString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"1");
                    editor.apply(); }
            }
        });

        
        //блок кода для выпадающего списка
        soundsSpinner = (Spinner) findViewById(R.id.soundsSpinner);
            soundsSpinner.setBackgroundResource(R.drawable.inset_ripped);


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
        if (v.getId() == R.id.saveToMenu){
            Intent intent = new Intent(ExtraActivity.this, MenuActivity.class);

            startActivity(intent);
        }
    }
}