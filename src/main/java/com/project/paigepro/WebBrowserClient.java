/*
    Name:   WebBrowserClient.java
    Author: Sean Smith
    Date:   28 December 2013

    This is used for the activation of the entered url within the application's web browser.
*/

package com.project.paigepro;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebBrowserClient extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        view.loadUrl(url);
        return true;
    }
}
