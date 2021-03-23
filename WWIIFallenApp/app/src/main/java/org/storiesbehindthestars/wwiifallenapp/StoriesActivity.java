package org.storiesbehindthestars.wwiifallenapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import org.storiesbehindthestars.wwiifallenapp.components.MemorialView;
import org.storiesbehindthestars.wwiifallenapp.components.ReturnButton;
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

        // THE BACK BUTTON at the top. Also had to list the parent activity in the manifest
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Search Results");

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

        if (stories.length == 0){
            //TODO

            MaterialTextView emptyNote = new MaterialTextView(this,null, R.attr.textAppearanceHeadline6);
            emptyNote.setText("No Stories Found");
            LinearLayout.LayoutParams noteParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            noteParams.setMargins(0,24,0,24);
            noteParams.gravity = (Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            emptyNote.setLayoutParams(noteParams);

            mainLayout.addView(emptyNote);
        }

//        MaterialButton returnButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
//        returnButton.setText("Return To Home");
//
//        LinearLayout.LayoutParams buttonParams =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        buttonParams.gravity = (Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
//        returnButton.setLayoutParams(buttonParams);
//        returnButton.setIconResource(R.drawable.ic_baseline_arrow_back_24);
//
//        returnButton.setOnClickListener((view) ->{
//            this.finish();
//        });

        ReturnButton returnButton = new ReturnButton(this);
        mainLayout.addView(returnButton);

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