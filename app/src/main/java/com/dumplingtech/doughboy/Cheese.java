package com.dumplingtech.doughboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Cheese extends Enemy{

    private int screenX;

    private Background bg = SpriteSheetView.getBg1();
    private float speedY;


    Cheese(Context context, int screenX, int screenY, int x, int y){

        super.frameWidth = 50;
        super.frameHeight = 120;

        this.screenX = screenX;
        this.screenY = screenY;

        // Initialize the bitmap
        bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.cheese);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmapBack = Bitmap.createScaledBitmap(bitmapBack,
                frameWidth,
                frameHeight,
                false);

        bgX = x;
        bgY = y;
        speedY = 0;

        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);
    }

    public void update(long fps) {
        speedY =0.00005f * Doughboy.totalDistance() +10;
        bgX += (bg.getSpeedX() );
        if (bgX <= -screenX) {
            bgX += screenX*2;
        }
        bgY += (speedY);
        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);

    }

}
