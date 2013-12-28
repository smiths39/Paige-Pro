/*
    Name:   Splash.java
    Author: Sean Smith
    Date:   28 December 2013

    This page is displayed when the application is launched.
*/

package com.project.paigepro;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Splash extends Activity {

    MediaPlayer introSong;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        introSong = MediaPlayer.create(Splash.this, R.raw.paige_intro);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean music = getPrefs.getBoolean("checkbox", true);

        if (music == true) {

            introSong.start();
        }

        Thread timer = new Thread() {

            public void run() {

                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent openStartingPoint = new Intent("com.project.paigepro.MENU");
                    startActivity(openStartingPoint);
                }
            }
        };

        timer.start();
        StartAnimations();
    }

    public void onAttachedToWindow() {

        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private void StartAnimations() {

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        animation.reset();

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayoutSplash);
        layout.clearAnimation();
        layout.startAnimation(animation);

        animation = AnimationUtils.loadAnimation(this, R.anim.translate);
        animation.reset();

        ImageView image = (ImageView) findViewById(R.id.ImageLogo);
        image.clearAnimation();
        image.startAnimation(animation);
    }

    @Override
    protected void onPause() {

        super.onPause();
        introSong.release();
        finish();
    }
}