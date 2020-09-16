package com.progtech.vkpodcasts;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.progtech.vkpodcasts.R;

import java.util.Date;

import static java.lang.String.format;

/**
 * TODO: document your custom view class.
 */
public class WaveformView extends View implements View.OnTouchListener {
    private int mScaleColor = Color.BLUE;
    private Paint paint;
    private Date dt;
    private int mPosition = 0;
    private int touchX = 0, touchY = 0;

    public WaveformView(Context context) {
        super(context);
        init(null, 0);
    }

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public WaveformView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.WaveformView, defStyle, 0);

        mScaleColor = a.getColor(
                R.styleable.WaveformView_scaleColor,
                mScaleColor);

        a.recycle();

        paint = new Paint();
        paint.setColor(mScaleColor);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);

        dt = new Date();

        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // размеры View
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        final int heightScale = 100;                // высота шкалы в px
        final int stepScale = 20;                   // деление шкалы в px
        int heightLine;
        dt.setTime(0);

        for (int i = 0; i < width / stepScale; i++) {
            if (i % 10 == 0) heightLine = 30;       // высота длиного деления
            else if (i % 2 == 0) heightLine = 20;   // высота промежуточного деления
            else heightLine = 10;                   // высота обычного деления

            // рисуем деления
            canvas.drawLine(i * stepScale + mPosition, heightScale,
                    i * stepScale + mPosition, heightScale - heightLine, paint);

            // подписи к делениям
            if (i % 10 == 0) {
                canvas.drawText(
                        DateFormat.format("mm:ss", dt).toString(),
                        i * stepScale + mPosition,
                        heightScale - heightLine - 3,
                        paint
                );
                dt.setTime(dt.getTime() + 10000);
            }

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log.d("TOUCH", "TOUCH");

        int deltaX, y;

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN: // нажатие
                touchX = (int) motionEvent.getX();
                touchY = (int) motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE: // движение
                deltaX = (int) motionEvent.getX() - touchX;
                mPosition = Math.min(deltaX, 0);
                Log.d("MOVE", String.format("touchX %d curX %d", touchX, (int)motionEvent.getX()));
                invalidate();
                break;
            case MotionEvent.ACTION_UP: // отпускание
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }
}
