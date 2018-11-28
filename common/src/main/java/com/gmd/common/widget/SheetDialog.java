package com.gmd.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.*;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import com.gmd.common.R;

/**
 * Created by forsven on 15/8/12.
 */
public class SheetDialog extends Dialog {
    public final static int STYLE_SLIDE_UP = 0;
    public final static int STYLE_SLIDE_DOWN = 1;

    private int mLayoutHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    private boolean mRunShowAnimation = false;
    private ContainerFrameLayout mContainer;
    private View mContentView;
    private Animation mAnimation;
    private int mDuration;
    private int animationStyle = STYLE_SLIDE_UP;

    private boolean mCancelable = true;
    private boolean mCancelableOnTouch = true;
    private final Handler mHandler = new Handler();
    private final Runnable mDismissAction = new Runnable() {
        public void run() {
            //dirty fix for java.lang.IllegalArgumentException: View not attached to window manager
            try {
                SheetDialog.super.dismiss();
            } catch (IllegalArgumentException ex) {
            }
        }
    };

    public SheetDialog(Context context) {
        this(context, R.style.BottomSheetDialog);
    }

    public SheetDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layout.height = ViewGroup.LayoutParams.MATCH_PARENT;
        layout.windowAnimations = R.style.DialogNoAnimation;
        getWindow().setAttributes(layout);

        mContainer = new ContainerFrameLayout(getContext());
        setContentView(mContainer);
        mDuration = 200;
    }

    public void setDuration(int duration) {
        this.mDuration = duration;
    }

    public void setAnimationStyle(int style) {
        this.animationStyle = style;
    }

    public void setCustomContentView(View v) {
        mContentView = v;
        mContainer.removeAllViews();
        mContainer.addView(v);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        mCancelableOnTouch = cancel;
    }

    @Override
    public void setCancelable(boolean flag) {
        super.setCancelable(flag);
        mCancelable = flag;
    }

    @Override
    public void dismiss() {
        if (mContentView != null) {
            mAnimation = new SlideAnimation(animationStyle == STYLE_SLIDE_UP ? mContentView.getTop() : 0, animationStyle == STYLE_SLIDE_UP ? mContainer.getMeasuredHeight() : -mContainer.getMeasuredHeight());
            mAnimation.setDuration(mDuration);
            mAnimation.setInterpolator(new AccelerateInterpolator());
            mAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mCancelableOnTouch = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mCancelableOnTouch = true;
                    mAnimation = null;
                    mHandler.post(mDismissAction);
                }
            });
            mContentView.startAnimation(mAnimation);
        } else {
            mHandler.post(mDismissAction);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mContentView != null) {
            mRunShowAnimation = true;
            mContainer.requestLayout();
        }
    }

    private class ContainerFrameLayout extends FrameLayout {

        private boolean mClickOutside = false;
        private int mChildTop = -1;

        public ContainerFrameLayout(Context context) {
            super(context);
            setBackgroundResource(R.color.transparent_black);
        }

        public void setChildTop(int top) {
            mChildTop = top;
            View child = getChildAt(0);
            if (child != null)
                child.offsetTopAndBottom(mChildTop - child.getTop());
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);

            View child = getChildAt(0);
            if (child != null) {
                switch (mLayoutHeight) {
                    case ViewGroup.LayoutParams.WRAP_CONTENT:
                        child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST));
                        break;
                    case ViewGroup.LayoutParams.MATCH_PARENT:
                        child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY));
                        break;
                    default:
                        child.measure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST));
                        break;
                }
            }
            setMeasuredDimension(widthSize, heightSize);
        }

        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
            View child = getChildAt(0);
            int start = 0;
            int end = 0;
            if (child != null) {
                switch (animationStyle) {
                    case STYLE_SLIDE_UP:
                        if (mChildTop < 0)
                            mChildTop = bottom - top;

                        start = mChildTop < 0 ? getHeight() : child.getTop() <= 0 ? mContainer.getHeight() : child.getTop();
                        end = mContainer.getHeight() - mContentView.getMeasuredHeight();
                        break;
                    case STYLE_SLIDE_DOWN:
                        mChildTop = 0;
                        start = -mContainer.getHeight();
                        end = 0;
                        break;
                }
                child.layout(0, mChildTop, child.getMeasuredWidth(), Math.max(bottom - top, mChildTop + child.getMeasuredHeight()));

                if (mRunShowAnimation) {
                    mRunShowAnimation = false;

                    if (mAnimation != null) {
                        mAnimation.cancel();
                        mAnimation = null;
                    }

                    if (start != end) {
                        mAnimation = new SlideAnimation(start, end);
                        mAnimation.setDuration(mDuration);
                        mAnimation.setInterpolator(new DecelerateInterpolator());
                        mContentView.startAnimation(mAnimation);
                    }
                }
            }
        }

        private boolean isOutsideDialog(float x, float y) {
            if (y < mChildTop)
                return true;

            View child = getChildAt(0);
            if (child != null && y > mChildTop + child.getMeasuredHeight())
                return true;

            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            boolean handled = super.onTouchEvent(event);

            if (handled)
                return true;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (isOutsideDialog(event.getX(), event.getY())) {
                        mClickOutside = true;
                        return true;
                    }
                    return false;
                case MotionEvent.ACTION_MOVE:
                    return mClickOutside;
                case MotionEvent.ACTION_CANCEL:
                    mClickOutside = false;
                    return false;
                case MotionEvent.ACTION_UP:
                    if (mClickOutside && isOutsideDialog(event.getX(), event.getY())) {
                        mClickOutside = false;
                        if (mCancelable && mCancelableOnTouch) {
                            dismiss();
                        }
                        return true;
                    }
                    return false;
            }

            return false;
        }
    }

    private class SlideAnimation extends Animation {

        int mStart;
        int mEnd;

        public SlideAnimation(int start, int end) {
            mStart = start;
            mEnd = end;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            int top = Math.round((mEnd - mStart) * interpolatedTime + mStart);
            if (mContainer != null) {
                mContainer.setChildTop(top);
            } else {
                cancel();
            }
        }
    }

}


