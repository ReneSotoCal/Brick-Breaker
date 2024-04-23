package com.example.brickbreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class GameThread extends Thread{

    boolean running = true;
    SurfaceHolder holder;
    int width, height, radius;
    int x;
    int y;
    int dx = 5;
    int dy = 5;

    public GameThread(SurfaceHolder holder, int width, int height){
        this.holder = holder;
        this.height = height;
        this.width = width;
    }

    @Override
    public void run() {

        Canvas canvas = null;
        Paint white = new Paint();
        Paint ballColor = new Paint();

        white.setColor(Color.WHITE);
        ballColor.setColor(Color.RED);
        long previousTime = System.currentTimeMillis();//Setting the time at start as the previous time.

        //Initializing the radius and x and y coordinates
        radius = 20;
        x = (int)(Math.random() * ((width - radius) - radius) + radius);//To be changed to the center of the slider
        y = (int)(Math.random() * ((height - radius) - radius) + radius);//To be changed to the top of the slider


        //Changing directions of the ball when a border is reached
        while(running) {
            try {
                canvas = holder.lockCanvas();//Try to lock the canvas if it is not locked already
                synchronized (holder) {

                    long currentTime = System.currentTimeMillis();//Current time of the balls position
                    canvas.drawRect(0, 0, width, height, white);//Draw the canvas
                    double elapsedTime = currentTime - previousTime;//Time elapsed between previous time and current

                    if (x - radius <= 0 || x + radius >= width)
                        dx = -dx;
                    if (y - radius <= 0 || y + radius >= height)
                        dy = -dy;

                    //Updating the new x and y locations based on the rate of change and direction
                    x += (int) (elapsedTime * dx * 0.1);
                    y += (int) (elapsedTime * dy * 0.1);

                    //Ensuring the coordinates never exceed the borders
                    x = Math.max(radius, Math.min(x, width - radius));
                    y = Math.max(radius, Math.min(y, height - radius));

                    //Draw a circle at the x and y coordinates with the ballColor color
                    canvas.drawCircle(x, y, radius, ballColor);
                    previousTime = currentTime;//Update the previous time
                }
            } finally {
                if(canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
