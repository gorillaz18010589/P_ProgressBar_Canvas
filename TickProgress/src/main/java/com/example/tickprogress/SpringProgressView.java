package com.example.tickprogress;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/***
 * 自定义进度条
 *
 * @author spring sky Email:vipa1888@163.com 创建时间：2014-1-6下午3:28:51
 */
public class SpringProgressView extends View {

    /**
     * 分段颜色
     */

    private static final int[] SECTION_COLORS = {Color.rgb(191, 251, 151),
            Color.rgb(201, 246, 181), Color.rgb(148, 220, 110)};
    private static final int[] SECTION_COLORS1 = {Color.rgb(138, 212, 73),
            Color.rgb(140, 217, 75), Color.rgb(148, 220, 110)};

    /**
     * 进度条最大值
     */
    private float maxCount;
    /**
     * 进度条当前值
     */
    private float currentCount;
    /**
     * 画笔
     */
    private Paint mPaint, mPaint1, mPaint2, mPaint3;
    private int mWidth, mHeight;

    public SpringProgressView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public SpringProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SpringProgressView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();// 进度条
        mPaint1 = new Paint();
        mPaint2 = new Paint();

        mPaint.setAntiAlias(true);// 进度条
        mPaint1.setAntiAlias(true);
        mPaint2.setAntiAlias(true);

        int round = mHeight / 5;
        System.out.println("max=" + maxCount + "  current=" + currentCount);

        // 背景色
        mPaint2.setColor(0xFFc2c2c2);
        RectF rectBg = new RectF(0, 0, mWidth, mHeight);
        canvas.drawRoundRect(rectBg, round, round, mPaint2);

        // 背景色
        LinearGradient background = new LinearGradient(0, 0, 0, mHeight,
                new int[]{0xFFe6e6e6, 0xFFfbfbfb}, null,
                Shader.TileMode.MIRROR);
        mPaint1.setShader(background);
        RectF rectBlackBg = new RectF(2, 2, mWidth - 2, mHeight - 2);
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint1);

        // 进度条
        float section = currentCount / maxCount;
        RectF rectProgressBg_up = new RectF(2, 2, (mWidth - 2) * section,
                mHeight - 2);

        if (section <= 1.0f / 3.0f) {
            if (section != 0.0f) {
                mPaint.setColor(SECTION_COLORS[0]);
            } else {
                mPaint.setColor(Color.TRANSPARENT);
            }
        } else {
            int count = (section <= 1.0f / 3.0f * 2) ? 2 : 3;
            int[] colors = new int[count];
            System.arraycopy(SECTION_COLORS, 0, colors, 0, count);// 吧SECTION_COLORS的内容复制到colors中
            LinearGradient shader = new LinearGradient(3, 3, (mWidth - 3)
                    * section, mHeight - 3, colors, null,
                    Shader.TileMode.MIRROR);
            mPaint.setShader(shader);
        }

        canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint);

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
    }
}

