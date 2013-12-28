/*
    Name:   AudioNew.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays the interactive audio player feature of the application.
*/

package com.project.paigepro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class AudioNew extends Activity implements SeekBar.OnSeekBarChangeListener, OnCompletionListener {

	private ImageButton buttonPlay, buttonRewind, buttonFastForward, buttonNext, buttonPrevious, buttonPlaylist, buttonRepeat, buttonShuffle;
    private TextView songTitle, currentDuration, totalDuration;
	private SeekBar playBar;
	private MediaPlayer mediaPlayer;
	private AudioContent audioContent;
	private AudioServices service;

    private Handler handler = new Handler();;

	private int fastForwardTime = 5000;
	private int rewindTime = 5000;
	private int songID = 0;

	private boolean shuffleActive = false;
	private boolean repeatActive = false;

	private ArrayList <HashMap<String, String>> songList = new ArrayList <HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.audionew);
		
		buttonPlay = (ImageButton) findViewById(R.id.ibPlay);
		buttonFastForward = (ImageButton) findViewById(R.id.ibFastForward);
		buttonRewind = (ImageButton) findViewById(R.id.ibRewind);
		buttonNext = (ImageButton) findViewById(R.id.ibNext);
		buttonPrevious = (ImageButton) findViewById(R.id.ibPrevious);
		buttonPlaylist = (ImageButton) findViewById(R.id.ibPlaylist);
		buttonRepeat = (ImageButton) findViewById(R.id.ibRepeat);
		buttonShuffle = (ImageButton) findViewById(R.id.ibShuffle);

        playBar = (SeekBar) findViewById(R.id.sbPlayBar);

        songTitle = (TextView) findViewById(R.id.tvSongTitle);
		currentDuration = (TextView) findViewById(R.id.tvCurrentDuration);
		totalDuration = (TextView) findViewById(R.id.tvTotalDuration);
		
		mediaPlayer = new MediaPlayer();
        audioContent = new AudioContent();
		service = new AudioServices();
		
		playBar.setOnSeekBarChangeListener(AudioNew.this);
		mediaPlayer.setOnCompletionListener(AudioNew.this);

		songList = audioContent.getPlayListContents();
		
        playSelectedSong();
        fastForwardSelectedSong();
        rewindSelectedSong();
        nextSelectedSong();
	    previousSelectedSong();
	    repeatSelectedSong();
        shuffleSelectedSong();
        playlistSelected();
	}

    private void playSelectedSong() {

        buttonPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Check if song is currently playing
                if (mediaPlayer.isPlaying() && mediaPlayer != null) {

                    mediaPlayer.pause();

                    // Display button play image
                    buttonPlay.setImageResource(R.drawable.audio_play);
                } else if (mediaPlayer != null) {

                    mediaPlayer.start();

                    // Display button pause image
                    buttonPlay.setImageResource(R.drawable.audio_pause);
                }
            }
        });
    }

    private void fastForwardSelectedSong() {

        buttonFastForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int currentSongPosition = mediaPlayer.getCurrentPosition();

                // Check if forward time is less than duration of song
                if (currentSongPosition + fastForwardTime <= mediaPlayer.getDuration()) {

                    mediaPlayer.seekTo(currentSongPosition + fastForwardTime);
                } else {

                    // Fast forward to end
                    mediaPlayer.seekTo(mediaPlayer.getDuration());
                }
            }
        });
    }

    private void rewindSelectedSong() {

        buttonRewind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                int currentSongPosition = mediaPlayer.getCurrentPosition();

                // Check if rewind time is greater than 0 seconds
                if (currentSongPosition - rewindTime >= 0) {

                    mediaPlayer.seekTo(currentSongPosition - rewindTime);
                } else {

                    // Rewind to start
                    mediaPlayer.seekTo(0);
                }
            }
        });
    }

    private void nextSelectedSong() {

        buttonNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Check if next song is available
                if (songID < (songList.size() - 1)) {

                    songID++;
                    playSelectedSong(songID);

                } else {

                    // Play first song in playlist
                    songID = 0;
                    playSelectedSong(songID);
                }
            }
        });
    }

    private void previousSelectedSong() {

        buttonPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Check if previous song is available
                if (songID > 0) {

                    songID--;
                    playSelectedSong(songID);
                }else{

                    // Play last song in playlist
                    songID = songList.size() - 1;
                    playSelectedSong(songID);
                }
            }
        });
    }

    private void repeatSelectedSong() {

        buttonRepeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (repeatActive) {

                    Toast.makeText(getApplicationContext(), "Repeat disabled", Toast.LENGTH_SHORT).show();
                    buttonRepeat.setImageResource(R.drawable.audio_repeat);

                    repeatActive = false;
                } else {

                    Toast.makeText(getApplicationContext(), "Repeat enabled", Toast.LENGTH_SHORT).show();
                    buttonRepeat.setImageResource(R.drawable.audio_repeat_enable);
                    buttonShuffle.setImageResource(R.drawable.audio_shuffle);

                    repeatActive = true;
                    shuffleActive = false;
                }
            }
        });
    }

    private void shuffleSelectedSong() {

        buttonShuffle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (shuffleActive) {

                    Toast.makeText(getApplicationContext(), "Shuffle disabled", Toast.LENGTH_SHORT).show();
                    buttonShuffle.setImageResource(R.drawable.audio_shuffle);

                    shuffleActive = false;
                } else {

                    Toast.makeText(getApplicationContext(), "Shuffle enabled", Toast.LENGTH_SHORT).show();
                    buttonShuffle.setImageResource(R.drawable.audio_shuffle_enable);
                    buttonRepeat.setImageResource(R.drawable.audio_repeat);

                    shuffleActive = true;
                    repeatActive = false;
                }
            }
        });
    }

    private void playlistSelected() {

        buttonPlaylist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent playlistIntent = new Intent(getApplicationContext(), AudioPlaylist.class);
                startActivityForResult(playlistIntent, 100);
            }
        });
    }

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {

            songID = data.getExtras().getInt("songID");
            playSelectedSong(songID);
        }
    }
	
	public void playSelectedSong(int songIndex) {

		try {
        	mediaPlayer.reset();
			mediaPlayer.setDataSource(songList.get(songIndex).get("songLocationPath"));
			mediaPlayer.prepare();
			mediaPlayer.start();

            // Display song title
			String title = songList.get(songIndex).get("songTitle");
        	songTitle.setText(title);

            // Display button pause image
            buttonPlay.setImageResource(R.drawable.audio_pause);
			
			// Display play bar
			playBar.setProgress(0);
			playBar.setMax(100);
			updatePlayBar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatePlayBar() {

        handler.postDelayed(updatePlayBarTime, 100);
    }	

	private Runnable updatePlayBarTime = new Runnable() {

        public void run() {

	       long totalDurationTime = mediaPlayer.getDuration();
           long currentDurationTime = mediaPlayer.getCurrentPosition();

           // Displaying total duration time
           totalDuration.setText(service.convertPlayTime(totalDurationTime));

           // Displaying current duration time
           currentDuration.setText(service.convertPlayTime(currentDurationTime));

           // Update play bar
           int progress = service.getCurrentPlayPercentage(currentDurationTime, totalDurationTime);

           playBar.setProgress(progress);

           handler.postDelayed(this, 100);
       }
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {

    }

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	    // Stop updating play bar
		handler.removeCallbacks(updatePlayBarTime);
    }
	
	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        handler.removeCallbacks(updatePlayBarTime);

        int totalDuration = mediaPlayer.getDuration();
		int currentPosition = service.durationToTimer(seekBar.getProgress(), totalDuration);
		mediaPlayer.seekTo(currentPosition);

        updatePlayBar();
    }

	@Override
	public void onCompletion(MediaPlayer arg0) {

		if (repeatActive) {

			// Repeat song if repeat enabled
			playSelectedSong(songID);
		} else if (shuffleActive) {

			// Select random song if shuffle enabled
			Random random = new Random();
			songID = random.nextInt((songList.size() - 1) + 1);
			playSelectedSong(songID);
		} else if (songID < (songList.size() - 1)) {

            // Play next song
            songID++;
			playSelectedSong(songID);
		} else {

            // Play first song
            songID = 0;
            playSelectedSong(songID);
        }
    }
	
	@Override
	public void onDestroy(){

        super.onDestroy();
	    mediaPlayer.release();
        handler.removeCallbacks(updatePlayBarTime);
	}
}