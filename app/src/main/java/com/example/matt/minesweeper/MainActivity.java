/**
 * MainActivity
 *
 * @author Matt Doyle (mwdoyle), Emma Hughes (eha38)
 */

package com.example.matt.minesweeper;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    AnimationDrawable animationDrawable;
    ConstraintLayout myLayout;
    private CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        setupBackground();
        playVideo();
        countDownTimer();
        setupSkipButton();
        setupText();
    }

    // Background animation
    // Source: https://www.youtube.com/watch?v=-8QrtXkfF9A
    private void setupBackground() {
        // Declare animation and constraint layout
        myLayout = findViewById(R.id.myLayout);
        animationDrawable = (AnimationDrawable) myLayout.getBackground();

        // Add time changes
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();
    }

    // Play video
    // Source https://developer.android.com/reference/android/widget/VideoView.html
    // Video Converter https://video.online-convert.com/convert-to-3gp
    // Created at panzoid.com
    private void playVideo() {
        final VideoView videoView = findViewById(R.id.videoView);
        videoView.getHolder().setSizeFromLayout();
        videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video));
        videoView.setMediaController(new MediaController(this));
        videoView.requestFocus();
        videoView.start();
    }

    // Countdown timer for launching the main menu
    // Source https://developer.android.com/reference/android/os/CountDownTimer.html
    private void countDownTimer() {
        timer = new CountDownTimer(7000, 1000) {
            TextView textView = (TextView) findViewById(R.id.textView8);

            public void onTick(long millisUntilFinished) {
                textView.setText("Game Starts in " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Intent intent = MainMenuActivity.makeIntent(MainActivity.this);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    // Prevents timer from running if app is closed
    // Source https://stackoverflow.com/questions/27333742/how-do-i-prevent-my-countdowntimer-from-running-in-background
    @Override
    public void onPause() {
        super.onPause();
        timer.cancel(); // Timer is a reference to my inner CountDownTimer class
        timer = null;
    }

    // Skips the opening video/animations
    private void setupSkipButton() {
        Button btn = findViewById(R.id.btnSkip);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainMenuActivity.makeIntent(MainActivity.this);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupText() {
        Typeface myTypeface = Typeface.createFromAsset(getAssets(), "sign_painter_text.ttf");

        TextView textView = findViewById(R.id.textView);
        textView.setTypeface(myTypeface);
        Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_left);
        animation.setDuration(3000);
        textView.startAnimation(animation);

        TextView textView2 = findViewById(R.id.textView2);
        textView2.setTypeface(myTypeface);
    }
}