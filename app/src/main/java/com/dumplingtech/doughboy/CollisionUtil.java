package com.dumplingtech.doughboy;

import android.graphics.Color;
import android.graphics.Rect;

class CollisionUtil {
    private static int i = 0;
    private static int j = 0;

    static boolean isCollisionDetected(Doughboy doughboy, Enemy sprite2) {
        Rect bounds1 = doughboy.getRect();
        Rect bounds2 = sprite2.getRect();

        if (Rect.intersects(bounds1, bounds2)) {
            Rect collisionBounds = getCollisionBounds(bounds1, bounds2);
            for (i = collisionBounds.left; i < collisionBounds.right; i++) {
                for (j =  collisionBounds.top; j < collisionBounds.bottom; j++) {
                    int sprite1Pixel = getBitmapPixel(doughboy, i, j);
                    int sprite2Pixel = getBitmapPixel(sprite2, i, j);
                    if (isFilled(sprite1Pixel) && isFilled(sprite2Pixel))
                        return true;
                }
            }
        }
        return false;
    }

    private static int getBitmapPixel(Doughboy sprite, int i, int j) {
        return sprite.getBitmap().getPixel((i - (int) sprite.getX()) + SpriteSheetView.currentFrame * (int) sprite.getWidth(), j - (int) sprite.getY());
    }

    private static int getBitmapPixel(Enemy sprite, int i, int j) {
        return sprite.getBitmap().getPixel((i -  sprite.getBgX()), (j -  sprite.getBgY()));
    }

    private static Rect getCollisionBounds(Rect rect1, Rect rect2) {
        int left =  Math.max(rect1.left, rect2.left);
        int top = Math.max(rect1.top, rect2.top);
        int right =  Math.min(rect1.right, rect2.right);
        int bottom = Math.min(rect1.bottom, rect2.bottom);
        return new Rect(left, top, right, bottom);
    }

    private static boolean isFilled(int pixel) {
        return pixel != Color.TRANSPARENT;
    }
}
