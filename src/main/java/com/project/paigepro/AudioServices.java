/*
    Name:   AudioServices.java
    Author: Sean Smith
    Date:   28 December 2013

    The selected song's running time is converted into a format to be used for the display of the songs progress timer during playtime.
*/

package com.project.paigepro;

public class AudioServices {

	// Convert milliseconds time to Hours:Minutes:Seconds
	public String convertPlayTime(long milliseconds){

        String playTimer = "";
		String secondsTime = "";

        int minutes = (int)(milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int)((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

       	// Add 0 to start of seconds if only one digit
		if (seconds < 10) {
	        secondsTime = "0" + seconds;
		} else {
			secondsTime = "" + seconds;
        }

        playTimer += minutes + ":" + secondsTime;

		return playTimer;
	}
	
	public int getCurrentPlayPercentage(long currentTimeDuration, long totalTimeDuration){
		
		long currentTimeSeconds = (int) (currentTimeDuration / 1000);
		long totalTimeSeconds = (int) (totalTimeDuration / 1000);
		
		// Calculate percentage
		Double percentage = (((double)currentTimeSeconds) / totalTimeSeconds) * 100;

		return percentage.intValue();
	}

	// Convert to song duration to timer
	public int durationToTimer(int currentTime, int totalDuration) {

        totalDuration /= 1000;
		int currentDuration = (int) ((((double) currentTime) / 100) * totalDuration);
		
		// Return duration in milliseconds
		return currentDuration * 1000;
	}
}
