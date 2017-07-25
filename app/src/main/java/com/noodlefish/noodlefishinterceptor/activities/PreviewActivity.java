package com.noodlefish.noodlefishinterceptor.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.noodlefish.noodlefishinterceptor.R;
import com.noodlefish.noodlefishinterceptor.interpolators.CubicBezierInterpolator;
import com.noodlefish.noodlefishinterceptor.views.InterpolatorPreviewView;

/**
 * Created by zhoyu on 7/25/2017.
 */

public class PreviewActivity extends Activity {
    InterpolatorPreviewView previewView;
    TextView functionView;
    CubicBezierInterpolator cubicBezierInterpolator = new CubicBezierInterpolator();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        previewView = (InterpolatorPreviewView)findViewById(R.id.activity_preview_view);
        functionView = (TextView)findViewById(R.id.activity_preview_function);
        previewView.setInterpolator(cubicBezierInterpolator);
        functionView.setText("new CubicBezierInterpolator" + cubicBezierInterpolator.toString());
        previewView.setOnInterpolatorChangeListener(new InterpolatorPreviewView.OnInterpolatorChangeListener() {
            @Override
            public void onInterpolatorChanged(CubicBezierInterpolator interpolator) {
                functionView.setText("new CubicBezierInterpolator" + cubicBezierInterpolator.toString());
            }
        });
    }
}
