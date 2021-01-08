package org.storiesbehindthestars.wwiifallenapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.storiesbehindthestars.wwiifallenapp.presenters.DirectEntryPresenter;

public class DirectEntryActivity extends AppCompatActivity implements DirectEntryPresenter.MVPView {

    TextInputEditText input;


    //Functions as both a Direct Input page and a Check Accuracy page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        MaterialTextView promptTextView = new MaterialTextView(this);
        input = new TextInputEditText(this);
        input.setLines(7);
        input.setGravity(Gravity.START);

        MaterialButton okButton = new MaterialButton(this);
        okButton.setText("OK");

        mainLayout.addView(promptTextView);
        mainLayout.addView(input);
        mainLayout.addView(okButton);

        setContentView(mainLayout);
    }
}