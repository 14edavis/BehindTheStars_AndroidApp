package org.storiesbehindthestars.wwiifallenapp.presenters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import org.json.JSONException;
import org.storiesbehindthestars.wwiifallenapp.MainActivity;
import org.storiesbehindthestars.wwiifallenapp.api.Fold3ExSearch;
import org.storiesbehindthestars.wwiifallenapp.api.TessOCR;
import org.storiesbehindthestars.wwiifallenapp.models.Story;

import java.io.IOException;


public class MainPresenter {
    MVPView view;
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int PHOTO_REQUEST_CODE = 1;

    Uri outputFileUri;
    String result = "empty";

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";


    public interface MVPView{ //functions that it has to have...
        public TessOCR mTessOCR = null;

        public void goToCamera();
        public void goToDirectEntry();
        public void goToDirectEntry(String imageToTextResult);
        public void goToPhotos();
        public void setResultOfTextToImage(String resultOfTextToImage);
        public void goToStories(Story[] stories);

    }

    public MainPresenter(MainActivity view){
        this.view = view;
    }


    //Functions relating to the API & image-to text go in the presenter


    public void setOutputFileUri(Uri outputFileUri) {
        this.outputFileUri = outputFileUri;
    }

    public void handleScanPressed(){
        view.goToCamera();
        //rest is handled in view.onActivityResult()
    }

    public void handleSelectImagePressed(){
        view.goToPhotos();
        //rest is handled in view.onActivityResult()
    }

    public void handleEnterDirectlyPressed(){

        view.goToDirectEntry();
    }


    /*Discovered that this needs to be in the Activity for it to work properly*/
//    //READING TEXT FROM IMAGE
//    public void readImageText(Bitmap bitmap){
//        view.setResultOfTextToImage("Thomas T Takao"); //TODO: Place holder result
//        final String srcText = view.mTessOCR.getOCRResult(bitmap);
//
//        if (srcText != null && !srcText.equals("")){    //if it found something
//            view.setResultOfTextToImage(srcText);
//        }
//        else{   //if it's empty
//            view.setResultOfTextToImage("Error Reading Image");
//        }
//
//        view.mTessOCR.onDestroy();
//    }




    //FOLD3 API DATABASE HOOK UP
    public Story[] searchStories(String textForSearch) throws IOException, JSONException {
        //TODO: IMPLEMENT API SEARCH

        Fold3ExSearch searchAPI = new Fold3ExSearch(textForSearch);
        //search fold3.com/ex-search for matching pages

        Story[] stories = searchAPI.getStories(); //TODO
        //get name, story, thumbnail, and banner from searching
        //create a list of stories to pass on



        //test stories
//        Story[] stories = { new Story ("TEST STORY", "1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing",
//                "https://img.fold3.com/img/reference/STORY_PAGE/91243229?width=172&height=215&refresh=509",
//                "https://img.fold3.com/img/reference/BACKGROUND-IMAGE/91243229??refresh=941"),
//                new Story ("TEST STORY 2", "1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing",
//                        "https://img.fold3.com/img/reference/STORY_PAGE/91243229?width=172&height=215&refresh=509",
//                        "https://img.fold3.com/img/reference/BACKGROUND-IMAGE/91243229??refresh=941"),
//                new Story ("TEST STORY 3", "1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing1 2 3 testing",
//                        "https://img.fold3.com/img/reference/STORY_PAGE/91243229?width=172&height=215&refresh=509",
//                        "https://img.fold3.com/img/reference/BACKGROUND-IMAGE/91243229??refresh=941")};

        return stories;
    }



////from example at: https://stackoverflow.com/questions/7710123/how-can-i-use-tesseract-in-android
//    private String extractText(Bitmap bitmap) throws Exception{
//        TessBaseAPI tessBaseApi = new TessBaseAPI();
//        tessBaseApi.init(DATA_PATH, "eng");
//        tessBaseApi.setImage(bitmap);
//        String extractedText = tessBaseApi.getUTF8Text();
//        tessBaseApi.end();
//        return extractedText;
//    }




    /**
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
//    private void copyTessDataFiles(String path) {
//        try {
//            String fileList[] = getAssets().list(path);
//
//            for (String fileName : fileList) {
//
//                // open file within the assets folder
//                // if it is not already there copy it to the sdcard
//                String pathToDataFile = DATA_PATH + path + "/" + fileName;
//                if (!(new File(pathToDataFile)).exists()) {
//
//                    InputStream in = getAssets().open(path + "/" + fileName);
//
//                    OutputStream out = new FileOutputStream(pathToDataFile);
//
//                    // Transfer bytes from in to out
//                    byte[] buf = new byte[1024];
//                    int len;
//
//                    while ((len = in.read(buf)) > 0) {
//                        out.write(buf, 0, len);
//                    }
//                    in.close();
//                    out.close();
//
//                    Log.d(TAG, "Copied " + fileName + "to tessdata");
//                }
//            }
//        } catch (IOException e) {
//            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
//        }
//    }

}
