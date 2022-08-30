package com.yallina.myapplication.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

@SuppressLint({"AppCompatCustomView", "SoonBlockedPrivateApi"})
public class BaseEditText extends EditText {
    private static java.lang.reflect.Field mParent;

    static {
        try {
            mParent = View.class.getDeclaredField("mParent");
            mParent.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public BaseEditText(Context context) {
        super(context.getApplicationContext());
    }

    public BaseEditText(Context context, AttributeSet attrs) {
        super(context.getApplicationContext(), attrs);
    }

    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context.getApplicationContext(), attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public BaseEditText(Context context, AttributeSet attrs, int defStyleAttr,
                        int defStyleRes) {
        super(context.getApplicationContext(), attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            if (mParent != null)
                mParent.set(this, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        super.onDetachedFromWindow();
    }
}
