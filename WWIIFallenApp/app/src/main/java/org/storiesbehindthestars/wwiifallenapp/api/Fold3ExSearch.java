package org.storiesbehindthestars.wwiifallenapp.api;

import android.os.Build;
import android.util.JsonReader;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.simple.parser.JSONParser;
import org.storiesbehindthestars.wwiifallenapp.models.Story;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;


public class Fold3ExSearch {

    private JSONObject jsonResponse;
    private String baseSearch = "https://www.fold3.com/ex-search?resultTypes=memorials&keywords=www.storiesbehindthestars.org,";
    private String endSearch = "&apiKey=x2P6Q2Pf9jT7xREfcAyBRjJX";//"&apiKey=PLACEHOLDER"; //todo: insert API Key
    private String storybaseSearch = "https://www.fold3.com/partner-memorial?id=";

    public Fold3ExSearch(String textForSearch) throws IOException, JSONException {
        String convertedTextForSearch = textForSearch.replace(" ", ",");

        URL url = new URL(baseSearch + convertedTextForSearch+ endSearch); //+ apiKey);
        Scanner sc = new Scanner(url.openStream());
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.next());
        }
        String result = sb.toString(); //string of JSON results

        jsonResponse = new JSONObject(result);

//        JSONParser parser = new JSONParser();
//        JSONObject json = (JSONObject) parser.parse(result);
//
//        return json;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Story[] getStories() throws JSONException, IOException {
        //loop through the JSON response to find all stories, identify which ones are actually stories...
        ArrayList<JSONObject> jsonStoryArrayList = new ArrayList<>();
        JSONArray memorialList = (JSONArray) jsonResponse.get("hits");

        for (int i = 0; i<memorialList.length(); i++){
            JSONObject hitUrlObj = (JSONObject) memorialList.get(i);
            String hitUrl = (String) hitUrlObj.get("url");
            if (hitUrl.contains("/page/")){
                jsonStoryArrayList.add((JSONObject) memorialList.get(i));
            }
        }

        //for each JSON story, convert it to a Story object
        Story[] stories = new Story[jsonStoryArrayList.size()];

        //NAME
        for (int i=0; i<stories.length; i++){
            String name = (String) jsonStoryArrayList.get(i).get("label");

            //ADDING SPACES...
            for (int j = 1; j < name.length(); j++){
                if ((int) name.charAt(j) > 65 && (int) name.charAt(j) < 90){
                    name = name.substring(0, j) + " " + name.substring(j);
                    j++;
                }
            }

            //WEBPAGE
            URL url = new URL((String) jsonStoryArrayList.get(i).get("url"));

            //TODO: This currently just scrapes the webpage to find the picture. Future updates should
            //      integrate actually with Fold3's database to find the proper images
            //ID + PICTURES
            String pageID = jsonStoryArrayList.get(i).get("id").toString();
            String profilePic = (String) "https://img.fold3.com/img/reference/STORY_PAGE/"
                    +pageID
                    +"?width=100&height=100"
                    +endSearch;
            //STORY
//                        String story = readStoryFromWebpage(url);
            String story = "null";
            try{
                story = getStory(pageID);}
            catch(IOException e){
                story = "Unable to connect to Fold3.";
            }
            catch(JSONException e){
                story = "JSON Exception Occurred";
            }
            catch(Exception e){
                story = "Something went wrong with retrieving this story.";
            }


            //TODO - connect to custom background. This is just a default.
            //The image loading was failing because fold3 updated their background picture label
            //  and the api was pulling a faulty url
            String bannerPic = "https://img.fold3.com/img/memorialbg?width=500&id=91298175&amp;refresh=890";

            //obsolete
//                    "https://img.fold3.com/img/reference/BACKGROUND-IMAGE/"
//                    +pageID
//                    +"?";
//https://img.fold3.com/img/reference/BACKGROUND-IMAGE/
            Story newStory = new Story(name, story, profilePic, bannerPic, url.toString());

            stories[i] = newStory;
        }

        return stories;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getStory(String pageID) throws JSONException, IOException {
        URL url = new URL(storybaseSearch + pageID);
        Scanner sc = new Scanner(url.openStream());
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.next());
        }
        String result = sb.toString(); //string of JSON results
        JSONObject jsonStoryResponse = new JSONObject(result); //create object
        JSONArray storiesJSON = new JSONArray(jsonStoryResponse.get("stories")); //get the stories
        JSONObject storyJSON = new JSONObject((String) storiesJSON.get(0)); //gets the first story
        String storyText = (String) storyJSON.get("story"); //get the story's text
        storyText = parseOutHTML(storyText);
        storyText = translateAscii(storyText);
        return result;
    }

    public String readStoryFromWebpage(URL url) throws IOException {
        //Instantiating the URL class
//        URL url = new URL("http://www.something.com/");
        //Retrieving the contents of the specified page
        Scanner sc = new Scanner(url.openStream());
        //Instantiating the StringBuffer class to hold the result
        StringBuffer sb = new StringBuffer();
        while(sc.hasNext()) {
            sb.append(sc.next()+" ");
            //System.out.println(sc.next());
        }
        //Retrieving the String from the String Buffer object
        String result = sb.toString();
        System.out.println(result);
        //Removing the HTML tags
//        final String startOfStoryText = "{\"metadata\":{\"id\":{\"contentType\":\"STORY\"";
        if (result.indexOf("StoryBlock-text\">") == -1){
//        if (result.indexOf(startOfStoryText) == -1){
            return "";
        }
        result = result.substring(
                result.indexOf("StoryBlock-text\">")+17

        );
        result = result.substring(0, result.indexOf("</div>"));
//        result = result.replaceAll("<[^>]*>", "");
        System.out.println("Contents of the web page: "+result);

        result = parseOutHTML(result);
        result = translateAscii(result);
        return result;
    }


    public String parseOutHTML(String stringWithHTML){
        StringBuilder sb = new StringBuilder();
        while (stringWithHTML.contains("<") ){
            //search for sections
            int startTag = stringWithHTML.indexOf("<");
            int endTag = stringWithHTML.indexOf(">")+1;
            String htmlTag = stringWithHTML.substring(startTag, endTag);
            if (htmlTag.equals("</p>")){
                sb.append("\n\n");
            }
            //remove those sections from orginal
            stringWithHTML = stringWithHTML.substring(endTag);
            int startNextTag = stringWithHTML.indexOf("<");
            if (startNextTag-1 > 0) {
                sb.append(
                        stringWithHTML.substring(
                                0,
                                startNextTag
                        )
                );
            }
        }
        return sb.toString();
    }


    String translateAscii(String storyText){
        for(int i = 21; i < 30; i++){
            String ascii = "&#x"+i+";"; //&#x21 = ascii 33
            String replacement = Character.toString( (char) (i+(33-21)));
            storyText = storyText.replace(ascii, replacement);
        }
        return storyText;
    }

}
