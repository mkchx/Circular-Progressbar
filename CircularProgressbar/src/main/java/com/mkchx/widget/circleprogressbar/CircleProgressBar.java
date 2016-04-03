package com.mkchx.widget.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author Efthimis Charitonidis
 */
public class CircleProgressBar extends View {

    private ObjectAnimator mObjectAnimator;

    private RectF mRectF;
    private Paint mTextPaint, mPaintProgress, mPaintBackground;
    private int mProgressInnerColor, mProgressColor, mProgressTextColor;
    private float mStrokeWidth, mTextSize, mStartAngle = -90f, mAngle = 0f, mAnimAngle = 0f;
    private boolean mHideProgressValue, mAnimate = false;
    private int mWidth, mHeight;

    public CircleProgressBar(Context context) {
        super(context);

        if (mProgressColor == 0)
            mProgressColor = ContextCompat.getColor(context, R.color.colorAccent);

        if (mProgressTextColor == 0)
            mProgressTextColor = Color.DKGRAY;

        if (mTextSize == 0)
            mTextSize = getResources().getDimensionPixelSize(R.dimen.default_textSize);

        if (mProgressInnerColor == 0)
            mProgressInnerColor = Color.WHITE;

        if (mStrokeWidth == 0)
            mStrokeWidth = getResources().getDimensionPixelSize(R.dimen.default_strokeWidth);

        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, 0, 0);
        try {
            mProgressColor = a.getColor(R.styleable.CircleProgressBar_progressColor, ContextCompat.getColor(context, R.color.colorAccent));
            mProgressTextColor = a.getColor(R.styleable.CircleProgressBar_progressTextColor, Color.DKGRAY);
            mProgressInnerColor = a.getColor(R.styleable.CircleProgressBar_innerColor, Color.WHITE);
            mTextSize = a.getDimensionPixelSize(R.styleable.CircleProgressBar_progressTextSize, R.dimen.default_textSize);
            mHideProgressValue = a.getBoolean(R.styleable.CircleProgressBar_hideProgressValue, false);
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.CircleProgressBar_strokeDimension, R.dimen.default_strokeWidth);
        } finally {
            a.recycle();
        }

        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        if (!mHideProgressValue) {
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setColor(mProgressTextColor);
            mTextPaint.setTextSize(mTextSize);
        }

        mObjectAnimator = ObjectAnimator.ofFloat(this, "AnimAngle", mAnimAngle, 1.0f);
        mObjectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mObjectAnimator.setDuration(2500);

        mPaintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setColor(mProgressColor);
        mPaintProgress.setStrokeWidth(mStrokeWidth);

        mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBackground.setStyle(Paint.Style.FILL);
        mPaintBackground.setColor(mProgressInnerColor);
    }

    /**
     * update circle progress
     *
     * @param percent
     */
    public void updateProgress(float percent) {

        mAngle = calculateAngle(percent / 100f);

        if (mAnimate) {
            mAnimAngle = 0f;
            mObjectAnimator.start();
        } else {
            mAnimAngle = 1f;
            invalidate();
        }
    }

    /**
     * get the current anim angle
     *
     * @return
     */
    public float getAnimAngle() {
        return mAnimAngle;
    }

    /**
     * set animate progress
     *
     * @param animate
     */
    public void setAnimate(boolean animate) {
        mAnimate = animate;
    }

    /**
     * set current anim angle
     *
     * @param animAngle
     */
    public void setAnimAngle(float animAngle) {
        mAnimAngle = animAngle;
        invalidate();
    }

    /**
     * set progress stroke height
     *
     * @param dimen
     */
    public void setStrokeWidth(int dimen) {
        mStrokeWidth = getResources().getDimensionPixelSize(dimen);
    }

    /**
     * set progress text color
     *
     * @param res
     */
    public void setTextColor(int res) {
        mProgressTextColor = res;
    }

    /**
     * set progress color
     *
     * @param res
     */
    public void setProgressColor(int res) {
        mProgressColor = res;
    }

    /**
     * set progress inner color
     *
     * @param res
     */
    public void setInnerColor(int res) {
        mProgressInnerColor = res;
    }

    /**
     * set progress text size
     *
     * @param dimen
     */
    public void setTextSize(int dimen) {
        mTextSize = getResources().getDimensionPixelSize(dimen);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getWidth();
        mHeight = getHeight();

        if (mRectF == null) {

            float mRadius = calculateRadius(mWidth, mHeight);

            mRectF = new RectF(mWidth / 2 - mRadius, mHeight / 2 - mRadius, mWidth / 2 + mRadius, mHeight / 2 + mRadius);
        }

        drawCircleStroke(canvas);
        drawInnerCircle(canvas);

        if (!mHideProgressValue) {
            int xText = (mWidth / 2);
            int yText = (int) ((mHeight / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
            String text;

            if (mAnimate) {
                int val = (int) (mAnimAngle * 100);
                text = String.valueOf(val - (val % 1));
            } else {
                text = String.valueOf((int) calculatePercentage(mAngle));
            }

            canvas.drawText(text, xText, yText, mTextPaint);
        }
    }

    private void drawCircleStroke(Canvas canvas) {

        float angle = mAngle * mAnimAngle;

        canvas.drawArc(mRectF, mStartAngle, angle, false, mPaintProgress);
    }

    private void drawInnerCircle(Canvas canvas) {
        canvas.drawArc(mRectF, mStartAngle, 360, false, mPaintBackground);
    }

    private float calculateRadius(int width, int height) {
        return (((width > height) ? height : width) / 2) - mStrokeWidth;
    }

    private float calculateAngle(float percent) {
        return percent * 360f;
    }

    private float calculatePercentage(float angle) {
        return (angle / 360) * 100;
    }
}
