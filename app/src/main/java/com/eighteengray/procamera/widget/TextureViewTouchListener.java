package com.eighteengray.procamera.widget;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.eighteengray.procameralibrary.camera.TextureViewTouchEvent;
import org.greenrobot.eventbus.EventBus;



public class TextureViewTouchListener implements View.OnTouchListener {
    View parentView;
    Runnable runnable_short, runnable_long; //两个延时任务，第一个时间短，代表单击事件； 第二个时间长，代表长按事件

    public boolean isMoved = false;
    public boolean isFocused = false;
    public boolean isUp = false;

    float downX, downY;
    float rawX, rawY;
    float moveX, moveY;
    int touchSlop;



    //单指滑动，取横向纵向滑动最大距离，方向即为正负
    float maxChangeDistance;


    public TextureViewTouchListener(View parent) {
        this.parentView = parent;
        runnable_short = new Runnable() {
            @Override
            public void run()
            {
                if(!isMoved)
                {
                    postClickEvent(downX, downY, rawX, rawY);
                }
            }
        };
        runnable_long = new Runnable() {
            @Override
            public void run()
            {
                if(!isMoved && !isUp)
                {
                    postLongClickEvent(downX, downY);
                }
            }
        };
        touchSlop = ViewConfiguration.getTouchSlop();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        isMoved = false;
        isUp = false;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                rawX = event.getRawX();
                rawY = event.getRawY();
                parentView.postDelayed(runnable_short, 200);
                parentView.postDelayed(runnable_long, 1000);
                break;

            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();

                if(moveX != downX || moveY != downY) {
                    float absX = Math.abs(moveX - downX);
                    float abxY = Math.abs(moveY - downY);
                    if((absX - abxY > 0) && absX > touchSlop) {
                        maxChangeDistance = moveX - downX;
                        isMoved = true;
                    }
                    else if((absX - abxY < 0) && abxY > touchSlop) {
                        maxChangeDistance = moveY - downY;
                        isMoved = true;
                    }
                    if(isFocused) {
                        postDragEvent(maxChangeDistance);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
                isUp = true;
                break;
        }
        return true;
    }


    private void postClickEvent(float x, float y, float rawX, float rawY) {
        TextureViewTouchEvent.TextureClick textureClick = new TextureViewTouchEvent.TextureClick();
        textureClick.setX(x);
        textureClick.setY(y);
        textureClick.setRawX(rawX);
        textureClick.setRawY(rawY);
        EventBus.getDefault().post(textureClick);
    }


    private void postLongClickEvent(float x, float y) {
        TextureViewTouchEvent.TextureLongClick textureLongClick = new TextureViewTouchEvent.TextureLongClick();
        textureLongClick.setX(x);
        textureLongClick.setY(y);
        EventBus.getDefault().post(textureLongClick);
    }


    private void postDragEvent(float distance) {
        TextureViewTouchEvent.TextureOneDrag textureOneDrag = new TextureViewTouchEvent.TextureOneDrag();
        textureOneDrag.setDistance(distance);
        EventBus.getDefault().post(textureOneDrag);
    }


}
