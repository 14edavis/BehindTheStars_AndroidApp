package org.storiesbehindthestars.wwiifallenapp.api;

import org.json.JSONException;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
import org.storiesbehindthestars.wwiifallenapp.models.Story;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class Fold3ExSearch {

    private JSONObject jsonResponse;
    private String baseSearch = "https://www.fold3.com/ex-search?keywords=";
    private String endSearch = "&military.conflict=World+War+II&general.title.content.doc-type=STORY_PAGE:Memorial&";//key=";



    public Fold3ExSearch(String textForSearch) throws IOException, JSONException {
        String convertedTextForSearch = textForSearch.replace(" ", "+");

        URL url = new URL(baseSearch + convertedTextForSearch+ endSearch); //+ apiKey);
        Scanner sc = new Scanner(url.openStream());
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.next());
        }
        String result = sb.toString(); //string of JSON results

        jsonResponse = new JSONObject(result); //TODO: SEE IF THIS ACTUALLY WORKS...

//        JSONParser parser = new JSONParser();
//        JSONObject json = (JSONObject) parser.parse(result);
//
//        return json;
    }

    public Story[] getStories() throws JSONException {
        //loop through the JSON response to find all stories
        ArrayList<JSONObject> jsonStoryArrayList = new ArrayList<>();
        JSONObject[] hitList = (JSONObject[]) jsonResponse.get("hits");

        for (int i = 0; i<hitList.length; i++){
            String hitUrl = (String) hitList[i].get("url");
            if (hitUrl.contains("/page/")){
                jsonStoryArrayList.add(hitList[i]);
            }
        }

        //for each JSON story, convert it to a Story object
        Story[] stories = new Story[jsonStoryArrayList.size()];

        for (int i=0; i<stories.length; i++){
            String name = (String) jsonStoryArrayList.get(i).get("label");
            String story ="FIX STORY GETTER THING IN THE API"; //TODO
            String profilePic = (String) "https://img.fold3.com/img/reference/STORY_PAGE/"+jsonStoryArrayList.get(i).get("id") ;
            String bannerPic = profilePic; //TODO - REPLACE PLACE HOLDER

            Story newStory = new Story(name, story, profilePic, bannerPic);

            stories[i] = newStory;
        }

        return stories;
    }



}
