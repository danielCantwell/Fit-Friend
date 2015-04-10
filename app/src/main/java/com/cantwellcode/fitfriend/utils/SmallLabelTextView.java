package com.cantwellcode.fitfriend.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by danielCantwell on 3/24/15.
 */
public class SmallLabelTextView extends TextView {

    public SmallLabelTextView(Context context) {
        super(context);
    }

    public SmallLabelTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallLabelTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private CharSequence label = "";

    @Override
    public void setText(CharSequence text, BufferType type) {

        int initLength = text.length();

        String t = (String) text;
        t += label;

        Spannable span = new SpannableString(t);
        span.setSpan(new RelativeSizeSpan(0.6f), initLength, t.length(), 0);
        span.setSpan(new StyleSpan(Typeface.ITALIC), initLength, t.length(), 0);
//        span.setSpan(new ForegroundColorSpan(Color.BLACK), initLength, t.length(), 0);
        super.setText(span, type);

    }

    public void setTextWithLabel(String text, String label) {
        this.label = label;
        setText(text);
    }
}