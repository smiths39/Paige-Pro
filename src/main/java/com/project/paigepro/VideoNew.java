/*
    Name:   VideoNew.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays the interactive video feature of the application.
*/

package com.project.paigepro;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class VideoNew extends Activity implements View.OnClickListener {

    private ListView videoList;
    private Button buttonAddVideo;
    private Cursor cursor;
    private int videoID;
    private String videoPath;
    private String [] videoThumbnails = { MediaStore.Video.Thumbnails.DATA, MediaStore.Video.Thumbnails.VIDEO_ID };
    private String [] videoDetails = { MediaStore.Video.Media._ID, MediaStore.Video.Media.DATA, MediaStore.Video.Media.DISPLAY_NAME, MediaStore.Video.Media.SIZE };

    private static final int CAMERA_REQUEST = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.videonew);
        initialise();

        buttonAddVideo = (Button) findViewById(R.id.bAddVideo);
        buttonAddVideo.setOnClickListener(VideoNew.this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bAddVideo:
                initialiseCamera();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case CAMERA_REQUEST:
                    Intent cameraIntent = getIntent();
                    startActivity(cameraIntent);
                    finish();

                    break;
            }
        }
    }

    public void initialiseCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("outputX", 200);
        cameraIntent.putExtra("outputY", 150);

        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void initialise() {

        cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, videoDetails, null, null, null);
        videoList = (ListView) findViewById(R.id.lvVideo);

        videoList.setAdapter(new VideoAdapter(this.getApplicationContext()));
        videoList.setOnItemClickListener(videoPlay);
    }

    private AdapterView.OnItemClickListener videoPlay = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView adapterView, View view, int position, long l) {

            videoID = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToPosition(position);
            String videoTitle = cursor.getString(videoID);

            Intent videoPlayIntent = new Intent(VideoNew.this, VideoPlayer.class);
            videoPlayIntent.putExtra("videotitle", videoTitle);
            startActivity(videoPlayIntent);
        }
    };

    public class VideoAdapter extends BaseAdapter {

        private Context context;
        private TextView videoTitle, videoSize;
        private ImageView videoSnapshot;

        public VideoAdapter(Context newContext) {

            context = newContext;
        }

        public int getCount() {

            return cursor.getCount();
        }

        public Object getItem(int index) {

            return index;
        }

        public long getItemId(int index) {

            return index;
        }

        public View getView(int index, View view, ViewGroup viewGroup) {

            View viewItem = LayoutInflater.from(context).inflate(R.layout.videolist, viewGroup, false);

            videoSnapshot = (ImageView) viewItem.findViewById(R.id.ivVideoSnapshot);
            videoTitle = (TextView) viewItem.findViewById(R.id.tvVideoTitle);
            videoSize = (TextView) viewItem.findViewById(R.id.tvVideoSize);

            videoID = cursor.getColumnIndexOrThrow(videoDetails[2]);
            cursor.moveToPosition(index);
            videoTitle.setText(cursor.getString(videoID));

            videoID = cursor.getColumnIndexOrThrow(videoDetails[3]);
            cursor.moveToPosition(index);
            videoSize.setText("Size(KB): " + cursor.getString(videoID));

            int videoClipID = cursor.getInt(cursor.getColumnIndexOrThrow(videoDetails[0]));
            Cursor videoCursor = getContentResolver().query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI, videoThumbnails, videoThumbnails[1] + "=" + videoClipID, null, null);

            if (videoCursor.moveToFirst()) {

                videoPath = videoCursor.getString(videoCursor.getColumnIndex(videoThumbnails[0]));
            }

            videoSnapshot.setImageURI(Uri.parse(videoPath));

            return viewItem;
        }
    }
}