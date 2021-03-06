package com.cooper.nwemail.ui.news.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.cooper.nwemail.R;
import com.cooper.nwemail.application.ApplicationComponent;
import com.cooper.nwemail.application.NWEApplication;
import com.cooper.nwemail.constants.Constants;
import com.cooper.nwemail.ui.common.BaseActivity;

import javax.inject.Inject;

import butterknife.InjectView;
import butterknife.Optional;

/**
 * The NewsDetailActivity is the primary Activity used to display a news article.
 */
public class NewsDetailActivity extends BaseActivity implements NewsDetailView {

    private static final String TAG = "NewsDetailActivity";
    public static final String ARG_ARTICLE = "ARG_ARTICLE";

    @Inject
    NewsDetailPresenter presenter;

    @Optional
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @Optional
    @InjectView(R.id.textView_article_title)
    TextView mArticleTitle;
    @Optional
    @InjectView(R.id.textView_article_subheading)
    TextView mArticleSubheading;
    @Optional
    @InjectView(R.id.textView_article_text1)
    TextView mArticleText1;
    @Optional
    @InjectView(R.id.textView_article_text2)
    TextView mArticleText2;
    @Optional
    @InjectView(R.id.textView_pubDate)
    TextView mPubDate;

    protected String mGuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        //Get the article
        final Bundle arguments = getIntent().getExtras();
        if (arguments != null && arguments.containsKey(ARG_ARTICLE)) {
            mGuid = arguments.getString(ARG_ARTICLE);
        } else {
            finish();
        }

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        presenter.setArticleId(mGuid);
    }

    protected int getLayout() {
        return R.layout.activity_news_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_article_detail, menu);
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                shareArticle();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void shareArticle() {
        final Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, presenter.getArticleURL());
        startActivity(Intent.createChooser(sendIntent, getText(R.string.share_via)));

        /** Analytics **/
        NWEApplication.sendEvent(Constants.GAC_USER_INTERACTION, Constants.GAA_SHARE);
    }

    @Override
    public void setActivityTitle(final String title) {
        //setTitle(Html.fromHtml(title));
        setTitle(title);
    }

    @Override
    public void setArticleHeadline(String headline) {
        if (mArticleTitle != null)
            mArticleTitle.setText(headline);
    }

    @Override
    public void setArticleSubheading(String subheading) {
        if (mArticleSubheading != null)
            mArticleSubheading.setText(subheading);
    }

    @Override
    public void setArticleText1(final String text) {
        if (mArticleText1 != null)
            mArticleText1.setText(text);
    }

    @Override
    public void setArticleText2(String text) {
        if (mArticleText2 != null)
            mArticleText2.setText(text);
    }

    @Override
    public void setArticlePubDate(String pubDate) {
        if (mPubDate != null)
            mPubDate.setText(getString(R.string.last_updated).concat(" " + pubDate));
    }

    @Override
    public void setArticleImage(String imageUrl) {
        //We ignore it in this activity as we don't have an image to display
    }

    @Override
    public void setImageCaption(String caption) {
    }

    @Override
    public void showMiddleAdvert() {
        Log.d(TAG, "showing Middle Advert");
        //Show the middle advert
    }

    @Override
    public void showBottomAdvert() {
        //Show the bottom advert
    }

    @Override
    public void displayError() {
        Snackbar.make(findViewById(android.R.id.content),
                R.string.snackbar_display_fail, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showLoading() {
        //TODO
    }

    @Override
    public void hideLoading() {
        //TODO
    }

    @Override
    protected void setupComponent(ApplicationComponent applicationComponent) {
        DaggerNewsDetailComponent.builder()
                .applicationComponent(applicationComponent)
                .newsDetailModule(new NewsDetailModule(this))
                .build()
                .inject(this);
    }


    @Override
    protected void onDestroy() {
        //Destroy the adverts
        super.onDestroy();
    }
}
