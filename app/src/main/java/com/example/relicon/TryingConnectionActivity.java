package com.example.relicon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
public class TryingConnectionActivity extends AppCompatActivity {

    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    private GoogleSignInOptionsExtension fitnessOptions;
    private TextView textView;
    private GoogleSignInAccount account;
    private float sleepHours = 0;

    VideoView back_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trying_connection);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));

        ////////////////////////////////////////////////////////////////////////////

        fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
                .build();

        account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE, // e.g. 1
                    account,
                    fitnessOptions);
        } else {
            accessGoogleFit();
        }

       // if (Google)
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE)
                accessGoogleFit();
                //Intent intent = new Intent(TryingConnectionActivity.this, MenuActivity.class);
                //startActivity(intent);
            
        } else {
            Log.d("TAGgag", "FAIL");
            Log.d("TAGgag", String.valueOf(requestCode));
            Toast.makeText(this,"Не удалось подключиться",Toast.LENGTH_SHORT).show();
        }
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

    private void accessGoogleFit() {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        long stop = calendar.getTimeInMillis();
        calendar.add(Calendar.HOUR_OF_DAY, -24);

        long start = calendar.getTimeInMillis();
        Log.d("TAGgag", "In methods");
        DataReadRequest dataReadRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_SLEEP_SEGMENT)
                .bucketByActivityType(1, TimeUnit.DAYS)
                .setTimeRange(start, stop, TimeUnit.MILLISECONDS)
                .build();

        Fitness.getHistoryClient(this, account)
                .readData(dataReadRequest)
                .addOnSuccessListener(response -> {

                    long startH = response.getBuckets().get(0).getStartTime(TimeUnit.HOURS);
                    long endH = response.getBuckets().get(0).getEndTime(TimeUnit.HOURS);
                    sleepHours += (startH - endH);


                    textView.setText(String.valueOf(sleepHours));
                    Log.d("TAGgag", String.valueOf(response.getDataSet(DataType.TYPE_SLEEP_SEGMENT)));
                })
                .addOnFailureListener(e -> textView.setText("Таких данных нет"));
    }

}