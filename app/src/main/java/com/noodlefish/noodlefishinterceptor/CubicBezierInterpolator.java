package com.noodlefish.noodlefishinterceptor;

import android.view.animation.Interpolator;

/**
 * Created by Noodle Fish on 7/25/2017.
 */

public class CubicBezierInterpolator implements Interpolator {
    private float x0 = 0.25f;
    private float y0 = 0.1f;
    private float x1 = 0.25f;
    private float y1 = 1.0f;

    public CubicBezierInterpolator()
    {
    }

    public CubicBezierInterpolator(float x0, float y0, float x1, float y1)
    {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
    }

    float cubicBezier(float t, float c0, float c1)
    {
        return 3*c0*t*(1-t)*(1-t) + 3*c1*t*t*(1-t) + t*t*t;
    }

    float estimateT(float x)
    {
        float result = 0;
        float step = 0.001f;
        for(int i = 0; i < 1000; ++i)
        {
            if(cubicBezier(result, x0, x1) >= x)
            {
                break;
            }
            else
            {
                result += step;
            }
        }
        return result;
    }

    @Override
    public float getInterpolation(float input) {
        float t = estimateT(input);
        float yResult = cubicBezier(t, y0, y1);
        return yResult;
    }
}
