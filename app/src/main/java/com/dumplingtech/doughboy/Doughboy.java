package com.dumplingtech.doughboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Doughboy {
    static Rect rect;

    private Bitmap bitmapBob;

    private Background bg1 = SpriteSheetView.getBg1();
    private Background bg2 = SpriteSheetView.getBg2();

    private int screenX;
    private int screenY;
    private int frameWidth = 80;
    private int frameHeight = 80;

    private float bobXPosition = 10;//He starts 10 pixels from the left
    private float bobYPosition ;

    private static float totalDistance = 0;

    final int JUMPSPEED;
    private float MOVESPEED;
    private float speedX = 0;
    private float speedY = 0;

    private boolean jumped = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;

    Doughboy(Context context, int screenX, int screenY, int frameCount){

        this.screenX = screenX;
        this.screenY = screenY;

        bitmapBob = BitmapFactory.decodeResource(context.getResources(), R.drawable.doughboy);
        bitmapBob = Bitmap.createScaledBitmap(bitmapBob,
                frameWidth * frameCount,
                frameHeight,
                false);

        bobXPosition = 10;
        bobYPosition = screenY - screenY/(3.5f);

        MOVESPEED = screenX / 90;
        JUMPSPEED = -screenY/16;

        rect = new Rect((int)bobXPosition, (int)bobYPosition, (int)(bobXPosition + getWidth()),
                (int)(bobYPosition + getHeight() ));
    }
    
    public void update() {
        if (speedX < 0)
            bobXPosition += (speedX);
        if (speedX == 0 || speedX < 0) {
            bg1.setSpeedX(0);
            bg2.setSpeedX(0);
        }
        if (bobXPosition <= screenX / 3 && speedX > 0)
            bobXPosition += (speedX );
        if (speedX > 0 && bobXPosition > screenX / 3) {
            bg1.setSpeedX(-((int) (speedX)));
            bg2.setSpeedX(-((int) (speedX )));
            totalDistance+=speedX;
        }
        if (bobXPosition + (speedX ) <= 10)
            bobXPosition = 10;
        
        bobYPosition += (int) (speedY);
        if (bobYPosition <10)
            speedY = 0;
        if (bobYPosition < (screenY - screenY/(3.5f)))
            speedY+=10;
        if (bobYPosition > (screenY - screenY/(3.5f)))
            bobYPosition = (screenY - screenY/(3.5f));
        
        rect = new Rect((int)bobXPosition, (int)bobYPosition, (int)(bobXPosition + getWidth()),
                        (int)(bobYPosition + getHeight() ));
    }

    public static Rect getRect(){
        return rect;
    }

    Bitmap getBitmap(){
        return bitmapBob;
    }

    float getX(){
        return bobXPosition;
    }

    float getY(){
        return bobYPosition;
    }

    float getWidth(){
        return frameWidth;
    }

    float getHeight(){
        return frameHeight;
    }

    void moveRight() {
        speedX = MOVESPEED;
    }
    void moveLeft() {
        speedX = -MOVESPEED;
    }
    void stopRight() {
        setMovingRight(false);
        stop();
    }
    void stopLeft() {
        setMovingLeft(false);
        stop();
    }
    private void stop() {
            speedX = 0;
    }

    boolean isMoving()
    {
        if (speedX==0)
            return false;
        return true;
    }

    public boolean isMovingRight() {
        return movingRight;
    }

    public void setMovingRight(boolean movingRight) {
        this.movingRight = movingRight;
    }

    public boolean isMovingLeft() {
        return movingLeft;
    }

    public void setMovingLeft(boolean movingLeft) {
        this.movingLeft = movingLeft;
    }
    
    void jump() {
        if (!jumped) {
            speedY = JUMPSPEED;
            jumped = true;
        }
    }
    
    void setJumped(boolean jumped) {
        this.jumped = jumped;
    }

    static float totalDistance(){
        return totalDistance;
    }

    static void resetTotalDistance()
    {
        totalDistance =0;
    }

}
