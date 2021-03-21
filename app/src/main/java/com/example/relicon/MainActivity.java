package com.example.relicon;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    VideoView back_video;
    ImageView relicon_h;
    Button next;

    Animation mFadeInAnim, mFadeOutAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        back_video = (VideoView) findViewById(R.id.back_video);
        String path = "android.resource://";
        Uri uri = Uri.parse(path + getPackageName() + "/" + R.raw.space_background);
        back_video.setVideoURI(uri);
        back_video.start();

        relicon_h = (ImageView) findViewById(R.id.relicon_h);

        next = (Button) findViewById(R.id.nextToBracletQuestion);
        next.setBackgroundResource(R.drawable.inset_ripped);
        //just comment
        back_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        mFadeInAnim = AnimationUtils.loadAnimation(this,R.anim.fadein);
        mFadeOutAnim = AnimationUtils.loadAnimation(this,R.anim.fadeout);

        relicon_h.startAnimation(mFadeInAnim);


        next.startAnimation(mFadeInAnim);
        next.startAnimation(mFadeOutAnim);
        next.startAnimation(mFadeInAnim);

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
}