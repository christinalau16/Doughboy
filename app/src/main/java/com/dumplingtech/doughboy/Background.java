package com.dumplingtech.doughboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

class Background {

    RectF rect;

    private Bitmap bitmapBack;

    private int screenX;

    private int bgX, bgY, speedX;

    Background(Context context, int screenX, int screenY, int x, int y){

        // Initialize a blank RectF
        rect = new RectF();

        this.screenX = screenX;

        // Initialize the bitmap
        bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmapBack = Bitmap.createScaledBitmap(bitmapBack,
                screenX + 15,
                screenY,
                false);

        bgX = x;
        bgY = y;
        speedX = 0;
    }

    Bitmap getBitmap(){
        return bitmapBack;
    }

    void update() {
        if (isMoving()) {
            bgX += (speedX );
            if (bgX <= -screenX) {
                bgX += screenX*2;
            }
        }
    }

    int getBgX() {
        return bgX;
    }

    int getBgY() {
        return bgY;
    }

    int getSpeedX() {
        return speedX;
    }

    void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public boolean isMoving()
    {
        if (speedX==0)
            return false;
        return true;
    }


}
