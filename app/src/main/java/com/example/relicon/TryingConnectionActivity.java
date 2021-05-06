package com.example.relicon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static java.text.DateFormat.getTimeInstance;
import android.annotation.SuppressLint;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionReadRequest;
import java.text.DateFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    public static GoogleSignInAccount account;
    private float sleepHours = 0;
    public static String SLEEP_GOOGLE_FIT_HOURS;


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
                .addDataType(DataType.AGGREGATE_ACTIVITY_SUMMARY, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_SLEEP_SEGMENT, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_ACTIVITY_SEGMENT, FitnessOptions.ACCESS_READ)
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
            SharedPreferences myData;
            myData = getSharedPreferences(MainActivity.APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = myData.edit();
            editor.putString(MainActivity.APP_PREFERENCES_HAS_BRACELET,"YES");
            editor.apply();

                Intent intent = new Intent(TryingConnectionActivity.this, MenuActivity.class);
                startActivity(intent);
            
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

        // Берем данные за прошедший день
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        //cal.add(Calendar.DAY_OF_WEEK, -1); //Изменено на 0. В случае неудачи вернуть на -1
        long startTime = cal.getTimeInMillis();

        // создаем подключение в виде сессии (не знаю чем отличается от простого запроса данных)
        SessionReadRequest request = new SessionReadRequest.Builder()
                .readSessionsFromAllApps()
                // By default, only activity sessions are included, so it is necessary to explicitly
                // request sleep sessions. This will cause activity sessions to be *excluded*.
                .includeSleepSessions()
                // Sleep segment data is required for details of the fine-granularity sleep, if it is present.
                .read(DataType.TYPE_SLEEP_SEGMENT)
                .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        // регистрируем сессию подключения для апи
        Session session = new Session.Builder()
                .setName("sessionName")
                .setIdentifier("identifier")
                .setDescription("description")
                .setStartTime(startTime, TimeUnit.MILLISECONDS)
                .build();

        // стартуем сессию
        Fitness.getSessionsClient(this, account).startSession(session).addOnFailureListener(e -> Log.i("TAG", "Session" + e.getMessage()));

        // Запрашиваем данные
        Fitness.getSessionsClient(this, account).readSession(request).addOnSuccessListener(sessionReadResponse -> {
            for (Session ses : sessionReadResponse.getSessions()) {
                long sessionStart = ses.getStartTime(TimeUnit.MILLISECONDS);
                long sessionEnd = ses.getEndTime(TimeUnit.MILLISECONDS);
                long sessionSleepTime = sessionEnd - sessionStart;
                // переводим миллисекунды в часы
                // https://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
                String hours = String.format("%d hour", TimeUnit.MILLISECONDS.toHours(sessionSleepTime));
                Log.i("TAG", hours);
                //textView.setText(hours);
                SLEEP_GOOGLE_FIT_HOURS = hours;
            }
        }).addOnFailureListener(e -> Log.i("TAG", e.getMessage()));
    }
}