package com.dumplingtech.doughboy;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Random;
class SpriteSheetView extends SurfaceView implements Runnable {

    Thread gameThread = null;

    SurfaceHolder ourHolder;

    volatile boolean playing;

    Canvas canvas;
    Paint paint;

    // This variable tracks the game frame rate
    long fps;

    // This is used to help calculate the fps
    private long timeThisFrame;

    private int screenX;
    private int screenY;

    private Doughboy doughboy;

    private static Background bg1;
    private static Background bg2;

    private int numCheeses = 5;
    private int numTomatoes = 5 + 2;
    private int numOlives = 5 + 2 + 1;
    private int numPepperonis = 5 + 2 + 1 + 1;
    private int numMushrooms = 5 + 2 + 1 + 1 + 2;
    private Enemy[] enemies = new Enemy[numMushrooms];


    // How many frames are there on the sprite sheet?
    private int frameCount = 3;
    // Start at the first frame - where else?
    public static int currentFrame = 0;
    // What time was it when we last changed frames
    private long lastFrameChangeTime = 0;
    // How long should each frame last
    private int frameLengthInMilliseconds = 100;

    private int jumpPressed = 0;

    // A rectangle to define an area of the sprite sheet that represents 1 frame
    private Rect frameToDraw;
    // A rect that defines an area of the screen on which to draw doughboy
    RectF whereToDraw;

    private Context context;

    Bitmap bitmapLeft;
    Bitmap bitmapRight;
    Bitmap bitmapUp;
    Bitmap bitmapReplay;
    Bitmap bitmapPizza;

    boolean lost = false;

    AssetManager assets;

    public SpriteSheetView(Context context, int x, int y, AssetManager assets) {
        super(context);
        this.context = context;
        this.assets = assets;
        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;

        prepareLevel();
    }

    private void prepareLevel() {
        lost = false;
        jumpPressed = 0;
        //background
        bg1 = new Background(context, screenX, screenY, 0, 0);
        bg2 = new Background(context, screenX, screenY, screenX, 0);

        //doughboy
        doughboy = new Doughboy(context, screenX, screenY, frameCount);
        Doughboy.resetTotalDistance();
        frameToDraw = new Rect(
                0,
                0,
                (int) doughboy.getWidth(),
                (int) doughboy.getHeight());
        whereToDraw = new RectF(
                doughboy.getX(), 0,
                doughboy.getX() + doughboy.getWidth(),
                doughboy.getHeight());

        //enemies
        for (int j = 0; j < enemies.length; j++) {
            if (j < numCheeses) {
                Random generator = new Random();
                int xAnswer = generator.nextInt(screenX + 500) + screenX / 2;
                int yAnswer = generator.nextInt(800);
                enemies[j] = new Cheese(context, screenX, screenY, xAnswer, -yAnswer);
            } else if (j >= numCheeses && j < numTomatoes) {
                Random generator = new Random();
                int xAnswer = generator.nextInt(screenX + 500) + screenX / 2;
                int yAnswer = generator.nextInt(screenY - 100);
                enemies[j] = new Tomato(context, screenX, screenY, xAnswer, yAnswer);
            } else if (j >= numTomatoes && j < numOlives) {
                Random generator = new Random();
                int xAnswer = generator.nextInt(screenX + 500) + screenX;
                enemies[j] = new Olive(context, screenX, screenY, xAnswer, (screenY * 2 / 3));
            } else if (j >= numOlives && j < numPepperonis) {
                Random generator = new Random();
                int xAnswer = generator.nextInt(screenX + 500) + screenX;
                enemies[j] = new Pepperoni(context, screenX, screenY, xAnswer, screenY - screenY / 4 - 50);
            } else {
                Random generator = new Random();
                int xAnswer = generator.nextInt(screenX + 500) + screenX / 2;
                enemies[j] = new Mushroom(context, screenX, screenY, xAnswer, screenY - screenY / 4);
            }
        }
        //arrows
        bitmapLeft = BitmapFactory.decodeResource(this.getResources(), R.drawable.left);
        bitmapLeft = Bitmap.createScaledBitmap(bitmapLeft,
                250,
                250,
                false);
        bitmapRight = BitmapFactory.decodeResource(this.getResources(), R.drawable.right);
        bitmapRight = Bitmap.createScaledBitmap(bitmapRight,
                250,
                250,
                false);
        bitmapUp = BitmapFactory.decodeResource(this.getResources(), R.drawable.up);
        bitmapUp = Bitmap.createScaledBitmap(bitmapUp,
                250,
                250,
                false);
        //lost screen
        bitmapReplay = BitmapFactory.decodeResource(this.getResources(), R.drawable.replaybutton);
        bitmapReplay = Bitmap.createScaledBitmap(bitmapReplay,
                400,
                400,
                false);
        bitmapPizza = BitmapFactory.decodeResource(this.getResources(), R.drawable.pizza);
        bitmapPizza = Bitmap.createScaledBitmap(bitmapPizza,
                screenX,
                screenY,
                false);

    }

    @Override
    public synchronized void run() {
        while (playing) {

            // Capture the current time in milliseconds in startFrameTime
            long startFrameTime = System.currentTimeMillis();

            // Update the frame
            update();

            // Draw the frame
            draw();

            // Calculate the fps this frame
            // We can then use the result to
            // time animations and more.
            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 2000 / timeThisFrame;
            }

        }

    }

    public synchronized void update() {
        doughboy.update();
        bg1.update();
        bg2.update();
        for (int j = 0; j < enemies.length; j++) {
            if (enemies[j].getBgX() + enemies[j].getFrameWidth() > 0 && enemies[j].getBgY() < screenY) {
                enemies[j].update(fps);
                if (CollisionUtil.isCollisionDetected(doughboy, enemies[j])) {
                    //lost = true;
                    j = enemies.length;
                }
            } else {
                if (j < numCheeses) {
                    Random generator = new Random();
                    int xAnswer = generator.nextInt(screenX + 300);
                    int yAnswer = generator.nextInt(500);
                    enemies[j].setBgY(-yAnswer);
                    enemies[j].setBgX(xAnswer);
                } else if (j >= numCheeses && j < numTomatoes) {
                    Random generator = new Random();
                    int xAnswer = generator.nextInt(screenX * 2) + screenX;
                    int yAnswer = generator.nextInt(screenY - 200);
                    enemies[j].setBgY(yAnswer);
                    enemies[j].setBgX(xAnswer);
                } else if (j >= numTomatoes && j < numOlives) {
                    Random generator = new Random();
                    int xAnswer = generator.nextInt(screenX * 2) + screenX;
                    enemies[j].setBgX(xAnswer);
                } else if (j >= numOlives && j < numPepperonis) {
                    Random generator = new Random();
                    int xAnswer = generator.nextInt(screenX * 2) + screenX;
                    enemies[j].setBgX(xAnswer);
                } else {
                    Random generator = new Random();
                    int xAnswer = generator.nextInt(screenX * 2) + screenX;
                    enemies[j].setBgX(xAnswer);
                    enemies[j].resetCurrentFrame();
                }
            }
        }

    }

    public synchronized void draw() {

        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 26, 128, 182));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255, 249, 129, 100));

            //background
            canvas.drawBitmap(bg1.getBitmap(), bg1.getBgX(), bg1.getBgY(), paint);
            canvas.drawBitmap(bg2.getBitmap(), bg2.getBgX(), bg2.getBgY(), paint);

            //grass
            paint.setColor(Color.argb(230, 0, 153, 0));
            canvas.drawRect(0, screenY - screenY / 4, screenX, screenY, paint);
            paint.setColor(Color.argb(255, 249, 129, 100));

            //doughboy
            whereToDraw.set((int) doughboy.getX(),
                    (int) doughboy.getY(),
                    (int) doughboy.getX() + doughboy.getWidth(),
                    (int) doughboy.getY() + doughboy.getHeight());
            getCurrentFrame();
            canvas.drawBitmap(doughboy.getBitmap(),
                    frameToDraw,
                    whereToDraw, paint);

            //enemies
            for (int j = 0; j < enemies.length; j++) {
                if (enemies[j].getBgX() < screenX + 300 && enemies[j].getBgY() < screenY) {
                    if (j < numPepperonis)
                        canvas.drawBitmap(enemies[j].getBitmap(), enemies[j].getBgX(), enemies[j].getBgY(), paint);
                    else {
                        canvas.drawBitmap(enemies[j].getBitmap(), enemies[j].getFrameToDrawMushroom(), enemies[j].getWhereToDrawMushroom(), paint);

                    }
                }
            }
            //arrows
            canvas.drawBitmap(bitmapLeft, 40, screenY - 250, paint);
            canvas.drawBitmap(bitmapRight, 305, screenY - 250, paint);
            canvas.drawBitmap(bitmapUp, screenX - screenX / 6, screenY - 250, paint);

            //score
            paint.setColor(Color.argb(255, 0, 0, 0));
            Typeface tf = Typeface.createFromAsset(assets, "RockSalt.ttf");
            paint.setTypeface(tf);
            paint.setTextSize(50);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setFakeBoldText(true);
            canvas.drawText("Score: " + (int) (Doughboy.totalDistance() / 100), 20, 80, paint);

            if (lost) {
                playing = false;
                //background
                canvas.drawBitmap(bg1.getBitmap(), bg1.getBgX(), bg1.getBgY(), paint);
                canvas.drawBitmap(bg2.getBitmap(), bg2.getBgX(), bg2.getBgY(), paint);

                //grass
                paint.setColor(Color.argb(230, 0, 153, 0));
                canvas.drawRect(0, screenY - 100 * 2 - 10, screenX, screenY, paint);

                //pizza
                paint.setColor(Color.argb(255, 0, 0, 0));
                canvas.drawBitmap(bitmapPizza, 0, 0, paint);

                //red box for text
                paint.setColor(Color.argb(230, 84, 3, 3));
                canvas.drawRect(screenX / 5, screenY / 4, screenX - screenX / 5, screenY - screenY / 4, paint);

                //replay button
                paint.setColor(Color.argb(255, 255, 255, 255));
                canvas.drawBitmap(bitmapReplay, screenX / 2 - 200, screenY - screenY / 3 - 100, paint);

                //score
                paint.setTextAlign(Paint.Align.CENTER);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(100);
                canvas.drawText("Score: " + (int) (Doughboy.totalDistance() / 100), screenX / 2, screenY / 3 + 100, paint);

                //high score
                if ((int) (Doughboy.totalDistance() / 100) > MainActivity.getHighScore())
                    MainActivity.setHighScore((int) (Doughboy.totalDistance() / 100));
                paint.setTextSize(70);
                canvas.drawText("High Score: " + MainActivity.getHighScore(), screenX / 2, screenY / 2 + 100, paint);
            }

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }

    }

    // If SimpleGameEngine Activity is paused/stopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
        }

    }

    // If SimpleGameEngine Activity is started theb
    // start our thread.
    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void getCurrentFrame() {

        long time = System.currentTimeMillis();
        if (doughboy.isMoving()) {// Only animate if bob is moving
            if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame++;
                if (currentFrame >= frameCount) {

                    currentFrame = 0;
                }
            }
        }
        //update the left and right values of the source of
        //the next frame on the spritesheet
        frameToDraw.left = currentFrame * (int) doughboy.getWidth();
        frameToDraw.right = frameToDraw.left + (int) doughboy.getWidth();

    }

    public static Background getBg1() {
        return bg1;
    }

    public static Background getBg2() {
        return bg2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int count;
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                count = motionEvent.getPointerCount();
                for (int pointerIndex = 0; pointerIndex < count; pointerIndex++) {
                    if (lost && motionEvent.getX(pointerIndex) > screenX / 2 - 200 && motionEvent.getX(pointerIndex) < screenX / 2 + 200
                            && motionEvent.getY(pointerIndex) > screenY - screenY / 3 - 100 && motionEvent.getY(pointerIndex) < screenY - screenY / 3 + 300) {
                        prepareLevel();
                        playing = true;
                        gameThread = new Thread(this);
                        gameThread.start();
                    } else {
                        if (motionEvent.getX(pointerIndex) > 40 && motionEvent.getX(pointerIndex) < 40 + 250 &&
                                motionEvent.getY(pointerIndex) > screenY - 250 &&
                                motionEvent.getY(pointerIndex) < screenY)
                            doughboy.moveLeft();
                        else if (motionEvent.getX(pointerIndex) > 305 && motionEvent.getX(pointerIndex) < 305 + 250 &&
                                motionEvent.getY(pointerIndex) > screenY - 250 &&
                                motionEvent.getY(pointerIndex) < screenY)
                            doughboy.moveRight();
                        if (motionEvent.getX(pointerIndex) > screenX - screenX / 6
                                && motionEvent.getX(pointerIndex) < screenX - screenX / 6 + 250
                                && motionEvent.getY(pointerIndex) > screenY - 250
                                && motionEvent.getY(pointerIndex) < screenY) {
                            if (doughboy.getY() == (screenY - screenY / (3.5f)))
                                jumpPressed = 0;
                            if (doughboy.getY() > 10 && jumpPressed < 2) {
                                doughboy.jump();
                                jumpPressed++;
                            }
                        }
                    }
                }
                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_MOVE:
                count = motionEvent.getPointerCount();
                for (int pointerIndex = 0; pointerIndex < count; pointerIndex++) {
                    if (motionEvent.getX(pointerIndex) < 40 && motionEvent.getX(pointerIndex) > 40 + 250 &&
                            motionEvent.getY(pointerIndex) < screenY - 250 &&
                            motionEvent.getY(pointerIndex) > screenY) {
                        doughboy.stopLeft();
                        bg1.setSpeedX(0);
                        bg2.setSpeedX(0);
                    } else if (motionEvent.getX(pointerIndex) < 305 && motionEvent.getX(pointerIndex) > 305 + 250 &&
                            motionEvent.getY(pointerIndex) < screenY - 250 &&
                            motionEvent.getY(pointerIndex) > screenY) {
                        doughboy.stopRight();
                        bg1.setSpeedX(0);
                        bg2.setSpeedX(0);
                    }
                    if (motionEvent.getX(pointerIndex) < screenX - screenX / 6
                            && motionEvent.getX(pointerIndex) > screenX - screenX / 6 + 250
                            && motionEvent.getY(pointerIndex) < screenY - 250
                            && motionEvent.getY(pointerIndex) > screenY) {
                        doughboy.setJumped(false);
                    }
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                count = motionEvent.getPointerCount();
                for (int pointerIndex = 0; pointerIndex < count; pointerIndex++) {
                    if (motionEvent.getX(pointerIndex) < 305 && motionEvent.getX(pointerIndex) > 305 + 250 && motionEvent.getY(pointerIndex) < screenY - 250 && motionEvent.getY(pointerIndex) > screenY) {
                        doughboy.stopRight();
                    } else if (motionEvent.getX(pointerIndex) < 40 && motionEvent.getX(pointerIndex) > 40 + 250 &&
                            motionEvent.getY(pointerIndex) < screenY - 250 &&
                            motionEvent.getY(pointerIndex) > screenY) {
                        doughboy.stopLeft();
                    } else {
                        doughboy.setJumped(false);
                    }
                }
                break;

        }
        return true;
    }
}
