package com.dumplingtech.doughboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Pepperoni extends Enemy {

    private Background bg = SpriteSheetView.getBg1();
    private int speedX;

    float MOVESPEED;

    Pepperoni(Context context, int screenX, int screenY, int x, int y){
        super.frameWidth = 80;
        super.frameHeight = 80;

        super.screenX = screenX;
        super.screenY = screenY;

        // Initialize the bitmap
        bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.pepperoni);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmapBack = Bitmap.createScaledBitmap(bitmapBack,
                frameWidth,
                frameHeight,
                false);

        MOVESPEED = -screenX/100.0f;

        bgX = x;
        bgY = y;
        speedX = (int) MOVESPEED;

        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);
    }

    public void update(long fps) {
        if (bg.getSpeedX() < 0)
            bgX += (2 * (speedX ));
        else
            bgX += (speedX );

        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);

    }

}
