package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.request.SessionReadRequest;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BraceletReadSleep extends AppCompatActivity {
    String todaySleep;
    VideoView back_video; TextView sleepCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bracelet_read_sleep);

        back_video = findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();                                                               //видео на фоне
        back_video.setOnPreparedListener(mp -> mp.setLooping(true));
        //accessGoogleFit();
        SleepTextAnim sleepTextAnim = new SleepTextAnim(); //Анимацией показывает кол-во часов сна
        sleepTextAnim.execute();
    }

    class SleepTextAnim extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SharedPreferences data = getSharedPreferences(MainActivity.APP_PREFERENCES,MODE_PRIVATE);
            todaySleep = data.getString(MainActivity.APP_PREFERENCES_BRACELET_SLEEP_CHECK," ");
            Animation mFadeInAnim, mFadeOutAnim;
            mFadeInAnim = AnimationUtils.loadAnimation(BraceletReadSleep.this,R.anim.fadein);
            mFadeOutAnim = AnimationUtils.loadAnimation(BraceletReadSleep.this,R.anim.fadeout);

            sleepCount = (TextView) findViewById(R.id.sleepCount);
            sleepCount.setText("Вы спали " + todaySleep + " часов");      //Вытащил из другой активности информацию о часах сна
            Intent intent = new Intent(BraceletReadSleep.this, MenuActivity.class);



            for(int i =0; i < 2; i++){
                sleepCount.startAnimation(mFadeInAnim);
                try {                                                   //этот блок кода
                    TimeUnit.MILLISECONDS.sleep(1500);            //показывает надпись о том, сколько мы спали
                } catch (InterruptedException e) {                         //два раза
                    e.printStackTrace();
                }
                sleepCount.startAnimation(mFadeOutAnim);
            }
            BraceletReadSleep.this.startActivity(intent);                  //переход в меню
            return null;
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
        cal.add(Calendar.DAY_OF_WEEK, -1); //Изменено на 0. В случае неудачи вернуть на -1
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
        Fitness.getSessionsClient(this, GoogleSignIn.getLastSignedInAccount(this)).startSession(session).addOnFailureListener(e -> Log.i("TAG", "Session" + e.getMessage()));

        // Запрашиваем данные
        Fitness.getSessionsClient(this, GoogleSignIn.getLastSignedInAccount(this)).readSession(request).addOnSuccessListener(sessionReadResponse -> {
            for (Session ses : sessionReadResponse.getSessions()) {
                long sessionStart = ses.getStartTime(TimeUnit.MILLISECONDS);
                long sessionEnd = ses.getEndTime(TimeUnit.MILLISECONDS);
                //long sessionSleepTime = sessionEnd - sessionStart;
                long sessionSleepTime = sessionStart - sessionEnd;
                // переводим миллисекунды в часы
                // https://stackoverflow.com/questions/625433/how-to-convert-milliseconds-to-x-mins-x-seconds-in-java
                String hours = String.format("%d hour", TimeUnit.MILLISECONDS.toHours(sessionSleepTime));
                Log.i("TAG", hours);
                //textView.setText(hours);
                todaySleep = hours;
            }
        }).addOnFailureListener(e -> Log.i("FAILURE2", e.getMessage()));
    }

}