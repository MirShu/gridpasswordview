package com.newstart.gridpasswordview.extend;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by shiyuankao on 2016/8/4.
 */
public class ImeDelBugFixedEditText extends CustomBoardEditText {

    public ImeDelBugFixedEditText(Context context) {
        super(context);
    }

    public ImeDelBugFixedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImeDelBugFixedEditText(Context context, AttributeSet attrs,int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnDelKeyEventListener {
        void onDeleteClick();
    }
}