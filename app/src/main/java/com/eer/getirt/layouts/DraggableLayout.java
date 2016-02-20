package com.eer.getirt.layouts;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;


/**
 * Created by Ergun on 20.02.2016.
 */
public class DraggableLayout extends LinearLayout {

    private float firstPosY;

    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;



    public DraggableLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        mPosY = getTop();
        firstPosY = getTop();
    }

    public DraggableLayout(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        firstPosY = getTop();
    }

    public DraggableLayout(Context context){
        super(context);
        firstPosY = getTop();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        // More to come here later...
        final int action = ev.getAction();
        switch(action){
            case MotionEvent.ACTION_DOWN: {
                final float y = ev.getY();
                mLastTouchY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                final float y = ev.getY();
                final float dy = y - mLastTouchY;
                mPosY += dy;
                params.setMargins(getLeft(), (int)mPosY, getRight(), getBottom());
                setLayoutParams(params);
                bringToFront();
            }
        }
        return true;
    }
}
