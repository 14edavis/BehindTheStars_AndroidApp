package org.storiesbehindthestars.wwiifallenapp.presenters;

import org.storiesbehindthestars.wwiifallenapp.DirectEntryActivity;

public class DirectEntryPresenter {
    MVPView view;

    public interface MVPView { //functions that it has to have...
    }

    public DirectEntryPresenter (DirectEntryActivity view){
        this.view = view;
    }

    //Functions relating to the API go in the presenter




}
