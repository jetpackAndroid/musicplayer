package com.example.bhavik.canvas.CustomViews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.security.PrivateKey;

/**
 * Created by Bhavik on 8/9/2015.
 */
public class CanvasView extends View {
    public int width;
    public int height;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    Context mContext;
    private Paint mPaint;
    private float mX,mY;
    private static final float TOLERANCE = 5;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
}
