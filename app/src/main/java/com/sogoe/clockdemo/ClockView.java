package com.sogoe.clockdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * Created by sogoe on 2015/3/14.
 */
public class ClockView extends View {
    private int height = 0; //view的高度
    private int width = 0; //view的宽度
    private int padding = 0; //padding值
    private int fontSize = 0; //字体大小
    private static int numeralSpacing = 20; //数字距离钟表的距离
    private int handTruncation = 0;
    private int hourHandTruncation = 0;
    private int radius = 0; //钟表的大小
    private int numeralRadius = 0;
    private Paint paint = null;
    boolean isInited = false;
    private int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    public ClockView(Context context) {
        super(context);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    void initClock() {
        height = this.getHeight();
        width = this.getWidth();
        padding = 35;
        fontSize = 10;
        int min = Math.min(this.getHeight(), this.getWidth());
        radius = min/2 - padding;
        handTruncation = min/25;
        hourHandTruncation = min/10;
        numeralRadius = radius + numeralSpacing;

        paint = new Paint();
        isInited = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!isInited)
            initClock();

        drawCircle(canvas);
        drawCenter(canvas);
        drawNumeral(canvas);
        drawHands(canvas);

        this.postInvalidateDelayed(1000);
        this.invalidate();

    }

    void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.black));
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width/2, height/2, radius, paint);
    }

    void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width/2, height/2, 5, paint);
    }

    void drawNumeral(Canvas canvas) {
        paint.setTextSize(fontSize);
        float fontWidth;
        double angle;
        int x, y;
        for (int number : numbers) {
            fontWidth = paint.measureText(String.valueOf(number));
            angle = Math.PI / 6 * (number - 3);
            x = (int) (width / 2 + Math.cos(angle) * numeralRadius - fontWidth / 2);
            y = (int) (height / 2 + Math.sin(angle) * numeralRadius + fontSize / 3);
            canvas.drawText(String.valueOf(number), x, y, paint);
        }
    }

    void drawHand(Canvas canvas, double loc, boolean isHour) {
        double angle = Math.PI * loc / 30 - Math.PI / 2;
        int handRadius = isHour ? radius - handTruncation - hourHandTruncation : radius - handTruncation;

        canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * handRadius),
                (float) (height / 2 + Math.sin(angle) * handRadius),
                paint);
    }

    void drawHands(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        float hour = calendar.get(Calendar.HOUR_OF_DAY);
        hour = hour > 12 ? hour - 12 : hour;

        drawHand(canvas, (hour + calendar.get(Calendar.MINUTE) / 60.f) * 5.f, true);
        drawHand(canvas, calendar.get(Calendar.MINUTE), false);
        drawHand(canvas, calendar.get(Calendar.SECOND), false);
    }
}
