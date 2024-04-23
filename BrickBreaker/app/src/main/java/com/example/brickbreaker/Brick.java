package com.example.brickbreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Brick {
    boolean isActive;
    int height, width, x, y;

    protected Brick(){

    }
    protected Brick(boolean isActive, int height, int width, int x, int y){
        this.isActive = isActive;
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }

//    public void draw(Canvas canvas, Paint paint){
//        paint.setColor(Color.DKGRAY);
//        canvas.drawRect(x, y, x + width, y + height, paint);
//    }
}
