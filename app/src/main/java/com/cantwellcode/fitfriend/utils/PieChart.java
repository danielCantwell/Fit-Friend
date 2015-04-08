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

    private int width, height;

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

        int dim = 2 * width / 5;

        if (sum == 0) {
            drawArc(canvas, 0, 360, Color.DKGRAY);
        } else {

            int a1 = (values[0] * 360) / sum;
            int a2 = (values[1] * 360) / sum;
            int a3 = (values[2] * 360) / sum;
            int a4 = (values[3] * 360) / sum;

            int a1Percent = values[0] * 100 / sum;
            int a2Percent = values[1] * 100 / sum;
            int a3Percent = values[2] * 100 / sum;
            int a4Percent = values[3] * 100 / sum;

            a1 ++;

            drawArc(canvas, 0, a1, type == Type.Macros ? COLOR_FAT : COLOR_BREAKFAST);
            drawArc(canvas, a1, a2, type == Type.Macros ? COLOR_CARBS : COLOR_LUNCH);
            drawArc(canvas, a1 + a2, a3, type == Type.Macros ? COLOR_PROTEIN : COLOR_DINNER);
            drawArc(canvas, a1 + a2 + a3, a4, COLOR_SNACK);

            int dyMacros = dim / 3;
            int dyMeals = dim / 4;
            int colorCodeTextY;

            mPaint.setColor(Color.BLACK);

            if (type == Type.Macros) {

                colorCodeTextY = dyMacros;

                mPaint.setColor(COLOR_FAT);
                canvas.drawText(a1Percent + "% Fat", width / 2, colorCodeTextY, mPaint);
                colorCodeTextY += dyMacros;

                mPaint.setColor(COLOR_CARBS);
                canvas.drawText(a2Percent + "% Carbs", width / 2, colorCodeTextY, mPaint);
                colorCodeTextY += dyMacros;

                mPaint.setColor(COLOR_PROTEIN);
                canvas.drawText(a3Percent + "% Protein", width / 2 , colorCodeTextY, mPaint);

            } else {

                colorCodeTextY = dyMeals;

                mPaint.setColor(COLOR_BREAKFAST);
                canvas.drawText(a1Percent + "% Breakfast", a1Percent == 100 ? ((width / 2) + (dim)) / 2 : width / 2, colorCodeTextY, mPaint);
                colorCodeTextY += dyMeals;

                mPaint.setColor(COLOR_LUNCH);
                canvas.drawText(a2Percent + "% Lunch", width / 2, colorCodeTextY, mPaint);
                colorCodeTextY += dyMeals;

                mPaint.setColor(COLOR_DINNER);
                canvas.drawText(a3Percent + "% Dinner", width / 2, colorCodeTextY, mPaint);
                colorCodeTextY += dyMeals;

                mPaint.setColor(COLOR_SNACK);
                canvas.drawText(a4Percent + "% Snack", width / 2, colorCodeTextY, mPaint);

            }
        }
    }

    private void drawArc(Canvas canvas, int startDegree, int angle, int color) {
//        RectF oval = new RectF(40, 50, 240, 250);
        int dim = 2 * width / 5;

        RectF oval = new RectF(0, 0, dim, dim);

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
        width = mHolder.getSurfaceFrame().width();
        height = mHolder.getSurfaceFrame().height();
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
