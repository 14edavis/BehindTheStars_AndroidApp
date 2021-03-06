package org.storiesbehindthestars.wwiifallenapp.presenters;

import android.content.Intent;

import org.storiesbehindthestars.wwiifallenapp.DirectEntryActivity;
import org.storiesbehindthestars.wwiifallenapp.StoriesActivity;

public class DirectEntryPresenter {
    MVPView view;

    public interface MVPView { //functions that it has to have...
        public void goBack(String inputText);
    }

    public DirectEntryPresenter (DirectEntryActivity view){
        this.view = view;
    }

    public void handleOkButtonPressed(String inputText){
        view.goBack(inputText);

        //FOR TESTING
//            Intent intent = new Intent(this, StoriesActivity.class);
//            startActivity(intent);
    }

    //Functions relating to the API go in the presenter




}
