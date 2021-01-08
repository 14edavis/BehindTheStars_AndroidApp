package org.storiesbehindthestars.wwiifallenapp.presenters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.storiesbehindthestars.wwiifallenapp.MainActivity;
import org.storiesbehindthestars.wwiifallenapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class MainPresenter {
    MVPView view;
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int PHOTO_REQUEST_CODE = 1;

    Uri outputFileUri;
    String result = "empty";

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";


    public interface MVPView{ //functions that it has to have...
//        String result = null; //FOR TESTING, redo later

        public void takePhoto();
        public void goToDirectEntry();
        public void goToCheckAccuracy();
//        public void copyTessDataFiles(String path);
        public void goToPhotos();
    }

    public MainPresenter(MainActivity view){
        this.view = view;
    }


    //Functions relating to the API & image-to text go in the presenter

//    public String handlePhotoTaken(){
//        return "hello world";
//    }

    public String getImageText(){
        return "helloWorld";
    }

    public void setOutputFileUri(Uri outputFileUri) {
        this.outputFileUri = outputFileUri;
    }

    public void handleScanPressed(){
//        view.takePhoto();

//        getImageText();

//        extractText();
//        outputFileUri = Uri.parse("storage/emulated/0/Download/90951844_133832664487.jpg"); // fromFile(new File("storage/emulated/0/Download/90951844_133832664487.jpg"));
        view.goToPhotos();
//        view.goToCheckAccuracy();
//        doOCR();


//        return result;

    }

    public void handleEnterDirectlyPressed(){
        view.goToDirectEntry();
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
