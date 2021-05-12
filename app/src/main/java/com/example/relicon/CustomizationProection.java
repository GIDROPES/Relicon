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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

public class CustomizationProection extends AppCompatActivity {

    VideoView back_video; Button saveToMenuCustom; CheckBox checkSpeedNotif;
    Spinner colorsSpiner;
    SharedPreferences sp;

    CheckBox checkTheme1, checkTheme2, checkTheme3, checkTheme4, checkTheme5;
    ImageView firstBack, secondBack, thirdBack, fourthBack;
    TextView fifthBack;
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


        checkTheme1 = findViewById(R.id.checkTheme1); checkTheme2 = findViewById(R.id.checkTheme2);
        checkTheme3 = findViewById(R.id.checkTheme3); checkTheme4 = findViewById(R.id.checkTheme4);
        checkTheme5 = findViewById(R.id.checkTheme5);

        firstBack = findViewById(R.id.firstBack); secondBack = findViewById(R.id.secondBack);
        thirdBack = findViewById(R.id.thirdBack); fourthBack = findViewById(R.id.fourhBack);
        fifthBack = findViewById(R.id.fifthBack); //это текст если что, так на заметку

        checkTheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
            }
        });
        firstBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme1.setChecked(true);
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
            }
        });

        checkTheme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme1.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
            }
        });
        secondBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme1.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
                checkTheme2.setChecked(true);
            }
        });

        checkTheme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme2.setChecked(false);
                checkTheme1.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
            }
        });
        thirdBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme2.setChecked(false);
                checkTheme1.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
                checkTheme3.setChecked(true);
            }
        });

        checkTheme4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme1.setChecked(false);
                checkTheme5.setChecked(false);
            }
        });
        fourthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme1.setChecked(false);
                checkTheme5.setChecked(false);
                checkTheme4.setChecked(true);
            }
        });

        checkTheme5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme1.setChecked(false);
            }
        });
        fifthBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme1.setChecked(false);
                checkTheme5.setChecked(true);
            }
        });
        CheckSpinnerColorAndTheme checkSpinnerColor = new CheckSpinnerColorAndTheme(); checkSpinnerColor.execute();
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

            if (checkTheme1.isChecked()){ editor.putString(MainActivity.APP_PREFERENCES_PROECTION_THEME,"Theme1"); editor.apply();}
            if (checkTheme2.isChecked()){ editor.putString(MainActivity.APP_PREFERENCES_PROECTION_THEME,"Theme2"); editor.apply();}
            if (checkTheme3.isChecked()){ editor.putString(MainActivity.APP_PREFERENCES_PROECTION_THEME,"Theme3"); editor.apply();}
            if (checkTheme4.isChecked()){ editor.putString(MainActivity.APP_PREFERENCES_PROECTION_THEME,"Theme4"); editor.apply();}
            if (checkTheme5.isChecked()){ editor.putString(MainActivity.APP_PREFERENCES_PROECTION_THEME,"Theme5"); editor.apply();}

            return null;
        }
    }
    class CheckSpinnerColorAndTheme extends AsyncTask<Void,Void,Void>{
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

            String theme = sp.getString(MainActivity.APP_PREFERENCES_PROECTION_THEME,"");
            if(theme.equals("Theme1")){
                checkTheme1.setChecked(true);
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
            }
            if(theme.equals("Theme2")){
                checkTheme1.setChecked(false);
                checkTheme2.setChecked(true);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
            }
            if(theme.equals("Theme3")){
                checkTheme1.setChecked(false);
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(true);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(false);
            }
            if(theme.equals("Theme4")){
                checkTheme1.setChecked(false);
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(true);
                checkTheme5.setChecked(false);
            }
            if(theme.equals("Theme5")){
                checkTheme1.setChecked(false);
                checkTheme2.setChecked(false);
                checkTheme3.setChecked(false);
                checkTheme4.setChecked(false);
                checkTheme5.setChecked(true);}
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