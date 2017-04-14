package com.dumplingtech.doughboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Mushroom extends Enemy{

    private Background bg = SpriteSheetView.getBg1();

    // Num of frames on sprite sheet
    private int frameCount = 100;

    // Time since last changed frame
    private long lastFrameChangeTime = 0;

    // How long should each frame last
    private int frameLengthInMilliseconds = 60;

    Mushroom(Context context, int screenX, int screenY, int x, int y){

        super.frameWidth = 100;
        super.frameHeight = 100;

        super.screenX = screenX;
        super.screenY = screenY;

        // Initialize the bitmap
        bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.mushroom);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmapBack = Bitmap.createScaledBitmap(bitmapBack,
                frameWidth,
                frameHeight,
                false);

        bgX = x;
        bgY = y;

        currentFrame = 0;

        frameToDrawMushroom = new Rect(
                0,
                0,
                getFrameWidth(),
                0);
        whereToDrawMushroom = new Rect(
                getBgX(), screenY-screenY/4,
                getBgX() + getFrameWidth(),
                screenY-screenY/4);

        super.rect = new Rect(getBgX(),
                (screenY-screenY/4),
                getBgX() + getFrameWidth(),
                (screenY-screenY/4));
    }

    public void update(long fps) {
        bgX += (bg.getSpeedX() );
        if (bgX <= -screenX) {
            bgX += screenX*2;
        }

        if (currentFrame <frameCount)
            getCurrentFrame();
        whereToDrawMushroom.set(getBgX(),
                (screenY-screenY/4)-super.currentFrame,
                getBgX() + getFrameWidth(),
                (screenY-screenY/4));

        bgY =screenY-screenY/4- super.currentFrame;

        super.rect = whereToDrawMushroom;

    }

    public void getCurrentFrame(){

        long time  = System.currentTimeMillis();
        if ( time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame ++;
        }
        frameToDrawMushroom.bottom = frameToDrawMushroom.top + super.currentFrame;

    }

    public Rect getRect()
    {
        return whereToDrawMushroom;
    }


}

