/*
    Name:   DisplayBookmarkAdapter.java
    Author: Sean Smith
    Date:   28 December 2013

    This is used to populate the provided listview with stored bookmarks.
*/

package com.project.paigepro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplayBookmarkAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> bookmarkID;
    private ArrayList<String> bookmarkTitle;
    private ArrayList<String> bookmarkURL;

    public DisplayBookmarkAdapter(Context newContext, ArrayList<String> newBookmarkID, ArrayList<String> newBookmarkTitle, ArrayList<String> newBookmarkURL) {

        this.context = newContext;
        this.bookmarkID = newBookmarkID;
        this.bookmarkTitle = newBookmarkTitle;
        this.bookmarkURL = newBookmarkURL;
    }

    @Override
    public int getCount() {

        return bookmarkID.size();
    }

    @Override
    public Object getItem(int index) {

        return null;
    }

    @Override
    public long getItemId(int index) {

        return 0;
    }

    @Override
    public View getView(int position, View child, ViewGroup parent) {

        Holder holder;
        LayoutInflater layoutInflater;

        if (child == null) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.bookmarkdisplay, null);

            holder = new Holder();
            holder.tvBookmarkID = (TextView) child.findViewById(R.id.tvBookmarkID);
            holder.tvBookmarkTitle = (TextView) child.findViewById(R.id.tvBookmarkTitle);
            holder.tvBookmarkURL = (TextView) child.findViewById(R.id.tvBookmarkURL);

            child.setTag(holder);
        } else {
            holder = (Holder) child.getTag();
        }

        holder.tvBookmarkID.setText(bookmarkID.get(position));
        holder.tvBookmarkTitle.setText(bookmarkTitle.get(position));
        holder.tvBookmarkURL.setText(bookmarkURL.get(position));

        return child;
    }

    private static class Holder {

        TextView tvBookmarkID;
        TextView tvBookmarkTitle;
        TextView tvBookmarkURL;
    }
}
