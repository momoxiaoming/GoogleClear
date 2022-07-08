package com.mckj.sceneslib.ui.scenes.model.tools.newtools.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author Tom.wu
 * @Description:
 * @Package: com.wjm.view.progress
 * @data 2022/3/23 10:37
 */
public class VerticalSeekBar extends androidx.appcompat.widget.AppCompatSeekBar{
    private ProgressChangedListener progressChangedListener;
    public VerticalSeekBar(Context context) {
        super(context);
    }
    public VerticalSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public VerticalSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(h, w, oldh, oldw);
    }
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension(getMeasuredHeight(), getMeasuredWidth());
    }
    protected void onDraw(Canvas c) {
        //将SeekBar转转90度
        c.rotate(-90);
        //将旋转后的视图移动回来
        c.translate(-getHeight(), 0);
        super.onDraw(c);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnabled()) {
            return false;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                int i = 0;
                //获取滑动的距离
                i = getMax() - (int) (getMax() * event.getY() / getHeight());
                //设置进度
                setProgress(i);
                if (progressChangedListener != null) {
                    progressChangedListener.onProgressChanged(getProgress());
                }
                onSizeChanged(getWidth(), getHeight(), 0, 0);
            case MotionEvent.ACTION_UP:

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
    public void setOnProgressChanged(ProgressChangedListener progressChangedListener) {
        this.progressChangedListener = progressChangedListener;
    }
    public interface ProgressChangedListener {
        void onProgressChanged(int progress);
    }
}