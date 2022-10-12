package com.eighteengray.procamera.widget;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.eighteengray.procamera.R;



public class RippleView extends androidx.appcompat.widget.AppCompatButton {
	private float mDownX;
	private float mDownY;
	private float mAlphaFactor;
	private float mDensity;
	private float mRadius;
	private float mMaxRadius;

	private int mRippleColor;
	private boolean mIsAnimating = false;
	private boolean mHover = true;

	private RadialGradient mRadialGradient;
	private Paint mPaint;
	private ObjectAnimator mRadiusAnimator;

	private int dp(int dp)
	{
		return (int) (dp * mDensity + 0.5f);
	}

	public RippleView(Context context)
	{
		this(context, null);
	}

	public RippleView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	public RippleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RippleView);
		mRippleColor = a.getColor(R.styleable.RippleView_rippleColor,
				mRippleColor);
		mAlphaFactor = a.getFloat(R.styleable.RippleView_alphaFactor,
				mAlphaFactor);
		mHover = a.getBoolean(R.styleable.RippleView_hover, mHover);
		a.recycle();
	}

	public void init() {
		mDensity = getContext().getResources().getDisplayMetrics().density;

		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setAlpha(100);
		setRippleColor(Color.BLACK, 0.2f);
	}

	public void setRippleColor(int rippleColor, float alphaFactor) {
		mRippleColor = rippleColor;
		mAlphaFactor = alphaFactor;
	}

	public void setHover(boolean enabled) {
		mHover = enabled;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mMaxRadius = (float) Math.sqrt(w * w + h * h);
	}

	private boolean mAnimationIsCancel;
	private Rect mRect;

	@Override
	public boolean onTouchEvent(final MotionEvent event) {
		boolean superResult = super.onTouchEvent(event);
		if (event.getActionMasked() == MotionEvent.ACTION_DOWN && this.isEnabled() && mHover) {
			mRect = new Rect(getLeft(), getTop(), getRight(), getBottom());
			mAnimationIsCancel = false;
			mDownX = event.getX();
			mDownY = event.getY();

			mRadiusAnimator = ObjectAnimator.ofFloat(this, "radius", 0, dp(50)).setDuration(400);
			mRadiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
			mRadiusAnimator.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animator) {
					mIsAnimating = true;
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					setRadius(0);
					mIsAnimating = false;
				}

				@Override
				public void onAnimationCancel(Animator animator) {

				}

				@Override
				public void onAnimationRepeat(Animator animator) {

				}
			});
			mRadiusAnimator.start();
			if (!superResult) {
				return true;
			}
		} else if (event.getActionMasked() == MotionEvent.ACTION_MOVE && this.isEnabled() && mHover) {
			mDownX = event.getX();
			mDownY = event.getY();

			// Cancel the ripple animation when moved outside
			if (mAnimationIsCancel = !mRect.contains(
					getLeft() + (int) event.getX(),
					getTop() + (int) event.getY())) {
				setRadius(0);
			} else {
				setRadius(dp(50));
			}
			if (!superResult) {
				return true;
			}
		} else if (event.getActionMasked() == MotionEvent.ACTION_UP && !mAnimationIsCancel && this.isEnabled()) {
			mDownX = event.getX();
			mDownY = event.getY();

			final float tempRadius = (float) Math.sqrt(mDownX * mDownX + mDownY * mDownY);
			float targetRadius = Math.max(tempRadius, mMaxRadius);

			if (mIsAnimating) {
				mRadiusAnimator.cancel();
			}
			mRadiusAnimator = ObjectAnimator.ofFloat(this, "radius", dp(50), targetRadius);
			mRadiusAnimator.setDuration(500);
			mRadiusAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
			mRadiusAnimator.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animator) {
					mIsAnimating = true;
				}

				@Override
				public void onAnimationEnd(Animator animator) {
					setRadius(0);
					mIsAnimating = false;
				}

				@Override
				public void onAnimationCancel(Animator animator) {

				}

				@Override
				public void onAnimationRepeat(Animator animator) {

				}
			});
			mRadiusAnimator.start();
			if (!superResult) {
				return true;
			}
		}
		return superResult;
	}

	public int adjustAlpha(int color, float factor) {
		int alpha = Math.round(Color.alpha(color) * factor);
		int red = Color.red(color);
		int green = Color.green(color);
		int blue = Color.blue(color);
		return Color.argb(alpha, red, green, blue);
	}

	public void setRadius(final float radius) {
		mRadius = radius;
		if (mRadius > 0) {
			mRadialGradient = new RadialGradient(mDownX, mDownY, mRadius, adjustAlpha(mRippleColor, mAlphaFactor), mRippleColor, Shader.TileMode.MIRROR);
			mPaint.setShader(mRadialGradient);
		}
		invalidate();
	}

	private Path mPath = new Path();

	@Override
	protected void onDraw(final Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode()) {
			return;
		}

		canvas.save();

		mPath.reset();
		mPath.addCircle(mDownX, mDownY, mRadius, Path.Direction.CW);

		canvas.clipPath(mPath);
		canvas.restore();

		canvas.drawCircle(mDownX, mDownY, mRadius, mPaint);
	}

}
