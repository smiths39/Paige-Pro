/*
    Name:   AudioPlaylist.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays all songs retrieved from the 'sdcard' folder and inserts them within an interactive listview.
*/

package com.project.paigepro;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class AudioPlaylist extends ListActivity {

    private String [] songTitle = { "songTitle" };
    private int [] songTitleID = { R.id.tvSongTitle };

	public ArrayList <HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
		setContentView(R.layout.audioplaylist);

		ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

		AudioContent audioContent = new AudioContent();

		// Retrieve all songs from sdcard
		this.songList = audioContent.getPlayListContents();

		for (int index = 0; index < songList.size(); index++) {

			HashMap<String, String> song = songList.get(index);
			songsListData.add(song);
		}

		ListAdapter adapter = new SimpleAdapter(this, songsListData, R.layout.audioplaylistsong, songTitle, songTitleID);
		setListAdapter(adapter);

		ListView listView = getListView();

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent songIntent = new Intent(getApplicationContext(), AudioNew.class);
                songIntent.putExtra("songID", position);
                setResult(100, songIntent);
                finish();
			}
		});
	}
}
