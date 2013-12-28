/*
    Name:   PhotoNew.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays the interactive photo feature of the application.
*/

package com.project.paigepro;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PhotoNew extends Activity {

    private Button addPhoto;
    private ListView photoList;

    private ArrayList<PhotoStorage> photoArray = new ArrayList<PhotoStorage>();
    private String[] captureOption = new String[] { "Take Photo from Camera", "Select Photo from Gallery" };
    private DisplayPhotoAdapter photoAdapter;

    private byte[] photoName;
    private int photoID;
    private Bitmap photoBitmap;
    private DBPhoto database;

    private static final int CAMERA_REQUEST = 1;
    private static final int GALLERY_REQUEST = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.photonew);

        photoList = (ListView) findViewById(R.id.lvPhoto);
        database = new DBPhoto(PhotoNew.this);

        List<PhotoStorage> photoCollection = database.getAllContacts();

        for (PhotoStorage photo : photoCollection) {

            photoArray.add(photo);
        }

        photoAdapter = new DisplayPhotoAdapter(PhotoNew.this, R.layout.photolist, photoArray);
        photoList.setAdapter(photoAdapter);

        photoList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int index, long l) {

                photoName = photoArray.get(index).getPhotoImage();
                photoID = photoArray.get(index).getPhotoID();

                ByteArrayInputStream photoStream = new ByteArrayInputStream(photoName);
                photoBitmap = BitmapFactory.decodeStream(photoStream);

                Intent photoIntent = new Intent(PhotoNew.this, PhotoActivity.class);
                photoIntent.putExtra("photoname", photoBitmap);
                photoIntent.putExtra("photoid", photoID);
                startActivity(photoIntent);
            }
        });

        ArrayAdapter<String> optionAdapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, captureOption);

        AlertDialog.Builder builder = new AlertDialog.Builder(PhotoNew.this);
        builder.setTitle("Select Option");

        builder.setAdapter(optionAdapter, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int option) {

                if (option == 0) {
                    initialiseCamera();
                } else if (option == 1) {
                    initialiseGallery();
                }
            }
        });

        final AlertDialog dialog = builder.create();

        addPhoto = (Button) findViewById(R.id.bAddPhoto);

        addPhoto.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case CAMERA_REQUEST:
                    Bundle cameraExtra = data.getExtras();

                    if (cameraExtra != null) {

                        Bitmap bitPhoto = cameraExtra.getParcelable("data");
                        ByteArrayOutputStream bitStream = new ByteArrayOutputStream();
                        bitPhoto.compress(Bitmap.CompressFormat.PNG, 100, bitStream);

                        byte imageInByte[] = bitStream.toByteArray();
                        database.addPhotoStorage(new PhotoStorage(DateFormat.getDateTimeInstance().format(new Date()), imageInByte));

                        Intent cameraIntent = new Intent(PhotoNew.this, PhotoNew.class);
                        startActivity(cameraIntent);
                        finish();
                    }
                    break;

                case GALLERY_REQUEST:

                    Bundle galleryExtra = data.getExtras();

                    if (galleryExtra != null) {

                        Bitmap bitPhoto = galleryExtra.getParcelable("data");
                        ByteArrayOutputStream bitStream = new ByteArrayOutputStream();
                        bitPhoto.compress(Bitmap.CompressFormat.PNG, 100, bitStream);

                        byte imageInByte[] = bitStream.toByteArray();
                        database.addPhotoStorage(new PhotoStorage(DateFormat.getDateTimeInstance().format(new Date()), imageInByte));

                        Intent galleryIntent = new Intent(PhotoNew.this, PhotoNew.class);
                        startActivity(galleryIntent);
                        finish();
                    }
                    break;
            }
        }
    }

    public void initialiseCamera() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra("crop", "true");
        cameraIntent.putExtra("outputX", 200);
        cameraIntent.putExtra("outputY", 150);

        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    public void initialiseGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        galleryIntent.putExtra("crop", "true");
        galleryIntent.putExtra("outputX", 400);
        galleryIntent.putExtra("outputY", 350);
        galleryIntent.putExtra("return-data", true);

        startActivityForResult(Intent.createChooser(galleryIntent, "Complete Action Using"), GALLERY_REQUEST);
    }
}