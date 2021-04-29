package com.example.relicon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProectionMode extends AppCompatActivity implements LocationListener {

    private LocationManager locationManager;
    private TextView speedValue, kmh;
    int roundedCurrentSpeed;
    public static int normalRotationMode = 0;
    private Button backToMenu;

    int countSound40 = 0; int countSound60 = 0; int countSound90 = 0; int countSount110 = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proection_mode);

        speedValue = (TextView) findViewById(R.id.speedValue);
        kmh = (TextView) findViewById(R.id.kmh);

        if (normalRotationMode == 0) {
            speedValue.setRotationY(-180f);
            kmh.setRotationY(-180f);
        }
        else {
            speedValue.setRotationY(0f);
            kmh.setRotationY(0f);
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
                startActivity(intent);
            }
        });
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        SoundNotif soundNot = new SoundNotif();

        speedValue = (TextView) findViewById(R.id.speedValue);

        if (location == null) {
            speedValue.setText("-");
        } else {
            float currentSpeed = location.getSpeed() * 3.6f + 5.0f;
            roundedCurrentSpeed = (int) currentSpeed;
            if (roundedCurrentSpeed < 9 ) {speedValue.setText(String.valueOf(0));}
                //speedValue.setText(String.format("%.2f", currentSpeed));
            else {speedValue.setText(String.valueOf(roundedCurrentSpeed)); soundNot.execute();}
        }
    }

    class SoundNotif extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            MediaPlayer mp;
            int exit = 0;

            while (true) {
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
                if (roundedCurrentSpeed >= 90 && roundedCurrentSpeed <= 109 && countSound90 == 0)
                {
                    mp = MediaPlayer.create(ProectionMode.this, R.raw.speed90);
                    mp.start();
                    countSound90 = 1;
                    countSount110 = 0;
                }
                if (roundedCurrentSpeed >= 110 && countSount110 == 0)
                {
                    mp = MediaPlayer.create(ProectionMode.this, R.raw.speed110);
                    mp.start();
                    countSount110 = 1;
                }

                if (exit == 1) break;
            }
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
        Toast.makeText(this,"Подключение может занять несколько минут", Toast.LENGTH_LONG).show();

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