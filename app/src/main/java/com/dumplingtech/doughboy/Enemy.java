package com.dumplingtech.doughboy;

import android.graphics.Bitmap;
import android.graphics.Rect;

abstract class Enemy {
    Rect rect;
    Bitmap bitmapBack;
    int bgX, bgY;
    int frameWidth, frameHeight;
    int screenX, screenY;
    int currentFrame;
    Rect frameToDrawMushroom;
    Rect whereToDrawMushroom;

    public Rect getRect(){
        return rect;
    }

    Bitmap getBitmap(){
        return bitmapBack;
    }

    int getBgX() {
        return bgX;
    }

    int getBgY() {
        return bgY;
    }

    void setBgX(int bgX) {
        this.bgX = bgX;
    }

    void setBgY(int bgY) {
        this.bgY = bgY;
    }

    int getFrameWidth(){return frameWidth;}

    abstract public void update(long fps);

    void resetCurrentFrame(){
        currentFrame = 0;
    }

    Rect getFrameToDrawMushroom(){
        return frameToDrawMushroom;
    }

    Rect getWhereToDrawMushroom(){
        return whereToDrawMushroom;
    }
}
