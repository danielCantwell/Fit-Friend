package com.cantwellcode.athletejournal;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Daniel on 3/18/14.
 */
public class SmallDecimalTextView extends TextView {

    public SmallDecimalTextView(Context context) {
        super(context);
    }

    public SmallDecimalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmallDecimalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        int decimalIndex = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '.') {
                decimalIndex = i;
                break;
            }
        }

        if (decimalIndex != 0) {
            Spannable span = new SpannableString(text);
            span.setSpan(new RelativeSizeSpan(0.7f), decimalIndex + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            super.setText(span, type);
        }

        else {
            super.setText(text, type);
        }
    }
}
