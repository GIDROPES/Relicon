package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

public class ExtraActivity extends AppCompatActivity implements View.OnClickListener {
    VideoView back_video; Button clear, saveToMenu; CheckBox toMirror; Spinner soundsSpinner;

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

        ChekBoxChekingTask cht = new ChekBoxChekingTask();
        cht.execute();

        toMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotationModeWritingTask rw = new RotationModeWritingTask();

                if (toMirror.isChecked()) {
                    rw.doInBackground(0);
                }
                else { rw.doInBackground(1); }
            }
        });


        //блок кода для выпадающего списка
        soundsSpinner = (Spinner) findViewById(R.id.soundsSpinner);
        soundsSpinner.setBackgroundResource(R.drawable.inset_ripped);

        SpinnerSetter spinnerSetter = new SpinnerSetter();
        spinnerSetter.execute();



    }
    //Задачи для отдельных потоков
    class SoundWritingTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

                    Integer pos = soundsSpinner.getSelectedItemPosition();
                    SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(MainActivity.APP_PREFERENCES_USABLE_SOUND, String.valueOf(pos));
                    //SoundWritingTask soundWritingTask = new SoundWritingTask();
                    //soundWritingTask.doInBackground(position);

            //Записываем позицию выбранного звука
            //для дальнейшего использования
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ExtraActivity.this,"Вы выбрали "+ soundsSpinner.getSelectedItem() , Toast.LENGTH_SHORT).show();

            super.onPostExecute(aVoid);
        }
    }
    class RotationModeWritingTask extends  AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... integers) {

            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            if (integers.equals(0)){
                editor.putString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"0");
                editor.apply();
            }
            if (integers.equals(1)){
                editor.putString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"1");
                editor.apply();
            }
            return null;
        }
    }
    class ClearEverything extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            return null;
        }
    }
    class ChekBoxChekingTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);


            if(sp.contains(MainActivity.APP_PREFERENCES_ROTATION_NORMAL)){
                if (sp.getString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"").equals("0"))
                    toMirror.setChecked(true);
            }  //код для проверки чек бокса

            return null;
        }
    }
    class SpinnerSetter extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);

            if (sp.contains(MainActivity.APP_PREFERENCES_USABLE_SOUND)) {

                switch (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND,"")) {
                    case "sound1" : soundsSpinner.setSelection(1); break;
                    case "sound2" : soundsSpinner.setSelection(2); break;
                    case "sound3" : soundsSpinner.setSelection(3); break;
                    case "sound4" : soundsSpinner.setSelection(4); break;
                }
            }
            return null;
        }
    }
    //-------------------------------------------------------

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.clear){
            ClearEverything ce = new ClearEverything();
            ce.execute();
            //Toast toast = Toast.makeText(this,"Перезагрузите приложение", Toast.LENGTH_LONG);
            //toast.show();
            Intent intent = new Intent(ExtraActivity.this, MainActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.saveToMenu){
            Intent intent = new Intent(ExtraActivity.this, MenuActivity.class);
            SoundWritingTask soundWritingTask = new SoundWritingTask();
            soundWritingTask.execute();
            startActivity(intent);
        }
    }
}