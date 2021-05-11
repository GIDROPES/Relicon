package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.VideoView;

public class CustomizationProection extends AppCompatActivity {

    VideoView back_video; Button saveToMenuCustom; CheckBox checkSpeedNotif;
    Spinner colorsSpiner;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization_proection);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        checkSpeedNotif = findViewById(R.id.checkSpeedNotif);
        CheckAndSetChekBox chbs = new CheckAndSetChekBox(); chbs.execute();

        saveToMenuCustom = findViewById(R.id.saveToMenuCustom);
        saveToMenuCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveCurrentOptions scopt = new SaveCurrentOptions();
                scopt.execute();
                Intent intent = new Intent(CustomizationProection.this,MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        colorsSpiner = findViewById(R.id.colorsSpinner);
        colorsSpiner.setBackgroundResource(R.drawable.inset_ripped);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.proectionColors, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        colorsSpiner.setAdapter(adapter);
        CheckSpinnerColor checkSpinnerColor = new CheckSpinnerColor(); checkSpinnerColor.execute();
    }

    class CheckAndSetChekBox extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            String checker = sp.getString(MainActivity.APP_PREFERENCES_SPEED_NOTIFICATION,"");
            if (checker.equals("true")){checkSpeedNotif.setChecked(true);}
            if (checker.equals("false")){checkSpeedNotif.setChecked(false);}
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
    class SaveCurrentOptions extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if(checkSpeedNotif.isChecked()){
                editor.putString(MainActivity.APP_PREFERENCES_SPEED_NOTIFICATION,"true");
                editor.apply();
            }
            if(!checkSpeedNotif.isChecked()){
                editor.putString(MainActivity.APP_PREFERENCES_SPEED_NOTIFICATION,"false");
                editor.apply();
            }

            editor.putString(MainActivity.APP_PREFERENCES_COLOR_PREFERED, String.valueOf(colorsSpiner.getSelectedItem()));
            editor.apply();
            return null;
        }
    }
    class CheckSpinnerColor extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sp = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
            String check = sp.getString(MainActivity.APP_PREFERENCES_COLOR_PREFERED,"");

            if (check.equals("Белый")){colorsSpiner.setSelection(0);}
            if (check.equals("Оранжевый")){colorsSpiner.setSelection(1);}
            if (check.equals("Голубой")){colorsSpiner.setSelection(2);}
            if (check.equals("Красный")){colorsSpiner.setSelection(3);}
            if (check.equals("Зеленый")){colorsSpiner.setSelection(4);}

        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
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