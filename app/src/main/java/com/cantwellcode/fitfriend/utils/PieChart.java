package com.cantwellcode.fitfriend.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.cantwellcode.fitfriend.R;

/**
 * Created by Daniel on 11/9/2014.
 */
public class PieChart extends SurfaceView implements SurfaceHolder.Callback {

    private static final int COLOR_BREAKFAST = Color.parseColor("#5da5da");
    private static final int COLOR_LUNCH = Color.parseColor("#faa43a");
    private static final int COLOR_DINNER = Color.parseColor("#60bd68");
    private static final int COLOR_SNACK = Color.parseColor("#f17cb0");

    private static final int COLOR_FAT = Color.parseColor("#f15854");
    private static final int COLOR_CARBS = Color.parseColor("#decf3f");
    private static final int COLOR_PROTEIN = Color.parseColor("#b276b2");

    private Paint mPaint;

    private SurfaceHolder mHolder;
    private boolean surfaceCreatedNotCalled = true;
    private boolean setValuesWaiting = false;

    private int[] values = {0, 0, 0, 0};
    private int sum = 0;

    private enum Type {
        Calories, Macros
    }

    private Type type;

    public PieChart(Context context) {
        super(context);

        construct();
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        construct();
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        construct();
    }

    public void construct() {
        mPaint = new Paint();
        mPaint.setTextSize(40);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(30);

        getHolder().addCallback(this);
        mHolder = getHolder();

        if (getId() == R.id.surfaceMacros) {
            type = Type.Macros;
        } else if (getId() == R.id.surfaceCalories) {
            type = Type.Calories;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        Log.d("Nutrition Canvas", "onDraw called");

        if (sum == 0) {
            drawArc(canvas, 0, 360, Color.DKGRAY);
        } else {

            int a1 = (values[0] * 360) / sum;
            int a2 = (values[1] * 360) / sum;
            int a3 = (values[2] * 360) / sum;
            int a4 = (values[3] * 360) / sum;

            int a1P = values[0] * 100 / sum;
            int a2P = values[1] * 100 / sum;
            int a3P = values[2] * 100 / sum;
            int a4P = values[3] * 100 / sum;

            a1 ++;

            drawArc(canvas, 0, a1, type == Type.Macros ? COLOR_FAT : COLOR_BREAKFAST);
            drawArc(canvas, a1, a2, type == Type.Macros ? COLOR_CARBS : COLOR_LUNCH);
            drawArc(canvas, a1 + a2, a3, type == Type.Macros ? COLOR_PROTEIN : COLOR_DINNER);
            drawArc(canvas, a1 + a2 + a3, a4, COLOR_SNACK);

            int colorCodeTextY = 80;

            mPaint.setColor(Color.BLACK);

            if (type == Type.Macros) {

                mPaint.setColor(COLOR_FAT);
                canvas.drawText(a1P + "% Fat", 290, colorCodeTextY, mPaint);
                colorCodeTextY += 80;

                mPaint.setColor(COLOR_CARBS);
                canvas.drawText(a2P + "% Carbs", 290, colorCodeTextY, mPaint);
                colorCodeTextY += 80;

                mPaint.setColor(COLOR_PROTEIN);
                canvas.drawText(a3P + "% Protein", 290, colorCodeTextY, mPaint);

            } else {

                mPaint.setColor(COLOR_BREAKFAST);
                canvas.drawText(a1P + "% Breakfast", a1P == 100 ? 265 : 275, colorCodeTextY, mPaint);
                colorCodeTextY += 60;

                mPaint.setColor(COLOR_LUNCH);
                canvas.drawText(a2P + "% Lunch", 275, colorCodeTextY, mPaint);
                colorCodeTextY += 60;

                mPaint.setColor(COLOR_DINNER);
                canvas.drawText(a3P + "% Dinner", 275, colorCodeTextY, mPaint);
                colorCodeTextY += 60;

                mPaint.setColor(COLOR_SNACK);
                canvas.drawText(a4P + "% Snack", 275, colorCodeTextY, mPaint);

            }
        }
    }

    private void drawArc(Canvas canvas, int startDegree, int angle, int color) {
        RectF oval = new RectF(40, 50, 240, 250);

        mPaint.setColor(color);
        canvas.drawArc(oval, startDegree, angle, true, mPaint);
    }

    public void setValues(int first, int second, int third, int fourth) {

        values[0] = first;
        values[1] = second;
        values[2] = third;
        values[3] = fourth;
        sum = first + second + third + fourth;

        Log.d("Nutrition Canvas", "Values: " + first + " " + second + " " + third + " " + fourth);
        Log.d("Nutrition Canvas", "Sum: " + sum);

        if (surfaceCreatedNotCalled) {
            setValuesWaiting = true;
        } else {
            setValuesWaiting = false;
            Canvas c = mHolder.lockCanvas();
            if (c == null) {
                Log.e("Nutrition Canvas", "Cannot draw onto canvas.  It is null.");
            } else {
                Log.d("Nutrition Canvas", "Drawing onto canvas");
                postInvalidate();
                draw(c);
                mHolder.unlockCanvasAndPost(c);
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mHolder = surfaceHolder;
        surfaceCreatedNotCalled = false;
        setWillNotDraw(false);

        Log.d("Nutrition Canvas", "Surface Created");

        if (setValuesWaiting) {
            setValues(values[0], values[1], values[2], values[3]);
            Log.d("Nutrition Canvas", "Set values has been called again");
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

}
