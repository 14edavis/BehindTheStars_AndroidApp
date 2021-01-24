package org.storiesbehindthestars.wwiifallenapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.storiesbehindthestars.wwiifallenapp.components.MemorialView;
import org.storiesbehindthestars.wwiifallenapp.models.Story;
import org.storiesbehindthestars.wwiifallenapp.presenters.StoriesPresenter;

public class StoriesActivity extends AppCompatActivity implements StoriesPresenter.MVPView{

    public static final String STORY = "story";

    StoriesPresenter presenter;
    Story[] stories;
    LinearLayout mainLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new StoriesPresenter(this);
        stories = (Story[]) getIntent().getSerializableExtra(MainActivity.STORIES_EXTRA);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        displayStories();

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(mainLayout);

        setContentView(scrollView);

        if (stories.length == 1){ // if there's only one, just go straight to it
            goToStory(stories[0]);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void displayStories(){ //TODO:Adjust once MemorialView is updated to accept an actual story
        for (int i = 0; i < stories.length; i++){
            MemorialView memorialView = new MemorialView(this, stories[i], false);
            int storyIndex = i;
            memorialView.setOnClickListener((view)->{
                presenter.handleStoryClicked(storyIndex);

//                Intent intent = new Intent(this, StoryActivity.class);
//                startActivity(intent);
            });
            mainLayout.addView(memorialView);
        }
    }

    public void goToStory(int storyIndex){
        goToStory(stories[storyIndex]);
    }

    public void goToStory(Story story){
        Intent intent = new Intent(this, StoryActivity.class);
        intent.putExtra(STORY, story);
        startActivity(intent);
    }

}