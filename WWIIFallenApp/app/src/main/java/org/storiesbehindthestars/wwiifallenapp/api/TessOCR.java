package org.storiesbehindthestars.wwiifallenapp.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.storiesbehindthestars.wwiifallenapp.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TessOCR {
    private final TessBaseAPI mTess;
    private Context context;

    //from github example
    private String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/WWIIFallenApp/";//"/TesseractSample/";
    private static final String TESSDATA = "tessdata"; //DO NOT CHANGE
    private static final String TAG = MainActivity.class.getSimpleName();


    public TessOCR(Context context, String language) {
        mTess = new TessBaseAPI();
        this.context = context;
//
//        String datapath = context.getFilesDir() + "/sampledata/"; //"/tesseract/";
//        datapath = Environment.getExternalStorageDirectory() + "/sampledata/";
//
//        datapath = context.getFilesDir().getPath();
//
//        datapath = Environment.getExternalStorageDirectory().toString() + "/WWIIFallenApp/tessdata/";

        DATA_PATH = context.getFilesDir()+"";


        copyTessDataFiles(TESSDATA);
        String datapath = DATA_PATH;

        mTess.init(datapath, language);
    }

    public String getOCRResult(Bitmap bitmap) {
        mTess.setImage(bitmap);
        return mTess.getUTF8Text();
    }

    public void onDestroy() {
        if (mTess != null) mTess.end();
    }


    /**
     * source: https://github.com/ashomokdev/Tess-two_example/blob/master/app/src/main/java/com/ashomok/tesseractsample/MainActivity.java
     * Copy tessdata files (located on assets/tessdata) to destination directory
     *
     * @param path - name of directory with .traineddata files
     */
    private void copyTessDataFiles(String path) {
        File root = new File(context.getFilesDir(), "tessdata");
        if (!root.exists()) {
            root.mkdirs();
        }

        try {
            String fileList[] = context.getAssets().list(path);
            Log.d(TAG, "length of getAssets request: " + fileList.length);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + "/"+ path + "/" + fileName; //added / before path --> FIXED IT! YAY!!!
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = context.getAssets().open(path + "/" + fileName);

                    OutputStream out = new FileOutputStream(pathToDataFile);

                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Log.d(TAG, "Copied " + fileName + "to tessdata");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
        }
    }

}

//Source: https://solidgeargroup.com/en/ocr-on-android/