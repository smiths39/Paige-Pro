/*
    Name:   VideoPlayer.java
    Author: Sean Smith
    Date:   28 December 2013

    This activates the device's video player which plays the selected video within the listview.
*/

package com.project.paigepro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayer extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent videoIntent = getIntent();
        Bundle extras = videoIntent.getExtras();

        String videoTitle = extras.getString("videotitle");
        initialiseVideo(videoTitle);
    }

    private void initialiseVideo(String newVideoTitle) {

        VideoView videoView = new VideoView(getApplicationContext());
        setContentView(videoView);

        videoView.setVideoPath(newVideoTitle);
        videoView.setMediaController(new MediaController(VideoPlayer.this));
        videoView.requestFocus();
        videoView.start();
    }
}
