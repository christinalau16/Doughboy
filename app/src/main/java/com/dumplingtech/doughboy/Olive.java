package com.dumplingtech.doughboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Olive extends Enemy {

    // The olive will be represented by a Bitmap

    private Background bg = SpriteSheetView.getBg1();
    private int speedX, speedY;

    final int JUMPSPEED;
    float MOVESPEED;

    Olive(Context context, int screenX, int screenY, int x, int y){
        super.frameWidth = 50;
        super.frameHeight = 50;

        super.screenX = screenX;
        super.screenY = screenY;

        // Initialize the bitmap
        bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.olive);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmapBack = Bitmap.createScaledBitmap(bitmapBack,
                frameWidth,
                frameHeight,
                false);

        MOVESPEED = -screenX / 90;
        JUMPSPEED = -screenY / 16;

        bgX = x;
        bgY = y;
        speedX = (int) MOVESPEED;
        speedY = JUMPSPEED;

        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);
    }

    public void update(long fps) {
        if (bg.getSpeedX() < 0)
            bgX += (2 * (speedX));
        else
            bgX += (speedX);
        bgY += (speedY);


        if (bgY <screenY/3)
            speedY = 0;
        if (bgY< (screenY-screenY/4))
        {
            speedY+=9;
        }
        else{
            bgY = screenY-screenY/4;
            speedY = -speedY;
        }

        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);

    }

}

