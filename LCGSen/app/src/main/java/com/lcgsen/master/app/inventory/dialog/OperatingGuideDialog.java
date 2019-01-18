package com.lcgsen.master.app.inventory.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.xujiaji.happybubble.BubbleDialog;
import com.lcgsen.master.app.inventory.R;
import com.lcgsen.master.app.inventory.helper.BubbleCreator;

public class OperatingGuideDialog extends BubbleDialog {

    private TextView mTextView;

    public OperatingGuideDialog(Context context) {
        super(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_operating_guide, null);
        mTextView = rootView.findViewById(R.id.content);

        addContentView(rootView);
        setBubbleLayout(BubbleCreator.getAmber((Activity) context));

        Window window = getWindow();
        if (window != null)
            window.setWindowAnimations(R.style.dialogWindowAnim);
    }

    public OperatingGuideDialog setText(String text) {
        mTextView.setText(text);
        return this;
    }
}
