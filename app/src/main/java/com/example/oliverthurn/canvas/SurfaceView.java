package com.example.oliverthurn.canvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

/**
 * Created by oliverthurn on 10/13/16.
 */
public class SurfaceView extends AppCompatActivity implements View.OnTouchListener {

    OurView theView;
    Bitmap ball;
    float x, y, dX, dY, sX, sY, fX, fY , animX, animY, scaleX, scaleY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theView = new OurView(this);
        theView.setOnTouchListener(this);
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.basketball);
        x = y = dX = dY = sX = sY = fX = fY =  animX = animY = scaleX = scaleY = 0;
        setContentView(theView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        theView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theView.resume();
    }



    /* Creating a class inside of a class that extends a type of view. This is a better way of controlling canvases and bitmaps that
     * require the screen to be drawn over and over. Keeps things separate from the UI view in its own thread to better manage the memory
      * usage of the app */

    public class OurView extends android.view.SurfaceView implements Runnable{

        Thread runThread = null;
        SurfaceHolder sHolder;
        boolean allGood = false;

        // This is the constructor that passes the context that it is being called in and uses the constructor from the surfaceView class
       public OurView(Context context) {
           super(context);
           sHolder = getHolder();
       }

        // Runs everything that our canvas and surfaceView will draw. While everything is allGood (true) it will check if the holder
        // is valid,
       @Override
        public void run() {

          try{
              runThread.sleep(10);
          } catch (InterruptedException e ){
              e.printStackTrace();
          }

           while (allGood == true){
               // This is where everything is done to draw the canvas.
               // if the surface is not not valid it will keep checking to make sure we get a valid surface
               if (!sHolder.getSurface().isValid()){
                   continue;
               }

               // This locks the canvas that is in the holder so we can manipulate it
               Canvas canvasInUse = sHolder.lockCanvas();
               //canvasInUse.drawARGB(0, 0, 0, 0); // this does not work because there is a transparency issue
               canvasInUse.drawColor(Color.BLACK); // have to use this so the background will be redrawn with the ball
               Paint p = new Paint();
               if (x != 0 && y != 0){
                   canvasInUse.drawBitmap(ball, x - (ball.getWidth() / 2), y - (ball.getHeight() / 2), p);
               }
               if( sX != 0 && sY != 0){
                   canvasInUse.drawBitmap(ball, x - (ball.getWidth() / 2), y - (ball.getHeight() / 2), p);
               }
               if (fX != 0 && fY != 0) {
                   canvasInUse.drawBitmap(ball, x - (ball.getWidth() / 2) - animX, y - (ball.getHeight() / 2) - animY, p);
               }
               animX =  animX + scaleX;
               animY = animY + scaleY;
               // Everything that is set on the bitmap or canvas needs to be done before this call
               sHolder.unlockCanvasAndPost(canvasInUse);

           }

        }

        // This will control everything that happens when the pause method is called from another class on an object from
        // this class. Sets the bool whether to run or not to false so things stop running and then controls the thread
        // and sets it back to null so nothing will happen.
        public void pause(){

            allGood = false;

            while(true){
                try {
                    runThread.join();  // waits for a thread to die
                         } catch (InterruptedException e){
                        e.printStackTrace();
                }
                break;
            }
            runThread = null;
        }

        // This will control everything that happens when the resume method is called from another class on an object from
        // this class. Sets the bool of whether to run to true so everything will operate as it should. Creates the thread and
        // starts the thread.
        public void resume(){
            allGood = true;
            runThread = new Thread(this);
            runThread.start();

        }

    } // End of view subclass

    // onTouch from the parent class
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        x = event.getX();
        y = event.getY();

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                sX = event.getX();
                sY = event.getY();
                dX = dY = fX = fY = animX = animY = scaleX  = scaleY = 0;
                //v.invalidate();
               break;
            case MotionEvent.ACTION_UP:
                fX = event.getX();
                fY = event.getY();
                dX = fX + sX;
                dY = fY - sY;
                scaleX = dX / 25;
                scaleY = dY / 25;
                x = y = 0;
                //v.invalidate();
                break;
//            case MotionEvent.ACTION_MOVE:
//                fX = event.getX();
//                fY = event.getY();
//                dX = fX + sX;
//                dY = fY - sY;
//                scaleX = dX / 25;
//                scaleY = dY / 25;
//                x = y = 0;
//                //v.invalidate();
//                break;
        }

        return true;
    }
}
