package com.example.brickbreaker;

public class Brick {
    boolean isActive;
    int height, width, x, y;

    protected Brick(boolean isActive, int height, int width, int x, int y){
        this.isActive = isActive;
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
    }

}
