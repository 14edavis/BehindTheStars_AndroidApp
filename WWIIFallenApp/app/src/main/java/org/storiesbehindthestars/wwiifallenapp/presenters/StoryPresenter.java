package org.storiesbehindthestars.wwiifallenapp.presenters;

public class StoryPresenter {

    private MVPView view;

    public interface MVPView{ //functions that it has to have...
    }

    public StoryPresenter(MVPView view){
        this.view = view;
    }
}
