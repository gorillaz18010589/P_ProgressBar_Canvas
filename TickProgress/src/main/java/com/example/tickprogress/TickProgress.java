package com.example.tickprogress;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class TickProgress extends ProgressBar {
    private static final long DURATION = 200;
    private int mViewWidth;
    private int mViewHeight;

    private int mStrokeWidth;

    private int mProgressWidth;

    private Paint mBgPaint;
    private Paint mProgressPaint;
    private Paint mMackTickPaint;


    private int mStrokeColor = Color.parseColor("#a3daff");//外邊框顏色

    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private ValueAnimator mAnimator;
    // 动画数值(用于控制动画状态,因为同一时间内只允许有一种状态出现,具体数值处理取决于当前状态)
    private float mAnimatorValue;


    public TickProgress(Context context) {
        this(context, null);
    }

    public TickProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Paint mStrokePaint;

    private int mHeight;

    public TickProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHeight = DisplayUtil.dip2px(context, 30); //進度條高度
        mStrokeWidth = DisplayUtil.dip2px(context, 1); //進度條邊框寬度
        mBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setStrokeWidth(mHeight);
        mBgPaint.setColor(Color.parseColor("#e6f6ff")); //背景顏色

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(mStrokeWidth);
        mStrokePaint.setColor(mStrokeColor);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStrokeWidth(mHeight);
        mProgressPaint.setStyle(Paint.Style.FILL);

        mMackTickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMackTickPaint.setStyle(Paint.Style.STROKE);
        mMackTickPaint.setStrokeWidth(mStrokeWidth);
        mMackTickPaint.setColor(mStrokeColor);

        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    private int mMackTickWidth;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewHeight = h;
        mViewWidth = w;
        mMackTickWidth = w / 100;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF rectF2 = new RectF(mStrokeWidth, mStrokeWidth, mViewWidth - mStrokeWidth, mHeight - mStrokeWidth);

        canvas.drawRoundRect(rectF2, mHeight / 2, mHeight / 2, mBgPaint);
        canvas.drawRoundRect(rectF2, mHeight / 2, mHeight / 2, mStrokePaint);


        RectF rectF1 = new RectF(mStrokeWidth / 2, mStrokeWidth / 2, mProgressWidth - mStrokeWidth / 2, mViewHeight - mStrokeWidth / 2);
        Path path = new Path();

        Path dstPath = new Path();
        PathMeasure pathMeasure = new PathMeasure();
        path.moveTo(0, 0);
        path.lineTo(mViewWidth, 0);
        pathMeasure.setPath(path, false);
        pathMeasure.getSegment(0, pathMeasure.getLength() * mAnimatorValue, dstPath, true);
        //渐变
        Shader mShader = new LinearGradient(mStrokeWidth / 2, mStrokeWidth / 2, mProgressWidth, mHeight - mStrokeWidth / 2, Color.parseColor("#27a6ff"), Color.parseColor("#099aff"), Shader.TileMode.REPEAT);
        //遮罩
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mProgressPaint.setShader(mShader);
        canvas.drawRect(rectF1, mProgressPaint);
        mProgressPaint.setXfermode(null);
        //刻度
        for (int i = 0; i < 120; i++) {
//            引數：起點的 x 座標，起點 y 座標，終點 x 座標，終點 y 座標，畫筆
            Log.v("hank","mMackTickWidth" + mMackTickWidth +"/mStrokeWidth:" + mStrokeWidth +"mMackTickWidth:" + mMackTickWidth +"/mViewHeight" + mViewHeight);
//            canvas.drawLine((i + 1) * mMackTickWidth, mStrokeWidth / 2, (i + 1) * mMackTickWidth, mViewHeight - mStrokeWidth / 2, mMackTickPaint);
            canvas.drawLine((i + 1) * mMackTickWidth, mStrokeWidth / 2, (i + 1) * mMackTickWidth, mViewHeight - mStrokeWidth / 2, mMackTickPaint);
        }
    }

    /**
     * 0--1
     */
    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        } else if (progress > 1) {
            progress = 1;
        }

        int delta = (int) (progress /0.1);
        Log.d("hank","delta:"+delta);
        delta = delta == 0 ? 1 : delta;

        mProgressWidth = (int) (progress * mViewWidth);
        mAnimator = ValueAnimator.ofFloat(0, progress).setDuration(delta * DURATION);

        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mAnimatorValue = (float) animation.getAnimatedValue();
                mProgressWidth = (int) (mAnimatorValue * mViewWidth);
                invalidate();
            }
        };
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.start();
    }
}
