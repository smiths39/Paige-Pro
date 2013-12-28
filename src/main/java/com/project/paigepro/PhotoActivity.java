/*
    Name:   PhotoActivity.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays the selected photo and provides the user with the option of deleting the photo.
*/

package com.project.paigepro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class PhotoActivity extends Activity {

    private Button buttonDelete;
    private ImageView photoDetail;
    private Bitmap bitPhoto;
    private int photoID;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.photodisplay);

        buttonDelete = (Button) findViewById(R.id.bDelete);
        photoDetail = (ImageView) findViewById(R.id.ivPhotoDetail);

        Intent photoIntent = getIntent();
        bitPhoto = photoIntent.getParcelableExtra("photoname");
        photoID = photoIntent.getIntExtra("photoid", 20);

        photoDetail.setImageBitmap(bitPhoto);

        buttonDelete.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                DBPhoto dbPhoto = new DBPhoto(PhotoActivity.this);
                dbPhoto.deleteContact(new PhotoStorage(photoID));

                Intent activityIntent = new Intent(PhotoActivity.this, PhotoNew.class);
                startActivity(activityIntent);
                finish();
            }
        });
    }
}