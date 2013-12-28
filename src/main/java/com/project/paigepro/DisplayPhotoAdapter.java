/*
    Name:   DisplayPhotoAdapter.java
    Author: Sean Smith
    Date:   28 December 2013

    This is used to populate the provided listview with stored photos.
*/

package com.project.paigepro;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayPhotoAdapter extends ArrayAdapter<PhotoStorage> {

    private Context context;
    private int layoutID;

    ArrayList<PhotoStorage> photoCollection = new ArrayList<PhotoStorage>();

    public DisplayPhotoAdapter(Context newContext, int newLayoutID, ArrayList<PhotoStorage> newPhotoCollection) {

        super(newContext, newLayoutID, newPhotoCollection);

        this.context = newContext;
        this.layoutID = newLayoutID;
        this.photoCollection = newPhotoCollection;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View tableRow = convertView;
        PhotoHolder photoHolder;

        if (tableRow == null) {

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            tableRow = inflater.inflate(layoutID, parent, false);

            photoHolder = new PhotoHolder();
            photoHolder.tvPhotoDescription = (TextView)tableRow.findViewById(R.id.tvPhotoDescription);
            photoHolder.ivPhoto = (ImageView)tableRow.findViewById(R.id.ivPhoto);
            tableRow.setTag(photoHolder);
        } else {

            photoHolder = (PhotoHolder)tableRow.getTag();
        }

        PhotoStorage picture = photoCollection.get(position);
        photoHolder.tvPhotoDescription.setText(picture.storageName);

        byte[] outImage = picture.storageImage;
        ByteArrayInputStream bitStream = new ByteArrayInputStream(outImage);
        Bitmap bitPhoto = BitmapFactory.decodeStream(bitStream);

        photoHolder.ivPhoto.setImageBitmap(bitPhoto);

        return tableRow;
    }

    private static class PhotoHolder {

        ImageView ivPhoto;
        TextView tvPhotoDescription;
    }
}