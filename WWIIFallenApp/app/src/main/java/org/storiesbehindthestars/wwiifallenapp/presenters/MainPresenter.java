package org.storiesbehindthestars.wwiifallenapp.presenters;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import org.storiesbehindthestars.wwiifallenapp.MainActivity;
import org.storiesbehindthestars.wwiifallenapp.models.Story;


public class MainPresenter {
    MVPView view;
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int PHOTO_REQUEST_CODE = 1;

    Uri outputFileUri;
    String result = "empty";

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";


    public interface MVPView{ //functions that it has to have...
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

    public void readImageText(Bitmap bitmap){
        view.setResultOfTextToImage("Thomas T Takao"); //TODO: Place holder result
    }

    public void searchStories(String textForSearch){
        Story[] stories = new Story[1]; //TODO: IMPLEMENT API SEARCH
        view.goToStories(stories);

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
