package org.storiesbehindthestars.wwiifallenapp.presenters;

public class StoryPresenter {

    private MVPView view;

    public interface MVPView{ //functions that it has to have...
        public void goToFold3Page();
    }

    public StoryPresenter(MVPView view){
        this.view = view;
    }

    public void handleViewButtonClick(){
        view.goToFold3Page();
    }
}
