package org.storiesbehindthestars.wwiifallenapp.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
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
import java.net.HttpURLConnection;
import java.net.URL;

//based off of example component created by: https://github.com/dittonjs

public class MemorialView extends MaterialCardView {

    private MaterialButton viewButton;
    private FloatingActionButton fab;
    private AppCompatImageView imageView;
    private MaterialTextView titleView;
//    private MaterialTextView descriptionView;
    private MaterialTextView contentsView;
    private LinearLayout header;
    private Story memorial;
    private boolean showFullPost = false;


//    public MemorialView(Context context) {
//        this(context, false);
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public MemorialView(Context context, ApiResource webLinkToStory, boolean showFullPost){
        super(context);

//        this.post = post; TODO
        this.showFullPost = showFullPost;
//        setTag(post.id); TODO
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(48, 24, 48, 24);
        setLayoutParams(params);
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        header = new LinearLayout(context);
        header.setOrientation(LinearLayout.VERTICAL);
        LinearLayout body = new LinearLayout(context);
        body.setPadding(64, 32, 64, 32);
        body.setOrientation(LinearLayout.VERTICAL);
        LinearLayout footer = new LinearLayout(context);

        mainLayout.addView(header);
        mainLayout.addView(body);
        mainLayout.addView(footer);

        addView(mainLayout);
        imageView = new AppCompatImageView(context);

//        new Thread(() -> {
            displayImage();
//        }).start();


        header.addView(imageView);


        //Body

        titleView = new MaterialTextView(context, null, R.attr.textAppearanceHeadline6);
        titleView.setText("Title"); //TODO
        body.addView(titleView);

//        descriptionView = new MaterialTextView(context);
//        descriptionView.setText("blah blah blah..."); //TODO
//        body.addView(descriptionView);
//        descriptionView.setTextSize(18);

        contentsView = new MaterialTextView(context);
        contentsView.setText("main content"); //TODO
        body.addView(contentsView);
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
    }

    //TODO: Get displayImage working with URL image
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void displayImage() {
        //Header
//        if (post.pictureUri.equals("") && showFullPost) {
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 320);
            imageView.setImageResource(R.drawable.ic_baseline_image_640);
            header.setBackgroundColor(getResources().getColor(R.color.white, null));
            imageView.setLayoutParams(imageParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            //TODO: If image available
//        } else if(!post.pictureUri.equals("")) {
//            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 560);
//            imageView.setImageURI(Uri.parse(post.pictureUri));
//            imageView.setLayoutParams(imageParams);
//            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        }
    }


    Bitmap bitmapFromUrl(String url) throws java.net.MalformedURLException, java.io.IOException {
    //see:https://stackoverflow.com/questions/3375166/android-drawable-images-from-url
        HttpURLConnection connection = (HttpURLConnection)new URL(url) .openConnection();
        connection.setRequestProperty("User-agent","Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        return BitmapFactory.decodeStream(input);
    }

    //for testing, to force text, TODO fix later
    public void setText(String name, String story){
        titleView.setText(name);
        contentsView.setText(story);
    }

    //TODO Incorporate a Memorial object
//    public void setBlogPost(BlogPost post) {
//        this.post = post;
//        displayImage();
//        titleView.setText(post.title);
//        descriptionView.setText(post.description);
//        contentsView.setText(post.contents);
//    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        viewButton.setOnClickListener(l);
    }

    public void setFabOnClickListener(OnClickListener l) {
        if (fab != null) {
            fab.setOnClickListener(l);
        }
    }


}
