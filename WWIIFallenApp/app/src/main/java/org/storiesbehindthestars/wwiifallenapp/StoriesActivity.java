package org.storiesbehindthestars.wwiifallenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import org.storiesbehindthestars.wwiifallenapp.presenters.StoriesPresenter;

public class StoriesActivity extends AppCompatActivity implements StoriesPresenter.MVPView{

    StoriesPresenter presenter;

    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainLayout = new LinearLayout(this);
        presenter = new StoriesPresenter(this);

        //Web View -- will probably swap out for something else later...
        final WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient());

        String urlToGoTo = presenter.createSearchUrl("Thomas T Takao"); //TODO: replace filler search
        webView.loadUrl(urlToGoTo);

        mainLayout.addView(webView);
        setContentView(mainLayout);

    }
}