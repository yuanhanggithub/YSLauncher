package com.ys.yslauncher;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class StatusbarRight extends LinearLayout {
    
    private ArrayList<View> mViews = new ArrayList<View>();
   
    private final int LEFT_BACKGROUND_ID = R.drawable.statusbar_l;
    private final int MIDDLE_BACKGROUND_ID = R.drawable.statusbar_m;
    private final int RIGHT_BACKGROUND_ID = R.drawable.statusbar_r;
    

    private final String MID_VIEW_TAG = "_mid";
    
    private LayoutInflater mInflater;
    private ImageView mLeftView ;

    public StatusbarRight(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        mLeftView = new ImageView(context, attrs);
        mLeftView.setImageResource(LEFT_BACKGROUND_ID);
//        View clock = mInflater.inflate(R.layout.digital_clock,null);
//        addViewForStatusbar(clock,null);
        setOrientation(HORIZONTAL);
    }
    
    private void addViewForLayout(){
        detachAllViewsFromParent();
        for(View childs:mViews){
            addView(childs);
        }
        invalidate();
    }
    
    private View createMiddleView(){
        ImageView v = new ImageView(getContext());
        v.setImageResource(MIDDLE_BACKGROUND_ID);
        return v;
    }
    
    public void addViewForStatusbar(View child, Object TAG){
        if(mViews.contains(child)){
            return;
        }
        if(mViews.size() == 0){
            mViews.add(mLeftView);
        }
        if(mViews.size() == 1){
            mViews.add(child);
            child.setBackgroundResource(RIGHT_BACKGROUND_ID);
        }else{
            mViews.add(1, child);
            child.setBackgroundResource(RIGHT_BACKGROUND_ID);
            View v = createMiddleView();
            if(TAG != null ){
                child.setTag(TAG);
                v.setTag(TAG + MID_VIEW_TAG);
            }
            mViews.add(2,v);
        }
        addViewForLayout();
    }
    public void setTagViewVisible(Object o , boolean isVisible){
        View v = findViewWithTag(o);
        View vm = findViewWithTag(o + MID_VIEW_TAG);
        if(v==null || vm==null){
            return; 
        }
        if(!isVisible){
            v.setVisibility(GONE);
            vm.setVisibility(GONE);
        }else{
            v.setVisibility(VISIBLE);
            vm.setVisibility(VISIBLE);
        }
    }

}
