/*
    Name:   AudioContent.java
    Author: Sean Smith
    Date:   28 December 2013

    This extracts all .mp3 files within the 'sdcard' folder on the Android device and extracts the song details.
*/

package com.project.paigepro;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

public class AudioContent {

	private String SDCARD_PATH = new String("/storage/emulated/0/sdcard/");
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	public AudioContent(){}

	public ArrayList<HashMap<String, String>> getPlayListContents() {

		File fileList = new File(SDCARD_PATH);

        	if (fileList.exists()) {

		    	if (fileList.listFiles(new AudioFileExtension()).length > 0) {

                		for (File file : fileList.listFiles(new AudioFileExtension())) {

		                    HashMap<String, String> songDetails = new HashMap<String, String>();
		                    songDetails.put("songTitle", file.getName());
		                    songDetails.put("songLocationPath", file.getPath());
		
		                    songsList.add(songDetails);
		                }
            		}
        	} else {

	            File sdCardFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "sdcard");
	            sdCardFolder.mkdirs();
	        }

		return songsList;
	}
	
	private class AudioFileExtension implements FilenameFilter {

	        public boolean accept(File directory, String name) {
	
	            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
		}
	}
}
