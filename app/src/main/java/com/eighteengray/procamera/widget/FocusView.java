package com.eighteengray.procamera.widget;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.eighteengray.procamera.R;



public class FocusView extends RelativeLayout
{
    Context context;
    LayoutInflater layoutInflater;
    ImageView iv_focus_camera, iv_arraw_awb;


    public FocusView(Context context)
    {
        super(context);
        this.context = context;
        init();
    }

    public FocusView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;
        init();
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }


    private void init()
    {
        layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.focusview, this);
        iv_focus_camera = (ImageView) findViewById(R.id.iv_focus_camera);
        iv_arraw_awb = (ImageView) findViewById(R.id.iv_arraw_awb);
    }


    public void showFocusing(float mRawX, float mRawY, final TextureViewTouchListener textureViewTouchListener)
    {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        if(width == 0)
        {
            width = 264;
        }
        if(height == 0)
        {
            height = 168;
        }

        ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(getLayoutParams());
        float newLeftMargin = mRawX - width / 2;
        float newTopMargin = mRawY - height / 2;
        margin.setMargins((int)(newLeftMargin ), (int)(newTopMargin ), 0, 0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(margin);
        setLayoutParams(layoutParams);

        setVisibility(VISIBLE);
        iv_arraw_awb.setVisibility(GONE);
        iv_focus_camera.setVisibility(View.VISIBLE);
        iv_focus_camera.setImageResource(R.mipmap.focusing);

        ScaleAnimation scaleAnimation = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        iv_focus_camera.startAnimation(scaleAnimation);
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setVisibility(GONE);
                textureViewTouchListener.isFocused = false;
            }
        }, 1000);
    }


    public void showFocusSucceed(final TextureViewTouchListener textureViewTouchListener)
    {
        setVisibility(VISIBLE);
        iv_arraw_awb.setVisibility(VISIBLE);
        iv_focus_camera.setVisibility(View.VISIBLE);
        iv_focus_camera.setImageResource(R.mipmap.focus_succeed);
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setVisibility(GONE);
                textureViewTouchListener.isFocused = true;
            }
        }, 5000);
    }


    public void showFocusFailed()
    {
        setVisibility(VISIBLE);
        iv_arraw_awb.setVisibility(GONE);
        iv_focus_camera.setVisibility(View.VISIBLE);
        iv_focus_camera.setImageResource(R.mipmap.focus_failed);
        postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                setVisibility(GONE);
            }
        }, 1000);
    }


    public void dragChangeAWB(float degree)
    {
        setVisibility(VISIBLE);
        iv_arraw_awb.setVisibility(VISIBLE);
        iv_focus_camera.setVisibility(View.VISIBLE);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(iv_arraw_awb, "rotation", 0, degree);
        float x = iv_focus_camera.getX();
        float y = iv_focus_camera.getY();
        float pivotX = x + iv_focus_camera.getMeasuredWidth() / 2;
        float pivotY = y + iv_focus_camera.getMeasuredHeight() / 2;
        iv_arraw_awb.setPivotX(pivotX);
        iv_arraw_awb.setPivotY(pivotY);
        objectAnimator.setDuration(1000);

        objectAnimator.addListener(new Animator.AnimatorListener()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
            }

            @Override
            public void onAnimationEnd(Animator animation)
            {
                postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        setVisibility(GONE);
                    }
                }, 3000);
            }

            @Override
            public void onAnimationCancel(Animator animation)
            {
            }

            @Override
            public void onAnimationRepeat(Animator animation)
            {
            }
        });

        objectAnimator.start();
    }


}
