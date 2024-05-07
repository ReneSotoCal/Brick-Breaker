package com.example.brickbreaker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    static boolean inMotion = false;
    GameThread gameThread;
    Handler handler = new Handler(Looper.getMainLooper());

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameThread = new GameThread(holder, getWidth(), getHeight(), this);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        gameThread.running = false;
        boolean retry = true;

        while(retry){
            try{
                gameThread.join();
                retry = false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(!inMotion)//On touch Event for the initial direction of the ball.
            if(event.getAction() == MotionEvent.ACTION_DOWN){

                gameThread.dy = 6;
                inMotion = true;

                if((int) event.getX() <= gameThread.sliderX + gameThread.width/10)
                    gameThread.dx = -5;
                else
                    gameThread.dx = 5;
            }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {//On touch Event for the Slider

            if((int) event.getX() >= gameThread.width - (gameThread.width/5))
                gameThread.sliderX = gameThread.width - (gameThread.width/5);
            else
                gameThread.sliderX = Math.max((int) event.getX(), 0);
        }
        return true;
    }
}
