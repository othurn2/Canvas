package com.example.oliverthurn.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;

/**
 * Created by oliverthurn on 10/13/16.
 */
public class Draw extends View implements View.OnTouchListener{

    Bitmap bball;
    float x, y;
    public Draw(Context context){
        super(context);
        bball = BitmapFactory.decodeResource(getResources(), R.drawable.basketball);
        x = 0;
        y = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect viewRect = new Rect(0,0,canvas.getWidth(), canvas.getHeight());
        Paint blue = new Paint();
        blue.setColor(Color.BLUE);
        blue.setStyle(Paint.Style.FILL);

        canvas.drawRect(viewRect, blue);


        canvas.drawBitmap(bball, x, y, new Paint());
        invalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        x = event.getX();
        y = event.getY();


        return false;
    }
}
