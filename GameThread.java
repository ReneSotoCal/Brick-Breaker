
package com.example.brickbreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.widget.Toast;

import java.util.ArrayList;

public class GameThread extends Thread{

    ArrayList<ArrayList<Brick>> bricks;
    boolean running = true;
    final SurfaceHolder holder;
    int width, height, radius;
    int x;
    int y;
    int dx = 4;
    int dy = 4;
    int sliderX;

    int sliderPos;

    GameView gameView;


    public GameThread(SurfaceHolder holder, int width, int height, GameView gameView){
        this.holder = holder;
        this.height = height;
        this.width = width;
        this.gameView = gameView;
        sliderPos = (width / 2) - (width / 10);
    }


    public void setSliderX(int x) {
        sliderPos = x;
    }



    @Override
    public void run() {

        //Initializing the canvas and paint instances
        Canvas canvas = null;
        Paint white = new Paint();
        Paint ballColor = new Paint();

        fillBricks(this);//Method to populate the bricks arraylist

        white.setColor(Color.WHITE);//Color for the canvas
        ballColor.setColor(Color.RED);//Ball Color
        long previousTime = System.currentTimeMillis();//Setting the time at start as the previous time.

        //Initializing the radius and x and y coordinates
        radius = 35;
        x = (int)(Math.random() * ((width - radius) - radius) + radius);//To be changed to the center of the slider
        y = (int)(Math.random() * ((height - radius - 110) - radius) + radius + 430);//To be changed to the top of the slider

        //Changing directions of the ball when a border is reached
        while(running) {
            try {
                canvas = holder.lockCanvas();//Try to lock the canvas if it is not locked already
                synchronized (holder) {//Ensure the holder's access is not taken by more than 1 process

                    long currentTime = System.currentTimeMillis();//Current time of the balls position
                    canvas.drawRect(0, 0, width, height, white);//Draw the canvas
                    double elapsedTime = currentTime - previousTime;//Time elapsed between previous time and current


                    //Draw the slider brick
                    Brick slider = new Brick(true, 75, width / 5, sliderPos, height - 100, true);
                    drawBrick(slider, canvas);
                    collides(slider, this);

                    boolean gameWon = true;
                    //Iterate through the bricks
                    for(ArrayList<Brick> row : bricks)
                        for(Brick brick : row){
                            if((brick.isActive)) {
                                drawBrick(brick, canvas);//Draw the bricks
                                gameWon = false;
                            }
                            if(collides(brick, this))//Calls collision method for bricks
                                brick.isActive = false;//Delete the brick if a collision occurs
                        }
//                    if(gameWon)
////                        Toast.makeText(gameView.getContext(), "You Win!", Toast.LENGTH_SHORT).show();

                    //Collisions for the borders of the screen
                    if (x - radius <= 0 || x + radius >= width)
                        dx = -dx;
                    if (y - radius <= 0)
                        dy = -dy;

                    //Stops game from running if the ball hits the bottom of the screen
                    if(y + radius >= height)
                        running = false;

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

    //Draw the brick on the canvas
    public static void drawBrick(Brick brick, Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(brick.x, brick.y, brick.x + brick.width, brick.y + brick.height, paint);
    }

    //Method to test collision between the ball and the bricks
    public static boolean collides(Brick brick, GameThread game){
        int testX = game.x;
        int testY = game.y;

        if(brick.isActive){//If the brick has never been touched

            //Make the testX coordinate the closest brick x coordinate to the ball
            if(game.x < brick.x)
                testX = brick.x;
            else if(game.x > brick.x + brick.width)
                testX = brick.x + brick.width;

            //Make the testY coordinate the closest brick y coordinate to the ball
            if(game.y < brick.y)
                testY = brick.y;
            else if(game.y > brick.y + brick.height)
                testY = brick.y + brick.height;

            //Calculate the distance of the respective x and y coordinates and the total distance
            int distanceX = game.x - testX;
            int distanceY = game.y - testY;
            int distance = (int) Math.sqrt(distanceX * distanceX + distanceY * distanceY);

            //If the distance is less than the radius of the ball then there is a collision
            if(distance <= game.radius) {
                //Bounding Boxes Based Algorithm
                if(game.x < brick.x ){
                    if(game.y < brick.y)
                        game.dy = -game.dy;
                    else if(game.y > brick.y + brick.height)
                        game.dy = -game.dy;

                    game.dx = -game.dx;
                } else if(game.x > brick.x + brick.width){
                    if(game.y < brick.y)
                        game.dy = -game.dy;
                    else if(game.y > brick.y + brick.height)
                        game.dy = -game.dy;

                    game.dx = -game.dx;
                } else {
                    if(game.y < brick.y)
                        game.dy = -game.dy;
                    else if(game.y > brick.y + brick.height)
                        game.dy = -game.dy;
                }
                return true;//There is a collision
            }
        }

        return false;//There is no collision
    }

    //Create and add Bricks to the bricks ArrayList
    public static void fillBricks(GameThread game){
        game.bricks = new ArrayList<>();
        ArrayList<Brick> row1 = new ArrayList<>();
        ArrayList<Brick> row2 = new ArrayList<>();
        ArrayList<Brick> row3 = new ArrayList<>();
        ArrayList<Brick> row4 = new ArrayList<>();
        ArrayList<Brick> row5 = new ArrayList<>();

        //Row 1
        row1.add(new Brick(true, 75, game.width / 5, 50, 0, false));
        row1.add(new Brick(true, 75, game.width / 5, row1.get(0).x + game.width / 5 + 30, 0, false));
        row1.add(new Brick(true, 75, game.width / 5, row1.get(1).x + game.width / 5 + 30, 0, false));
        row1.add(new Brick(true, 75, game.width / 5, row1.get(2).x + game.width / 5 + 30, 0, false));

        //Row2
        row2.add(new Brick(true, 75, game.width / 5, 50, 105, false));
        row2.add(new Brick(true, 75, game.width / 5, row1.get(0).x + game.width / 5 + 30, 105, false));
        row2.add(new Brick(true,  75, game.width / 5, row1.get(1).x + game.width / 5 + 30, 105, false));
        row2.add(new Brick(true, 75, game.width / 5, row1.get(2).x + game.width / 5 + 30, 105, false));

        //Row3
        row3.add(new Brick(true, 75, game.width / 5, 50, 210, false));
        row3.add(new Brick(true, 75, game.width / 5, row1.get(0).x + game.width / 5 + 30, 210, false));
        row3.add(new Brick(true, 75, game.width / 5, row1.get(1).x + game.width / 5 + 30, 210, false));
        row3.add(new Brick(true, 75, game.width / 5, row1.get(2).x + game.width / 5 + 30, 210, false));

        //Row4
        row4.add(new Brick(true, 75, game.width / 5, 50, 10, false));
        row4.add(new Brick(true, 75, game.width / 5, row1.get(0).x + game.width / 5 + 30, 315, false));
        row4.add(new Brick(true, 75, game.width / 5, row1.get(1).x + game.width / 5 + 30, 315, false));
        row4.add(new Brick(true, 75, game.width / 5, row1.get(2).x + game.width / 5 + 30, 315, false));

        //Row5
        row5.add(new Brick(true, 75, game.width / 5, 50, 10, false));
        row5.add(new Brick(true, 75, game.width / 5, row1.get(0).x + game.width / 5 + 30, 420, false));
        row5.add(new Brick(true, 75, game.width / 5, row1.get(1).x + game.width / 5 + 30, 420, false));
        row5.add(new Brick(true, 75, game.width / 5, row1.get(2).x + game.width / 5 + 30, 420, false));

        //Add the rows to the list of bricks
        game.bricks.add(row1);
        game.bricks.add(row2);
        game.bricks.add(row3);
        game.bricks.add(row4);
        game.bricks.add(row5);
    }
}
