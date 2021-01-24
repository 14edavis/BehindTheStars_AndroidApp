package org.storiesbehindthestars.wwiifallenapp.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import org.storiesbehindthestars.wwiifallenapp.R;
import org.storiesbehindthestars.wwiifallenapp.models.ApiResource;
import org.storiesbehindthestars.wwiifallenapp.models.Story;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

//based off of example component created by: https://github.com/dittonjs

public class MemorialView extends MaterialCardView {

    private MaterialButton viewButton;
    private FloatingActionButton fab;
    private AppCompatImageView backgroundImageView;
    private AppCompatImageView profileImageView;
    private MaterialTextView titleView;
//    private MaterialTextView descriptionView;
    private MaterialTextView contentsView;
    private FrameLayout header;
    private Story story;
    private boolean showFullPost = false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public MemorialView(Context context, Story story) {
        this(context, story,false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public MemorialView(Context context, Story story, boolean showFullPost){
        super(context);

        //set variables
        this.story = story;
        this.showFullPost = showFullPost;
//        setTag(post.id); TODO

        //Params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(48, 24, 48, 24);
        setLayoutParams(params);

        //Layout
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        header = new FrameLayout(context);
//        header.setOrientation(LinearLayout.VERTICAL);
        LinearLayout body = new LinearLayout(context);
        body.setPadding(64, 32, 64, 32);
        body.setOrientation(LinearLayout.VERTICAL);
        LinearLayout footer = new LinearLayout(context);

        mainLayout.addView(header);
        mainLayout.addView(body);
        mainLayout.addView(footer);

        addView(mainLayout);

        //Header
        backgroundImageView = new AppCompatImageView(context);
        profileImageView = new AppCompatImageView(context);
        FrameLayout.LayoutParams profileParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        profileParams.gravity = Gravity.LEFT;
        profileParams.setMargins(24, 24, 0,0);
        profileImageView.setLayoutParams(profileParams);

        displayImage();
        header.addView(backgroundImageView);
        header.addView(profileImageView);

        //Body
        titleView = new MaterialTextView(context, null, R.attr.textAppearanceHeadline6);
        titleView.setText(story.name);
        body.addView(titleView);

//        descriptionView = new MaterialTextView(context);
//        descriptionView.setText("blah blah blah...");
//        body.addView(descriptionView);
//        descriptionView.setTextSize(18);

        contentsView = new MaterialTextView(context);
        contentsView.setText(story.storyText);
        body.addView(contentsView);

        //Handling full vs partial post
        if (showFullPost) {
            contentsView.setTextSize(18);
        } else {
            contentsView.setMaxLines(3);
            contentsView.setEllipsize(TextUtils.TruncateAt.END);
        }
        LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentParams.setMargins(0, 12, 0, 0);
        contentsView.setLayoutParams(contentParams);

        // footer
        if (!showFullPost) {
            viewButton = new MaterialButton(context, null, R.attr.borderlessButtonStyle);
            viewButton.setText("Read");
            footer.addView(viewButton);
//        footer.setGravity(Gravity.RIGHT);
        }

        // FAB
        if (showFullPost) {
            //TODO: Eventually add an option to bookmark the memorial here
        }

        //Pull from web...
//        new TextLoaderAsyncTask().execute();
        new ImageLoaderAsyncTask().execute();


    }

    //TODO: Get displayImage working with URL image
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void displayImage() {
        //Header
//        if (post.pictureUri.equals("") && showFullPost) {
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 320);
            backgroundImageView.setImageResource(R.drawable.ic_baseline_image_640);
            header.setBackgroundColor(getResources().getColor(R.color.white, null));
            backgroundImageView.setLayoutParams(imageParams);
            backgroundImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            profileImageView.setImageResource(R.drawable.ic_baseline_person_outline_96);
    }

    //for testing, to force text, TODO fix later
    private void setText(String name, String story){
        titleView.setText(name);
        contentsView.setText(story);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setStory(Story story) {
        this.story = story;
        displayImage();
        titleView.setText(story.name);
//        descriptionView.setText(story.storyText);
        contentsView.setText(story.storyText);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        viewButton.setOnClickListener(l);
    }

    public void setFabOnClickListener(OnClickListener l) {
        if (fab != null) {
            fab.setOnClickListener(l);
        }
    }


    /** FOR LOADING AND SETTING TEXT **/
    class TextLoaderAsyncTask extends AsyncTask<URL, Void, String[]> {
        @Override
        protected String[] doInBackground(URL... urls){
            String[] result = new String[2]; //name and story
            try {
                URL url = new URL ("https://www.fold3.com/page/638791116/karol-a-bauer/stories");
                Scanner sc = new Scanner(url.openStream());
                StringBuffer sb = new StringBuffer();
                while(sc.hasNext()) {
                    sb.append(sc.next());
                    //System.out.println(sc.next());
                }
                String story = sb.toString();

                //Removing the HTML tags
                story = story.replaceAll("<[^>]*>", ""); //TODO: Place holder

                String name = "<NAME>"; //TODO: Place holder

                result[0] = name;
                result[1] = story;

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
                result[0] = "<NAME>";
                result[1] = "Error retrieving story";
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                result[0] = "<NAME>";
                result[1] = "Error retrieving story";
                return result;
            }
        }

        @Override
        protected void onPostExecute(String[] result) {
            MemorialView.this.titleView.setText(result[0]);
            MemorialView.this.contentsView.setText(result[1]);
        }
    }

    /** FOR LOADING AND SETTING IMAGES **/
    class ImageLoaderAsyncTask extends AsyncTask<URL, Void, Drawable[]> {
        @Override
        protected Drawable[] doInBackground(URL... urls) {
            InputStream content = null;
            InputStream content2 = null;

            try {
                URL url = new URL(MemorialView.this.story.profilePicURL); //"https://img.fold3.com/img/reference/STORY_PAGE/91243229?width=172&height=215&refresh=509");
                content = (InputStream) url.getContent();
                Drawable profileImage = Drawable.createFromStream(content, "src");

                URL url2 = new URL(MemorialView.this.story.backgroundPicURL); //"https://img.fold3.com/img/reference/BACKGROUND-IMAGE/91243229??refresh=941");
                content2 = (InputStream) url2.getContent();
                Drawable backgroundImage = Drawable.createFromStream(content2, "src");

                return new Drawable[]{profileImage, backgroundImage};

            } catch (IOException e) {
                e.printStackTrace();
                return null;
                }
        }

        @Override
        protected void onPostExecute(Drawable[] drawables) {
            if (drawables != null){
                MemorialView.this.profileImageView.setImageDrawable(drawables[0]);
                MemorialView.this.backgroundImageView.setImageDrawable(drawables[1]);
            }
        }
    }

}
