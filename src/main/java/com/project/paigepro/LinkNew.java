/*
    Name:   LinkNew.java
    Author: Sean Smith
    Date:   28 December 2013

    This page displays the interactive web browser feature of the application.
*/

package com.project.paigepro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkNew extends Activity implements View.OnClickListener {

    private EditText url;
    private WebView webBrowser;
    private Button goButton, backButton, forwardButton, refreshButton, clearHistoryButton, bookmarkPage, displayBookmarks;

    private String websiteLaunch, launchBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.linknew);

        webBrowser = (WebView) findViewById(R.id.wvBrowser);

        webBrowser.getSettings().setJavaScriptEnabled(true);
        webBrowser.getSettings().setLoadWithOverviewMode(true);
        webBrowser.getSettings().setUseWideViewPort(true);
        webBrowser.getSettings().setBuiltInZoomControls(true);

        webBrowser.setWebViewClient(new WebBrowserClient());

        try {
            launchBookmark = getIntent().getStringExtra("BookmarkLaunch");

            if (launchBookmark != null && !launchBookmark.isEmpty()) {
                webBrowser.loadUrl(launchBookmark);
            } else {
                webBrowser.loadUrl("http://www.google.ie");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        goButton = (Button) findViewById(R.id.bGo);
        backButton = (Button) findViewById(R.id.bBack);
        forwardButton = (Button) findViewById(R.id.bForward);
        refreshButton = (Button) findViewById(R.id.bRefresh);
        clearHistoryButton = (Button) findViewById(R.id.bHistory);
        bookmarkPage = (Button) findViewById(R.id.bBookmark);
        displayBookmarks = (Button) findViewById(R.id.bBookmarkList);

        url = (EditText) findViewById(R.id.etURL);

        goButton.setOnClickListener(LinkNew.this);
        backButton.setOnClickListener(LinkNew.this);
        forwardButton.setOnClickListener(LinkNew.this);
        refreshButton.setOnClickListener(LinkNew.this);
        clearHistoryButton.setOnClickListener(LinkNew.this);
        bookmarkPage.setOnClickListener(LinkNew.this);
        displayBookmarks.setOnClickListener(LinkNew.this);

        webBrowser.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    public String saveBookmarkNameExtract(String websiteTitle) {

        Pattern pattern = Pattern.compile("\\.(.*?)\\.");
        Matcher matcher = pattern.matcher(websiteTitle);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.bGo:
                websiteLaunch = "http://" + url.getText().toString();
                webBrowser.loadUrl(websiteLaunch);

                InputMethodManager inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethod.hideSoftInputFromWindow(url.getWindowToken(), 0);
                break;

            case R.id.bBack:
                if (webBrowser.canGoBack()) {
                    webBrowser.goBack();
                }
                break;

            case R.id.bForward:
                if (webBrowser.canGoForward()) {
                    webBrowser.goForward();
                }
                break;

            case R.id.bRefresh:
                webBrowser.reload();;
                break;

            case R.id.bHistory:
                webBrowser.clearHistory();
                break;

            case R.id.bBookmark:
                boolean storedStatus = true;

                try {
                    String websiteName = saveBookmarkNameExtract(websiteLaunch);
                    String websiteURL = websiteLaunch;

                    DBBookmark databaseEntry = new DBBookmark(this);
                    databaseEntry.open();

                    if (BookmarkStorage.bookmarkCurrentlyStored(websiteName)) {
                        databaseEntry.createEntry(websiteName, websiteURL);

                    } else {
                        storedStatus = false;

                        Dialog dialog = new Dialog(LinkNew.this);
                        dialog.setTitle("Bookmark Warning");

                        TextView textView = new TextView(LinkNew.this);
                        textView.setText("Bookmark already stored");

                        dialog.setContentView(textView);
                        dialog.show();
                    }
                    databaseEntry.close();
                } catch (Exception exception) {
                    storedStatus = false;
                    String error = exception.toString();

                    Dialog dialog = new Dialog(LinkNew.this);
                    dialog.setTitle("Bookmark Failure");

                    TextView textView = new TextView(LinkNew.this);
                    textView.setText(error);

                    dialog.setContentView(textView);
                    dialog.show();
                } finally {
                    if (storedStatus) {
                        Dialog dialog = new Dialog(LinkNew.this);
                        dialog.setTitle("Bookmark Successful");

                        TextView textView = new TextView(LinkNew.this);
                        textView.setText(saveBookmarkNameExtract(websiteLaunch) + " is stored");

                        dialog.setContentView(textView);
                        dialog.show();
                    }
                }
                break;

            case R.id.bBookmarkList:
                try {
                    Intent bookmarkStorageIntent = new Intent("com.project.paigepro.BOOKMARKSTORAGE");
                    startActivity(bookmarkStorageIntent);
                } catch (Exception exception) {
                    String error = exception.toString();
                    Dialog dialog = new Dialog(LinkNew.this);
                    dialog.setTitle("Bookmarks Retrieval Failure");

                    TextView textView = new TextView(LinkNew.this);
                    textView.setText(error);

                    dialog.setContentView(textView);
                    dialog.show();
                }
                break;
        }
    }
}
