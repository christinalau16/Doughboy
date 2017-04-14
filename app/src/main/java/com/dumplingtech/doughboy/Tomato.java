package com.dumplingtech.doughboy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

class Tomato extends Enemy{

    private Background bg = SpriteSheetView.getBg1();

    Tomato(Context context, int screenX, int screenY, int x, int y){

        super.frameWidth = 160;
        super.frameHeight = 120;

        super.screenX = screenX;
        super.screenY = screenY;

        // Initialize the bitmap
        bitmapBack = BitmapFactory.decodeResource(context.getResources(), R.drawable.tomatored);

        // stretch the bitmap to a size appropriate for the screen resolution
        bitmapBack = Bitmap.createScaledBitmap(bitmapBack,
                frameWidth,
                frameHeight,
                false);

        bgX = x;
        bgY = y;

        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);
    }

    public void update(long fps) {
        bgX += (bg.getSpeedX() );
        if (bgX <= -screenX) {
            bgX += screenX*2;
        }
        super.rect = new Rect(bgX, bgY, bgX + frameWidth, bgY + frameHeight);

    }


}

