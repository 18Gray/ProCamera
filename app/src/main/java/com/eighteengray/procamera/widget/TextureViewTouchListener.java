package com.eighteengray.procamera.widget;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.eighteengray.procameralibrary.camera.TextureViewTouchEvent;
import org.greenrobot.eventbus.EventBus;




//TextureView的触摸事件：
// 轻按拍摄区域：做两个延时任务，第一个时间短，完成时发送点击event到fragment，fragment执行textureview的focusRegion，在其回调中调用judgeFocus。
// 分成正在聚焦、聚焦成功、失败等方法，发送聚焦event到fragment，fragment根据聚焦状态显示不同图标。
// 聚焦完成时，发送聚焦完成event给TouchListener，此时TouchListener才能触发滑动事件。

// 第二个时间长，完成时显示长按事件，进行af/ae锁定。
// 只有完成了上面聚焦和测光后，才能进行单指滑动。如果是向右下则进度环增加，否则减小，用于调节焦点白平衡。滑动后修改上面两个延时任务的标志位，似其不再执行。
public class TextureViewTouchListener implements View.OnTouchListener
{
    View parentView;
    Runnable runnable_short, runnable_long;

    public boolean isMoved = false;
    public boolean isFocused = false;
    public boolean isUp = false;

    float downX, downY;
    float rawX, rawY;
    float moveX, moveY;
    int touchSlop;



    //单指滑动，取横向纵向滑动最大距离，方向即为正负
    float maxChangeDistance;


    public TextureViewTouchListener(View parent)
    {
        this.parentView = parent;
        runnable_short = new Runnable()
        {
            @Override
            public void run()
            {
                if(!isMoved)
                {
                    postClickEvent(downX, downY, rawX, rawY);
                }
            }
        };
        runnable_long = new Runnable()
        {
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
    public boolean onTouch(View v, MotionEvent event)
    {
        isMoved = false;
        isUp = false;
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
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

                if(moveX != downX || moveY != downY)
                {
                    float absX = Math.abs(moveX - downX);
                    float abxY = Math.abs(moveY - downY);
                    if((absX - abxY > 0) && absX > touchSlop)
                    {
                        maxChangeDistance = moveX - downX;
                        isMoved = true;
                    }
                    else if((absX - abxY < 0) && abxY > touchSlop)
                    {
                        maxChangeDistance = moveY - downY;
                        isMoved = true;
                    }
                    if(isFocused)
                    {
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





    private void postClickEvent(float x, float y, float rawX, float rawY)
    {
        TextureViewTouchEvent.TextureClick textureClick = new TextureViewTouchEvent.TextureClick();
        textureClick.setX(x);
        textureClick.setY(y);
        textureClick.setRawX(rawX);
        textureClick.setRawY(rawY);
        EventBus.getDefault().post(textureClick);
    }


    private void postLongClickEvent(float x, float y)
    {
        TextureViewTouchEvent.TextureLongClick textureLongClick = new TextureViewTouchEvent.TextureLongClick();
        textureLongClick.setX(x);
        textureLongClick.setY(y);
        EventBus.getDefault().post(textureLongClick);
    }


    private void postDragEvent(float distance)
    {
        TextureViewTouchEvent.TextureOneDrag textureOneDrag = new TextureViewTouchEvent.TextureOneDrag();
        textureOneDrag.setDistance(distance);
        EventBus.getDefault().post(textureOneDrag);
    }


}
