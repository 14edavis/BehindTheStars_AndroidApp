package org.storiesbehindthestars.wwiifallenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.storiesbehindthestars.wwiifallenapp.presenters.DirectEntryPresenter;

public class DirectEntryActivity extends AppCompatActivity implements DirectEntryPresenter.MVPView {

    DirectEntryPresenter presenter;
    TextInputEditText input;

    //Functions as both a Direct Input page and a Check Accuracy page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new DirectEntryPresenter(this);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        MaterialTextView titleTextView = new MaterialTextView(this, null, R.attr.textAppearanceHeadline5);
        titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);

        MaterialTextView promptTextView = new MaterialTextView(this);
        input = new TextInputEditText(this);
        input.setLines(7);
        input.setGravity(Gravity.START);

        MaterialButton okButton = new MaterialButton(this);
        okButton.setText("Find Memorial");
        okButton.setOnClickListener((view)->{
            presenter.handleOkButtonPressed(input.getText().toString());
        });

        mainLayout.addView(titleTextView);
        mainLayout.addView(promptTextView);
        mainLayout.addView(input);
        mainLayout.addView(okButton);

        setContentView(mainLayout);

        String text = getIntent().getStringExtra("imageText");
        if (text.equals("")){ //This is for direct entry
            titleTextView.setText("Enter Text From Memorial");
        }
        else{
            titleTextView.setText("Is This Correct?");
            input.setText(text);
        }

    }

    public void goBack(String inputText) {
        if (inputText == null || inputText.equals("")) {
            setResult(Activity.RESULT_CANCELED, null);
        } else {
            Intent intent = new Intent();
            intent.putExtra("result", inputText);
            setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }




}