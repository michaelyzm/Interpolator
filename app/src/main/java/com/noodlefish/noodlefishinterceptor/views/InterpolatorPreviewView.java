package com.noodlefish.noodlefishinterceptor.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
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
    private int currentDragging = 0;
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
        if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            float y = getHeight() - event.getY();
            if (x < padding) {
                x = padding;
            } else if (x > getWidth() - padding) {
                x = getWidth() - padding;
            }
            x = (x - padding) / (getWidth() - 2 * padding);
            y -= padding;
            if(y < - padding)
            {
                y = -padding;
            }
            else if(y > getHeight() - padding)
            {
                y = getHeight() - padding;
            }
            y = y / (getHeight() - 2 * padding);
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                float distance = Math.abs(x - interpolator.getX0()) + Math.abs(y - interpolator.getY0());
                if (distance < 0.1) {
                    currentDragging = 1;
                } else {
                    distance = Math.abs(x - interpolator.getX1()) + Math.abs(y - interpolator.getY1());
                    if (distance < 0.1) {
                        currentDragging = 2;
                    } else {
                        currentDragging = 0;
                    }
                }
            }

            if(currentDragging != 0) {
                if (currentDragging == 1) {
                    interpolator.setX0Y0(x, y);
                } else if(currentDragging == 2) {
                    interpolator.setX1Y1(x, y);
                }
                if (interpolatorChangedListener != null) {
                    interpolatorChangedListener.onInterpolatorChanged(interpolator);
                }
                invalidate();
            }
            return true;
        }
        else if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
            currentDragging = 0;
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        int size = Math.min(width, height);
        size -= 2*padding;

        canvas.drawLine(0, 0, width, 0, paint);
        canvas.drawLine(0, 0, 0, height, paint);
        canvas.drawLine(width, 0, width, height, paint);
        canvas.drawLine(0, height, width, height,paint);
        canvas.drawLine(padding, height-padding, width - padding, height-padding, paint);
        canvas.drawLine(padding, height - padding - size, padding, height - padding, paint);
        RectF P1 = new RectF(interpolator.getX0()*size+padding - 10, height - padding - interpolator.getY0()*size - 10, interpolator.getX0()*size+padding + 10, height - padding - interpolator.getY0()*size + 10);
        RectF P2 = new RectF(interpolator.getX1()*size+padding - 10, height - padding - interpolator.getY1()*size - 10, interpolator.getX1()*size+padding + 10, height - padding - interpolator.getY1()*size + 10);
        canvas.drawRect(P1, paint);
        canvas.drawRect(P2, paint);
        canvas.drawLine(padding, height - padding, interpolator.getX0()*size+padding, height - padding - interpolator.getY0()*size, paint);
        canvas.drawLine(width - padding, height - padding - size, interpolator.getX1()*size+padding, height - padding - interpolator.getY1()*size, paint);
        float x = padding, y = height - padding;
        float prex = x, prey = y;
        for(int i = 0; i <= size; ++i)
        {
            float xt = i + x;
            float yt = interpolator.getInterpolation((float)i/size) * size;
            yt = y - yt;
            canvas.drawLine(prex, prey, xt, yt, paint);
            prex = xt;
            prey = yt;
        }
    }

    public void setOnInterpolatorChangeListener(OnInterpolatorChangeListener onInterpolatorChangeListener)
    {
        this.interpolatorChangedListener = onInterpolatorChangeListener;
    }

    public interface OnInterpolatorChangeListener {
        void onInterpolatorChanged(CubicBezierInterpolator interpolator);
    }
}
