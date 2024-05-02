
package com.example.brickbreaker;

public class Brick {
    boolean isActive;
    int height, width, x, y;
    boolean isSlider;

    protected Brick(boolean isActive, int height, int width, int x, int y, boolean isSlider){
        this.isActive = isActive;
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        this.isSlider = isSlider;
    }

}
