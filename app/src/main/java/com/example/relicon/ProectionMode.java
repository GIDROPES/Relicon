package com.example.relicon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ProectionMode extends AppCompatActivity implements LocationListener {
    public static int exit = 0;
    Animation mFadeInAnim, mFadeOutAnim;
    private LocationManager locationManager; public static MediaPlayer mediaPlayer;
    TextView speedValue, kmh, wakeup;
    int roundedCurrentSpeed;
    public static int normalRotationMode = 0;
    private Button backToMenu;
    String wakeupPhrases[] = {"Ваши засыпаний под контролем", "Я позабочусь о вашей безопасности", "Будьте внимательны"
            ,"Не спать","Сосредоточьтесь на дороге","Осторожнее на дорогах"};
   // public static int MULTI_MODE;
    int countSound40 = 0; int countSound60 = 0; int countSound90 = 0; int countSount110 = 0;
    String checker;
    SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES, MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proection_mode);

        speedValue = (TextView) findViewById(R.id.speedValue);
        kmh = (TextView) findViewById(R.id.kmh);


        //SharedPreferences.Editor editor = sp.edit();

        if (sp.getString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"").equals("0")) {
            speedValue.setRotationY(-180f);
            kmh.setRotationY(-180f);
        }
        if (sp.getString(MainActivity.APP_PREFERENCES_ROTATION_NORMAL,"").equals("1"))
        {
            speedValue.setRotationY(0);
            kmh.setRotationY(0);
        }

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {

            //start the program if permission is granted
            doStuff();

        }


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        this.onLocationChanged(null);

        speedValue = (TextView) findViewById(R.id.speedValue);

        backToMenu = (Button) findViewById(R.id.backToMenu);

        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProectionMode.this, MenuActivity.class);
                //exit = 1;
                WriteMultiModeFalse wtf = new WriteMultiModeFalse();
                wtf.execute();
                startActivity(intent);
                finish();
            }
        });

        Random random = new Random();
        int index = random.nextInt(wakeupPhrases.length);
        wakeup = (TextView) findViewById(R.id.wakeup2);
        wakeup.setText( wakeupPhrases[ index ] );

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        SoundNotif soundNot = new SoundNotif();

        if (sp.getString(MainActivity.APP_PREFERENCES_MULTI_MODE,"").equals("true")) {
            wakeup = findViewById(R.id.wakeup2);
            SoundPlayingTask soundPlayingTask = new SoundPlayingTask();
            soundPlayingTask.execute();
        }

        speedValue = (TextView) findViewById(R.id.speedValue);

        if (location == null) {
            speedValue.setText("-");
        } else {
            float currentSpeed = location.getSpeed() * 3.6f + 5.0f;
            roundedCurrentSpeed = (int) currentSpeed;
            if (roundedCurrentSpeed < 9 ) {speedValue.setText(String.valueOf(0));}
                //speedValue.setText(String.format("%.2f", currentSpeed));
            else {
                CheckSoundNotif chsk = new CheckSoundNotif();
                chsk.execute();
                speedValue.setText(String.valueOf(roundedCurrentSpeed));
            if(checker.equals("true")) {
                soundNot.execute();
                }
            }
        }
    }

    class SoundPlayingTask extends AsyncTask<Void,Void,Void>{


        Integer sounds[] = { R.raw.sound2, R.raw.sound3, R.raw.sound4, R.raw.sound1};
        Integer results[] = {2,1,78,14,6,7};
        //int times[] = {22000, 30000, 15000, 17000, 25000, 10000};





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);

                mFadeInAnim = AnimationUtils.loadAnimation(ProectionMode.this,R.anim.fadein);
                mFadeOutAnim = AnimationUtils.loadAnimation(ProectionMode.this,R.anim.fadeout);

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
                        mediaPlayer = MediaPlayer.create(ProectionMode.this, sounds[0]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("1")) {
                        mediaPlayer = MediaPlayer.create(ProectionMode.this, sounds[1]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("2")) {
                        mediaPlayer = MediaPlayer.create(ProectionMode.this, sounds[2]);
                        mediaPlayer.start();
                    }
                    if (sp.getString(MainActivity.APP_PREFERENCES_USABLE_SOUND, "").equals("3")) {
                        mediaPlayer = MediaPlayer.create(ProectionMode.this, sounds[3]);
                        mediaPlayer.start();
                    }
                }

                try {
                    TimeUnit.MILLISECONDS.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wakeup.startAnimation(mFadeOutAnim);


            return null;
        }
    }
    class CheckSoundNotif extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            checker = sp.getString(MainActivity.APP_PREFERENCES_SPEED_NOTIFICATION,"");
            return null;
        }
    }
    class SoundNotif extends AsyncTask<Void,Void,Void>{
        SharedPreferences sp;
        @Override
        protected Void doInBackground(Void... voids) {
            MediaPlayer mp;
            //int exit = 0;
            sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            if(sp.getString(MainActivity.APP_PREFERENCES_SPEED_NOTIFICATION,"").equals("true")) {
                if (roundedCurrentSpeed <= 39) {
                    countSound40 = 0;
                    countSound60 = 0;
                    countSound90 = 0;
                    countSount110 = 0;
                }

                if (roundedCurrentSpeed >= 40 && roundedCurrentSpeed <= 59 && countSound40 == 0) {
                    mp = MediaPlayer.create(ProectionMode.this, R.raw.speed40);
                    mp.start();
                    countSound40 = 1;
                    countSound60 = 0;
                    countSound90 = 0;
                    countSount110 = 0;
                }
                if (roundedCurrentSpeed >= 60 && roundedCurrentSpeed <= 89 && countSound60 == 0) {
                    mp = MediaPlayer.create(ProectionMode.this, R.raw.speed60);
                    mp.start();
                    countSound60 = 1;
                    countSound90 = 0;
                    countSount110 = 0;
                }

                ////////блок обновлений////////

                if (roundedCurrentSpeed < 58 && roundedCurrentSpeed > 54) {
                    countSound60 = 0;
                }
                if (roundedCurrentSpeed < 88 && roundedCurrentSpeed > 84) {
                    countSound90 = 0;
                }
                if (roundedCurrentSpeed < 38 && roundedCurrentSpeed > 34) {
                    countSound40 = 0;
                }
                if (roundedCurrentSpeed < 108 && roundedCurrentSpeed > 104) {
                    countSount110 = 0;
                }

                //////////////////////////////

                if (roundedCurrentSpeed >= 90 && roundedCurrentSpeed <= 109 && countSound90 == 0) {
                    mp = MediaPlayer.create(ProectionMode.this, R.raw.speed90);
                    mp.start();
                    countSound90 = 1;
                    countSount110 = 0;
                }
                if (roundedCurrentSpeed >= 110 && countSount110 == 0) {
                    mp = MediaPlayer.create(ProectionMode.this, R.raw.speed110);
                    mp.start();
                    countSount110 = 1;
                }
            }
            return null;
        }
    }
    class WriteMultiModeFalse extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences sp = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(MainActivity.APP_PREFERENCES_MULTI_MODE);
            editor.apply();
            editor.putString(MainActivity.APP_PREFERENCES_MULTI_MODE,"false");
            editor.apply();
            return null;
        }
    }
    
    private void doStuff() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (lm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            //commented, this is from the old version
            
        }
        Toast.makeText(this,"Подключение может занять некоторое время", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff();
            } else {

                finish();
            }

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}