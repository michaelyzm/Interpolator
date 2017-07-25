package com.noodlefish.noodlefishinterceptor.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;

import com.noodlefish.noodlefishinterceptor.interpolators.CubicBezierInterpolator;

/**
 * Created by zhoyu on 7/25/2017.
 */

public class InterpolatorPreviewView extends View {

    CubicBezierInterpolator interpolator = null;
    private final int padding = 200;
    Paint paint = new Paint();
    private OnInterpolatorChangeListener interpolatorChangedListener = null;

    public InterpolatorPreviewView(Context context) {
        super(context);
    }

    public InterpolatorPreviewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterpolatorPreviewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setInterpolator(CubicBezierInterpolator interpolator)
    {
        this.interpolator = interpolator;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            float x = event.getX();
            float y = getHeight() - event.getY();
            if(x < padding)
            {
                x = padding;
            }
            else if(x > getWidth() - padding)
            {
                x = getWidth() - padding;
            }
            x = (x - padding) / (getWidth() - 2*padding);
            y -= padding;
            y = y / (getHeight() - 2*padding);

            if(x <= 0.5)
            {
                interpolator.setX0Y0(x, y);
            }
            else
            {
                interpolator.setX1Y1(x, y);
            }
            if(interpolatorChangedListener != null)
            {
                interpolatorChangedListener.onInterpolatorChanged(interpolator);
            }
            invalidate();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        long startTime = System.currentTimeMillis();
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int size = Math.min(width, height);
        size -= 2*padding;

        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(0, 0, 0, height, paint);

        canvas.drawLine(padding, height-padding, width - padding, height-padding, paint);
        canvas.drawLine(padding, height - padding - size, padding, height - padding, paint);
        float x = padding, y = height - padding;
        float prex = x, prey = y;
        for(int i = 0; i < size; ++i)
        {
            float xt = i + x;
            float yt = interpolator.getInterpolation((float)i/size) * size;
            yt = y - yt;
            canvas.drawLine(prex, prey, xt, yt, paint);
            prex = xt;
            prey = yt;
        }
        canvas.drawLine(prex, prey, width - padding, height - padding - size, paint);
        long timeUsed = System.currentTimeMillis() - startTime;
        Log.d("Total time used", ""+timeUsed);
        Log.d("Average time used", "" + timeUsed/size);
    }

    public void setOnInterpolatorChangeListener(OnInterpolatorChangeListener onInterpolatorChangeListener)
    {
        this.interpolatorChangedListener = onInterpolatorChangeListener;
    }

    public interface OnInterpolatorChangeListener {
        void onInterpolatorChanged(CubicBezierInterpolator interpolator);
    }
}
