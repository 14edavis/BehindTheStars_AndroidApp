package org.storiesbehindthestars.wwiifallenapp.components;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;

import org.storiesbehindthestars.wwiifallenapp.R;

public class ReturnButton extends MaterialButton{
    public ReturnButton(@NonNull Activity context) {
        super(context, null, R.attr.borderlessButtonStyle);

        setText("Return");
        setIconResource(R.drawable.ic_baseline_arrow_back_24);

        LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = (Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        setLayoutParams(params);

        setOnClickListener((view) ->{
        context.finish();
    });

    }

}
