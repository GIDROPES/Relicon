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
    VideoView back_video; Button clear, saveToMenu,toCustomProection; CheckBox toMirror; Spinner soundsSpinner;

    static String myPosition;


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

        toMirror = (CheckBox) findViewById(R.id.toMirror);

        toCustomProection = findViewById(R.id.toCustomization);
        toCustomProection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExtraActivity.this,CustomizationProection.class);
                startActivity(intent);
                finish();
            }
        });

        ChekBoxChekingTask cht = new ChekBoxChekingTask(); //делает чек бокс отмеченным или пустым в зависимости от прошлого выбора
        cht.execute();                                      //результат выбора сохраняется по кнопке "Сохранить"

        toMirror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //блок кода для выпадающего списка
        soundsSpinner = (Spinner) findViewById(R.id.soundsSpinner);
        soundsSpinner.setBackgroundResource(R.drawable.inset_ripped);

        ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this, R.array.sounds, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        soundsSpinner.setAdapter(adapter);

        SpinnerSetter spinnerSetter = new SpinnerSetter();
        spinnerSetter.execute();
        //soundsSpinner.setSelection(Integer.parseInt(myPosition)); //ВЫЗЫВАЕТ ОШИБКУ, ИСПРАВИТЬ


     /*   soundsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      */
    }
    //Задачи для отдельных потоков
    class SoundWritingTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

                    SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString(MainActivity.APP_PREFERENCES_USABLE_SOUND, String.valueOf(soundsSpinner.getSelectedItemPosition()));
                    editor.apply();
            //Записываем позицию выбранного звука
            //для дальнейшего использования
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(ExtraActivity.this,"Вы выбрали звук "+ soundsSpinner.getSelectedItemPosition() , Toast.LENGTH_SHORT).show();

            super.onPostExecute(aVoid);
        }
    }
    class RotationModeWritingTask1 extends  AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();


                editor.putString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"1"); //Кладем 1, чек бокс не отмечен, скорость в привычном виде
                editor.apply();

            return null;
        }
    }
    class RotationModeWritingTask0 extends  AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();


                editor.putString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"0");  //Кладем значение 0, галочка отмечена, скорость отзеркалена
                editor.apply();

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
                else if (sp.getString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"").equals("1")){
                    toMirror.setChecked(false);
                }
            }  //код для проверки чек бокса

            return null;
        }
    }
    class SpinnerSetter extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            String pos = "";
            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
            if(sp.contains(MainActivity.APP_PREFERENCES_USABLE_SOUND))
            pos = sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "");

            myPosition = pos;
            soundsSpinner.setSelection(Integer.parseInt(pos));
        }
    }
    //-------------------------------------------------------

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

        if (v.getId() == R.id.clear){
            ClearEverything ce = new ClearEverything();
            ce.execute();
            //Toast toast = Toast.makeText(this,"Перезагрузите приложение", Toast.LENGTH_LONG);
            //toast.show();
            Intent intent = new Intent(ExtraActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (v.getId() == R.id.saveToMenu){
            Intent intent = new Intent(ExtraActivity.this, MenuActivity.class);
            SoundWritingTask soundWritingTask = new SoundWritingTask();
            soundWritingTask.execute();
            RotationModeWritingTask1 rw1 = new RotationModeWritingTask1();
            RotationModeWritingTask0 rw0 = new RotationModeWritingTask0();

            if (toMirror.isChecked()) {
                rw0.execute();
            }
            else { rw1.execute(); }

            startActivity(intent);
            finish();
        }
    }
}