package com.example.tickprogress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class SpringProgressView2 extends View {

    /** 进度条最大值 */
    private float maxCount;
    /** 进度条当前值 */
    private float currentCount;
    /** 画笔 */
    private Paint mPaint1, mPaint2, mPaint3, mPaint4;
    private int mWidth, mHeight;
    /**
     *
     * 第一个斜线的起始位置
     */
    private float first_diagonal_line;
    /**
     *
     * 第一个斜线的底边最左端
     */
    private float first_diagonal_line_left;
    /**
     *
     * 每条斜线之间的距离
     */
    private float dis_line;
    /**
     *
     * 每条斜線的寬度
     */
    private float w_line;
    Canvas canvas;
    /**
     * 矩形圆角
     */
    private int round;
    /**
     * 进度条百分率
     */
    private float section;
    /**
     * 设置几条斜线
     */
    private int num;
    private float b;

    public SpringProgressView2(Context context, AttributeSet attrs,
                               int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SpringProgressView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpringProgressView2(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
    }

    // 初始化各种
    private void init() {
        mPaint1 = new Paint();
        mPaint2 = new Paint();
        mPaint3 = new Paint();
        mPaint4 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint2.setAntiAlias(true);
        mPaint3.setAntiAlias(true);
        mPaint4.setAntiAlias(true);

        mPaint1.setColor(0xffe6e3e1);
        // mPaint2.setColor(0xffe0dcda);
        mPaint4.setColor(0xfff3c45e);
        mPaint3.setColor(0xffd89c19);

        mPaint3.setStrokeWidth(1);// 斜线画笔的粗度1像素

        // 可以更改螺纹宽度等等
        round = mHeight / 5;
        first_diagonal_line = mWidth / 40;
        first_diagonal_line_left = first_diagonal_line - mWidth / 80;
        dis_line = mWidth / 24;
        w_line = mWidth / 48;
        section = currentCount / maxCount;
        // section =(float) 0.02;
        // 设置画出来几个斜线
        num = (int) ((section * (mWidth - 2) - first_diagonal_line) / (w_line + dis_line));

        System.out.println("num:" + num);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        init();

        System.out.println("max=" + maxCount + "  current=" + currentCount);

        // 背景黑边儿
        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, round, round, mPaint2);

        // 灰色背景
        RectF rectBlackBg = new RectF(2, 2, mWidth - 2, mHeight - 2);
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint1);
        // 浅色 进度条
        RectF rectProgressBg_up = new RectF(2, 2, (mWidth - 2) * section,
                mHeight - 2);
        // canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint4);
        /*
         * 当进度条小于第一条斜线最右短，不显示斜线 当大于第一条进度后开始画线 主要是线的宽度 和 两条线之间的宽度的计算。
         */

        if ((mWidth - 2) * section <= first_diagonal_line + w_line + mWidth
                / 80) {
            System.out.println("---------------");
            canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint4);
        }

        else {
            canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint4);

//			double a = ((mWidth - 2) * section - first_diagonal_line)
//					% (w_line + dis_line);
            for (float k = first_diagonal_line; k <= section * (mWidth - 2)
                    - mWidth/30 ; k = k + dis_line + w_line) {
                diagonal_line(k);
//				b = k;
            }
//			if (a < w_line + 4) {
//
//			} else {
//
//				diagonal_line(b);
//
//			}
        }

    }

    // 画一条斜线
    private void diagonal_line(float i) {

        System.out.println("----------画了一条线-------------------");

        for (float j = i; j <= i + w_line; j++) {
            canvas.drawLine(j, 2, j - mWidth / 80, mHeight - 3, mPaint3);

        }
    }

    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /***
     * 设置最大的进度值
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /***
     * 设置当前的进度值
     *
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    public float getMaxCount() {
        return maxCount;
    }

    public float getCurrentCount() {
        return currentCount;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY
                || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        if (heightSpecMode == MeasureSpec.AT_MOST
                || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(15);
        } else {
            mHeight = heightSpecSize;
        }
        setMeasuredDimension(mWidth, mHeight);
        System.out.println("mWidth:" + mWidth + "mHeight:" + mHeight);
    }

}






