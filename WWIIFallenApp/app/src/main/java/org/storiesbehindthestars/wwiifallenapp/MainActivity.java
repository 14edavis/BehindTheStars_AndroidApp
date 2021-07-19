package org.storiesbehindthestars.wwiifallenapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.json.JSONException;
import org.storiesbehindthestars.wwiifallenapp.api.Fold3ExSearch;
import org.storiesbehindthestars.wwiifallenapp.api.TessOCR;
import org.storiesbehindthestars.wwiifallenapp.models.Story;
import org.storiesbehindthestars.wwiifallenapp.presenters.MainPresenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity implements MainPresenter.MVPView {
//NOTE: https://www.fold3.com/ex-search?keywords=Benton+J+Broussard&military.conflict=World+War+II&general.title.content.doc-type=STORY_PAGE:Memorial&key=x2P6Q2Pf9jT7xREfcAyBRjJX
    //names
    public static final String NAME_OF_STRING_EXTRA = "imageText";
    public static final String STORIES_EXTRA = "searchResults";

    //permission requests
    public final int STORAGE_PERMISSION_REQUESTED = 0;
    public final int CAMERA_PERMISSION_REQUESTED = 1;

    //intent results
    public final int GET_TEXT_FOR_SEARCH = 4;
    public final int FIND_MATCH = 1;
    public final int TAKE_PICTURE = 2;
    public final int SELECT_IMAGE = 3;

    //presenter
    private MainPresenter presenter;

    //api
    public TessOCR mTessOCR;

    //components
    private ProgressBar progressBar;
    private Uri pictureUri;

    //AsyncTask Stuff
    public String resultOfTextToImage = "";
    public String textToSearch = "";
    public Story[] storiesFound;
    String filePath;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MainPresenter(this);


        FrameLayout frameLayout = new FrameLayout(this);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        //image
        AppCompatImageView imageView = new AppCompatImageView(this);
        imageView.setImageResource(R.drawable.sbts_starwall);

        //text
        MaterialTextView textView = new MaterialTextView(this, null, R.attr.textAppearanceHeadline5);
        textView.setText("Read the Stories of the WWII Fallen");
        textView.setGravity(Gravity.CENTER_HORIZONTAL);


        //Params for Buttons
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(24, 0, 24, 0);
        params.gravity = Gravity.CENTER_HORIZONTAL;

        //Buttons
        MaterialButton scanButton = new MaterialButton(this);
        scanButton.setText("Scan a Memorial");
        scanButton.setIconResource(R.drawable.ic_baseline_camera_alt_24); //need a RESOURCE if you want to use a vector drawable
        scanButton.setLayoutParams(params);
        scanButton.setOnClickListener((view)->{
            presenter.handleScanPressed();
        });

        MaterialButton selectImageButton = new MaterialButton(this, null, R.attr.materialButtonOutlinedStyle);
        selectImageButton.setText("Select Photo");
        selectImageButton.setIconResource(R.drawable.ic_baseline_insert_photo_24);
        selectImageButton.setLayoutParams(params);
        selectImageButton.setOnClickListener((view)->{
            presenter.handleSelectImagePressed();
        });

        MaterialButton enterDirectlyButton = new MaterialButton(this, null, R.attr.materialButtonOutlinedStyle);
        enterDirectlyButton.setText("Enter A Name");
        enterDirectlyButton.setIconResource(R.drawable.ic_baseline_edit_24);
        enterDirectlyButton.setLayoutParams(params);
        enterDirectlyButton.setOnClickListener((view) ->{
            presenter.handleEnterDirectlyPressed();
        });

        //progressBar
        progressBar = new ProgressBar(this);
        progressBar.setVisibility(View.INVISIBLE);
        FrameLayout.LayoutParams progressParams = new MaterialCardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        progressParams.gravity = (Gravity.CENTER_HORIZONTAL| Gravity.CENTER_VERTICAL);
        progressBar.setLayoutParams(progressParams);

        //FAB
        FloatingActionButton fab = new FloatingActionButton(this);
        fab.setImageResource(R.drawable.ic_baseline_help_24);
        FrameLayout.LayoutParams fabParams = new MaterialCardView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fabParams.gravity = (Gravity.RIGHT|Gravity.BOTTOM);
        fabParams.setMargins(0, 0, 48, 48);
        fab.setLayoutParams(fabParams);


        //add views to mainLayout
        mainLayout.addView(imageView);

        mainLayout.addView(textView);



        //Original Button Layout -- Removed temporarily
        //Right now we are going to only allow the user to search by entering a name, the image
        //      recognition options (scanButton and selectImageButton) will return once we have them
        //      working all the way.
//        mainLayout.addView(scanButton);
//
//        LinearLayout subLayout = new LinearLayout(this);
//        subLayout.setLayoutParams(params);
//        subLayout.addView(selectImageButton);
//        subLayout.addView(enterDirectlyButton);
//        mainLayout.addView(subLayout);


        //New layout with only enterDirectlyButton
        mainLayout.addView(enterDirectlyButton);


        //add views to frameLayout
        frameLayout.addView(mainLayout);
//        frameLayout.addView(fab); //TODO: Make the fab actually display something
        frameLayout.addView(progressBar);

        //set view
        setContentView(frameLayout);

        //TODO:tidy up...
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUESTED); //for storing bookmarked items and using the image to text AI todo: implement these features fully

        mTessOCR = new TessOCR(this, "eng");


        //TODO: remove later... just for testing
//        MaterialButton testButton = new MaterialButton(this);
//        testButton.setText("Test Story");
//        testButton.setOnClickListener((view)->{
//            Intent intent = new Intent(this, StoryActivity.class);
//            startActivity(intent);
//        });
//        mainLayout.addView(testButton);



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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void goToCamera() {
        //Check for camera permission first
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                //Prep to save picture
                String fileName = "WWIIFallenMemorial.jpg";

                File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);
                filePath = imageFile.getAbsolutePath();

                Uri imageUri = FileProvider.getUriForFile(
                    this,
                    "org.storiesbehindthestars.wwiifallenapp.provider",
                    imageFile
                );

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_PICTURE); }
            else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_REQUESTED);
            }
        }

        else { //if no permission, ask
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUESTED);
        }


    }


    @Override
    public void goToDirectEntry() {
//        Intent intent = new Intent(this, DirectEntryActivity.class);
//        intent.putExtra(NAME_OF_STRING_EXTRA, "");
//        startActivityForResult(intent, GET_TEXT_FOR_SEARCH);
        goToDirectEntry("");
    }

    @Override
    public void goToDirectEntry(String imageToTextResult){
        Intent intent = new Intent(this, DirectEntryActivity.class);
        intent.putExtra(NAME_OF_STRING_EXTRA, imageToTextResult);
        startActivityForResult(intent, GET_TEXT_FOR_SEARCH);
    }

    public void goToStories(Story[] stories){
        Intent intent = new Intent(this, StoriesActivity.class);
        intent.putExtra(STORIES_EXTRA, stories);
        startActivity(intent);
    }

    @Override
    public void goToPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
    }

    public void setResultOfTextToImage(String resultOfTextToImage) {
        this.resultOfTextToImage = resultOfTextToImage;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //FOR SELECT_IMAGE from file
        if (requestCode == SELECT_IMAGE && resultCode == Activity.RESULT_OK) {
            new ImageToTextAsyncTask().execute();
            this.pictureUri = data.getData();

//            try {
//                //solution from: https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureUri);
//                presenter.readImageText(bitmap); //TODO: Need to get Tess image-to-text working
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(this, "Unable to read image", Toast.LENGTH_SHORT).show();
//            }

//            goToDirectEntry(resultOfTextToImage);
        }

        //For TAKE_PICTURE from camera
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
            Uri pictureUri = Uri.parse(filePath);
            //TODO: Figure out how to properly get bitmap from picture taken
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), pictureUri);
//                presenter.readImageText(bitmap); //TODO: Replace this, incorporate async task
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "No text found in image. Try entering the name by hand instead.", Toast.LENGTH_SHORT).show();
            }
            goToDirectEntry(resultOfTextToImage);
        }

        //When it returns...
        if (requestCode == GET_TEXT_FOR_SEARCH && resultCode == Activity.RESULT_OK){
            String textForSearch = data.getStringExtra("result");
            textToSearch = textForSearch;
            new Fold3APIAsyncTask().execute();

//            goToStories(storiesFound); //have this in async task
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
        if (requestCode==CAMERA_PERMISSION_REQUESTED){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            presenter.handleScanPressed();
            }
            //TODO: display message
        }
    }


    //READ TEXT FROM IMAGE
    public class ImageToTextAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                //solution from: https://stackoverflow.com/questions/3879992/how-to-get-bitmap-from-an-uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(MainActivity.this.getContentResolver(), MainActivity.this.pictureUri);

                /*THIS IS WHERE ALL THE MAGIC HAPPENS...*/
                MainActivity.this.readImageText(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Unable to read image", Toast.LENGTH_LONG).show(); //sometimes causes an error - TODO
            }


            goToDirectEntry(MainActivity.this.resultOfTextToImage);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity.this.progressBar.setVisibility(View.INVISIBLE);

        }
    }


    public void readImageText(Bitmap bitmap){

        final String srcText = this.mTessOCR.getOCRResult(bitmap);

        if (srcText != null && !srcText.equals("")){    //if it found something
            setResultOfTextToImage(srcText);
        }
        else{   //if it's empty
            setResultOfTextToImage("");
            Toast.makeText(MainActivity.this, "Error reading image. Try entering the name by hand instead.", Toast.LENGTH_LONG).show();
        }

        mTessOCR.onDestroy();
    }



//    private void doOCR (final Bitmap bitmap) {
//        if (mProgressDialog == null) {
//            mProgressDialog = ProgressDialog.show(ocrView, "Processing",
//                    "Doing OCR...", true);
//        } else {
//            mProgressDialog.show();
//        }
//        new Thread(new Runnable() {
//            public void run() {
//                final String srcText = mTessOCR.getOCRResult(bitmap);
//                ocrView.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        if (srcText != null && !srcText.equals("")) {
//                            //srcText contiene el texto reconocido
//                        }
//                        mTessOCR.onDestroy();
//                        mProgressDialog.dismiss();
//                    }
//                });
//            }
//        }).start();
//    }






    //FOLD3 API
    private class Fold3APIAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.progressBar.setVisibility(View.VISIBLE);
        }

        private void setStoriesFound(Story[] stories){
            MainActivity.this.storiesFound = stories;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Story[] stories = MainActivity.this.presenter.searchStories(MainActivity.this.textToSearch);
                setStoriesFound(stories);
                MainActivity.this.goToStories(MainActivity.this.storiesFound);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //TODO: Search Fold3 Database with API
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MainActivity.this.progressBar.setVisibility(View.INVISIBLE);

        }
    }


    


    
//TODO: Remove. Rewrite majority in presenter (if possible)
//based on: https://codinginflow.com/tutorials/android/asynctask
//    private static class TessAsyncTask extends AsyncTask<Integer, Integer, String> {
//        private static final String TESSDATA = "tessdata";
//        private Bitmap bitmap;
//        private TessBaseAPI tessBaseApi;
//        private static final String lang = "eng";
//
//        private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString()+ "/TesseractSample/";
//        private static final String TAG = MainActivity.class.getSimpleName();
//
//        private WeakReference<MainActivity> activityWeakReference;
//
//        TessAsyncTask(MainActivity activity, Bitmap bitmap) {
//            activityWeakReference = new WeakReference<MainActivity>(activity);
//            this.bitmap = bitmap;
//        }
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            MainActivity activity = activityWeakReference.get();
//            if (activity == null || activity.isFinishing()) {
//                return;
//            }
//            activity.progressBar.setVisibility(View.VISIBLE);
//        }
//        @Override
//        protected String doInBackground(Integer... integers) {
//
//            //PUT ACTION HERE
//            prepareTesseract();
//            String result = extractText(bitmap);
//
//            MainActivity activity = activityWeakReference.get();
//            activity.resultOfTextToImage = result;
//            //TODO: What happens if there is no text found?
//
//
//            return "Finished!";
//        }
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//            MainActivity activity = activityWeakReference.get();
//            if (activity == null || activity.isFinishing()) {
//                return;
//            }
//            activity.progressBar.setProgress(values[0]);
//        }
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            MainActivity activity = activityWeakReference.get();
//            if (activity == null || activity.isFinishing()) {
//                return;
//            }
////            Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
//            activity.progressBar.setProgress(0);
//            activity.progressBar.setVisibility(View.INVISIBLE);
//        }
//
//        //TESS STUFF
//
//        // FROM: https://github.com/ashomokdev/Tess-two_example/blob/master/app/src/main/java/com/ashomok/tesseractsample/MainActivity.java
//
//        //    public void doOCR() {
//        //        prepareTesseract();
//        //        startOCR(outputFileUri);
//        //    }
//
//        /**
//         * Prepare directory on external storage
//         *
//         * @param path
//         * @throws Exception
//         */
//        private void prepareDirectory(String path) {
//
//            File dir = new File(path);
//            if (!dir.exists()) {
//                if (!dir.mkdirs()) {
//                    Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
//                }
//            } else {
//                Log.i(TAG, "Created directory " + path);
//            }
//        }
//
//
//        public void prepareTesseract() {
//            try {
//                prepareDirectory(DATA_PATH + TESSDATA);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            copyTessDataFiles(TESSDATA);
//        }
//
//        /**
//         * don't run this code in main thread - it stops UI thread. Create AsyncTask instead.
//         * http://developer.android.com/intl/ru/reference/android/os/AsyncTask.html
//         *
//         * @param imgUri
//         */
//    //    private void startOCR(Uri imgUri) {
//    //        try {
//    //            BitmapFactory.Options options = new BitmapFactory.Options();
//    //            options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
//    //            Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);
//    //
//    //            result = extractText(bitmap);
//    //            result = "testing! ...";
//    //
//    ////            textView.setText(result); TODO: THIS!
//    //
//    //        } catch (Exception e) {
//    //            Log.e(TAG, e.getMessage());
//    //        }
//    //    }
//
//
//        public String extractText(Bitmap bitmap) {
//            try {
//                tessBaseApi = new TessBaseAPI();
//            } catch (Exception e) {
//                Log.e(TAG, e.getMessage());
//                if (tessBaseApi == null) {
//                    Log.e(TAG, "TessBaseAPI is null. TessFactory not returning tess object.");
//                }
//            }
//
//            tessBaseApi.init(DATA_PATH, lang);  //TODO: for some reason it crashes here. It says that the file path does not exist.
//
//    //       //EXTRA SETTINGS
//    //        //For example if we only want to detect numbers
//    //        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
//    //
//    //        //blackList Example
//    //        tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]}{POIU" +
//    //                "YTRWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");
//
//            Log.d(TAG, "Training file loaded");
//            tessBaseApi.setImage(bitmap);
//            String extractedText = "empty result";
//            try {
//                extractedText = tessBaseApi.getUTF8Text();
//            } catch (Exception e) {
//                Log.e(TAG, "Error in recognizing text.");
//            }
//            tessBaseApi.end();
//            return extractedText;
//        }
//
//    public void copyTessDataFiles(String path) {
//        MainActivity activity = activityWeakReference.get();
//        try {
//            String fileList[] = activity.getAssets().list(path);
//
//            for (String fileName : fileList) {
//
//                // open file within the assets folder
//                // if it is not already there copy it to the sdcard
//                String pathToDataFile = DATA_PATH + path + "/" + fileName;
//                if (!(new File(pathToDataFile)).exists()) {
//
//                    InputStream in = activity.getAssets().open(path + "/" + fileName);
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
//    }



}

