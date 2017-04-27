package com.eighteengray.procamera.widget;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.eighteengray.commonutillibrary.ViewUtils;
import com.eighteengray.procameralibrary.camera.TextureViewTouchEvent;
import org.greenrobot.eventbus.EventBus;




//TextureView的触摸事件。轻按拍摄区域，显示焦点，完成聚焦和测光。
//长按进行af/ae锁定。
// 单指滑动，如果是向右下则进度环增加，否则减小，用于调节焦点白平衡。
// 两手指拖动则完成焦距调节。
public class TextureViewTouchListener implements View.OnTouchListener
{
    int mode;
    public static final int CLICK = 1;
    public static final int LongClick = 2; //前两者，都没有move，按下时间长短决定哪个
    public static final int OnePointDrag = 3;
    public static final int TwoPointDrag = 4;//都有移动move，点数量不同

    float downX, downY;
    float moveX, moveY;
    float upX, upY;
    float focusX, focusY;

    long downTime;
    long upTime;

    int touchSlop;
    //单指滑动，取横向纵向滑动最大距离，方向即为正负
    float maxChangeDistance;

    float downDistance;
    float moveDistance;
    //两指滑动，取缩放比
    float scale;



    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        touchSlop = ViewConfiguration.getTouchSlop();

        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            //单指触摸
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                downTime = event.getDownTime();
                break;

            //两指触摸
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = TwoPointDrag;
                downDistance = ViewUtils.getDistance(event);
                break;

            //手指移动
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX();
                moveY = event.getY();

                //说明手指移动了
                if(moveX != downX || moveY != downY)
                {
                    //OnePointDrag模式
                    if(mode != TwoPointDrag)
                    {
                        mode = OnePointDrag;
                        float absX = Math.abs(moveX - downX);
                        float abxY = Math.abs(moveY - downY);
                        if((absX - abxY > 0) && absX > touchSlop)
                        {
                            maxChangeDistance = moveX - downX;
                        }
                        else if((absX - abxY < 0) && abxY > touchSlop)
                        {
                            maxChangeDistance = moveY - downY;
                        }
                        TextureViewTouchEvent.TextureOneDrag textureOneDrag = new TextureViewTouchEvent.TextureOneDrag();
                        textureOneDrag.setDistance(maxChangeDistance);
                        EventBus.getDefault().post(textureOneDrag);
                    }
                    //TwoPointDrag模式
                    else
                    {
                        moveDistance = ViewUtils.getDistance(event);
                        scale = moveDistance / downDistance;
                        TextureViewTouchEvent.TextureTwoDrag textureTwoDrag = new TextureViewTouchEvent.TextureTwoDrag();
                        textureTwoDrag.setScale(scale);
                        EventBus.getDefault().post(textureTwoDrag);
                    }
                }
                break;

            //单指抬起
            case MotionEvent.ACTION_UP:
                upX = event.getX();
                upY = event.getY();
                upTime = event.getEventTime();

                //点没有挪动过
                if(downX == upX && downY == upY)
                {
                    //CLICK模式
                    if((upTime - downTime) < 1000)
                    {
                        mode = CLICK;
                        TextureViewTouchEvent.TextureClick textureClick = new TextureViewTouchEvent.TextureClick();
                        textureClick.setX(downX);
                        textureClick.setY(downY);
                        textureClick.setRawX(event.getRawX());
                        textureClick.setRawY(event.getRawY());
                        EventBus.getDefault().post(textureClick);
                    }
                    //LongClick模式
                    else
                    {
                        mode = LongClick;
                        TextureViewTouchEvent.TextureLongClick textureLongClick = new TextureViewTouchEvent.TextureLongClick();
                        textureLongClick.setX(downX);
                        textureLongClick.setY(downY);
                        EventBus.getDefault().post(textureLongClick);
                    }
                }
                break;

            //两指抬起
            case MotionEvent.ACTION_POINTER_UP:

                break;

        }
        return true;
    }

}
