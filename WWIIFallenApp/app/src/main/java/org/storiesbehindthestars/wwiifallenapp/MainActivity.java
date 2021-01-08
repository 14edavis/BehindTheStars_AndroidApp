package org.storiesbehindthestars.wwiifallenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.storiesbehindthestars.wwiifallenapp.presenters.MainPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity implements MainPresenter.MVPView {

    public final int STORAGE_PERMISSION_REQUESTED = 0;

    public final int FIND_MATCH = 1;
    public final int ANALYZE_IMAGE = 2;
    public final int SELECT_IMAGE = 3;
    MainPresenter presenter;

//    String result = "IN TESTING MODE";

    AppCompatTextView testingTextView;
    private ProgressBar progressBar;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        testingTextView = new AppCompatTextView(this);
        testingTextView.setText("IN TESTING MODE");

        MaterialButton scanButton = new MaterialButton(this);
        scanButton.setText("Scan");
        scanButton.setOnClickListener((view)->{
            presenter.handleScanPressed();
//            testingTextView.setText(message);

        });

        MaterialButton enterDirectlyButton = new MaterialButton(this);
        enterDirectlyButton.setText("Enter Name Manually");
        enterDirectlyButton.setOnClickListener((view) ->{
            presenter.handleEnterDirectlyPressed();
        });

        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.INVISIBLE);

        mainLayout.addView(scanButton);
        mainLayout.addView(enterDirectlyButton);
        mainLayout.addView(testingTextView);
        mainLayout.addView(progressBar);

        setContentView(mainLayout);

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUESTED); //todo: PUT WHERE THIS MAKES MORE SENSE


        File file = new File(Environment.getExternalStorageDirectory().toString()+ "/WWIIFallenApp/tessdata/");
        Log.e("file:", file.toString());
//        try {
//            file.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        if(!file.exists()) {
//            Toast.makeText(getApplicationContext(),"Directory does not exist, create it",
//                    Toast.LENGTH_LONG).show();
//        }
    }

    //functions relating to what the user sees displayed go in the activity. Otherwise, refer to the presenter.

    @Override
    public void takePhoto(){

    }

    @Override
    public void goToDirectEntry() {
        Intent intent = new Intent(this, DirectEntryActivity.class);
        startActivityForResult(intent, FIND_MATCH);

    }

    @Override
    public void goToCheckAccuracy(){

    }

    @Override
    public void goToPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //FOR PICK PICTURE
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            Uri pictureUri = data.getData();
            try {
                //solution from: https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureUri);

                //TODO: Need to get Tess image-to-text working
//                TessAsyncTask task = new TessAsyncTask(this, bitmap);
//                task.execute(10);

            } catch (IOException e) {
                e.printStackTrace();
            }
//            presenter.setOutputFileUri(pictureUri);
//            presenter.doOCR();

        }

        //FOR ANALYZE_IMAGE code
        //if success

        //if no text found

        //otherwise, if failure


        //FOR FIND_MATCH requestCode...
        //if it found a single match, display the story

        //if it found multiple matches, display a stories list

        //if it the search failed, show a failure message (on the stories page?)

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


//based on: https://codinginflow.com/tutorials/android/asynctask
    private static class TessAsyncTask extends AsyncTask<Integer, Integer, String> {
        private static final String TESSDATA = "tessdata";
        private Bitmap bitmap;
        private TessBaseAPI tessBaseApi;
        private static final String lang = "eng";

    private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString()+ "/TesseractSample/";
    private static final String TAG = MainActivity.class.getSimpleName();

        private WeakReference<MainActivity> activityWeakReference;

        TessAsyncTask(MainActivity activity, Bitmap bitmap) {
            activityWeakReference = new WeakReference<MainActivity>(activity);
            this.bitmap = bitmap;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Integer... integers) {

            //PUT ACTION HERE
            prepareTesseract();
            String result = extractText(bitmap);

//            testingTextView.setText(result);

            return "Finished!";
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            activity.progressBar.setProgress(values[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MainActivity activity = activityWeakReference.get();
            if (activity == null || activity.isFinishing()) {
                return;
            }
            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            activity.progressBar.setProgress(0);
            activity.progressBar.setVisibility(View.INVISIBLE);
        }

        //TESS STUFF

        // FROM: https://github.com/ashomokdev/Tess-two_example/blob/master/app/src/main/java/com/ashomok/tesseractsample/MainActivity.java

        //    public void doOCR() {
        //        prepareTesseract();
        //        startOCR(outputFileUri);
        //    }

        /**
         * Prepare directory on external storage
         *
         * @param path
         * @throws Exception
         */
        private void prepareDirectory(String path) {

            File dir = new File(path);
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
                }
            } else {
                Log.i(TAG, "Created directory " + path);
            }
        }


        public void prepareTesseract() {
            try {
                prepareDirectory(DATA_PATH + TESSDATA);
            } catch (Exception e) {
                e.printStackTrace();
            }

            copyTessDataFiles(TESSDATA);
        }

        /**
         * don't run this code in main thread - it stops UI thread. Create AsyncTask instead.
         * http://developer.android.com/intl/ru/reference/android/os/AsyncTask.html
         *
         * @param imgUri
         */
    //    private void startOCR(Uri imgUri) {
    //        try {
    //            BitmapFactory.Options options = new BitmapFactory.Options();
    //            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
    //            Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);
    //
    //            result = extractText(bitmap);
    //            result = "testing! ...";
    //
    ////            textView.setText(result); TODO: THIS!
    //
    //        } catch (Exception e) {
    //            Log.e(TAG, e.getMessage());
    //        }
    //    }


        public String extractText(Bitmap bitmap) {
            try {
                tessBaseApi = new TessBaseAPI();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                if (tessBaseApi == null) {
                    Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
                }
            }

            tessBaseApi.init(DATA_PATH, lang);  //TODO: for some reason it crashes here. It says that the file path does not exist.

    //       //EXTRA SETTINGS
    //        //For example if we only want to detect numbers
    //        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
    //
    //        //blackList Example
    //        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
    //                "YTRWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");

            Log.d(TAG, "Training file loaded");
            tessBaseApi.setImage(bitmap);
            String extractedText = "empty result";
            try {
                extractedText = tessBaseApi.getUTF8Text();
            } catch (Exception e) {
                Log.e(TAG, "Error in recognizing text.");
            }
            tessBaseApi.end();
            return extractedText;
        }

    public void copyTessDataFiles(String path) {
        MainActivity activity = activityWeakReference.get();
        try {
            String fileList[] = activity.getAssets().list(path);

            for (String fileName : fileList) {

                // open file within the assets folder
                // if it is not already there copy it to the sdcard
                String pathToDataFile = DATA_PATH + path + "/" + fileName;
                if (!(new File(pathToDataFile)).exists()) {

                    InputStream in = activity.getAssets().open(path + "/" + fileName);

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



}

