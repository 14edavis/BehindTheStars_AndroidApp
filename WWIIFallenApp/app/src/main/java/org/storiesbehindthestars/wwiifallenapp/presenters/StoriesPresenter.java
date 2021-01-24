package org.storiesbehindthestars.wwiifallenapp.presenters;

import android.util.Log;

import org.storiesbehindthestars.wwiifallenapp.StoriesActivity;

public class StoriesPresenter {

    MVPView view;
    private final String URL_SEARCH_START = "https://www.fold3.com/search?keywords=";
    private final String URL_SEARCH_END = "&military.conflict=World+War+II&general.title.content.doc-type=STORY_PAGE:Memorial";

    public interface MVPView{ //functions that it has to have...
         }

    public StoriesPresenter (StoriesActivity view){
        this.view = view;
    }

    public String createSearchUrl(String nameToSearch){
        String urlToGoTo = URL_SEARCH_START;

        String delims = "[ ]+";
        String[] tokens = nameToSearch.split(delims);
        for (int i = 0; i < tokens.length; i++){
            urlToGoTo = urlToGoTo + tokens[i];
            if (i != tokens.length-1){
                urlToGoTo = urlToGoTo + "+";
            }
        }

        urlToGoTo = urlToGoTo + URL_SEARCH_END;
        Log.e("url created:",  urlToGoTo);

        return urlToGoTo;
    }


}
