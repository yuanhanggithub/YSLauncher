package com.ys.yslauncher;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class StatusBar extends RelativeLayout {
        
    private ImageView mHome;
    private final int [] HOME_ID = {
            R.drawable.shome,
            R.drawable.home,
    };


    public StatusBar(Context context) {
        super(context);
    }
    
    public StatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);        
    }
    
    public void zoomed(boolean sel){
        if(sel){
            mHome.setImageResource(HOME_ID[0]);
        }else{
            mHome.setImageResource(HOME_ID[1]);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHome = (ImageView)findViewById(R.id.home);
        mHome.setImageResource(HOME_ID[0]);
        mHome.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setClassName("yslauncher.ys.com.yslauncher", "yslauncher.ys.com.yslauncher.MainActivity");
                getContext().startActivity(intent);
            }
        });

    }


}
