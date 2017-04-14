package com.dumplingtech.doughboy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayMessageActivity extends Activity{

    SpriteSheetView gameView;
    public static MediaPlayer bkgrdmsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_display_message);
        layout.addView(textView);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        //  Music loop
        bkgrdmsc = MediaPlayer.create(DisplayMessageActivity.this, R.raw.eight);
        bkgrdmsc.setLooping(true);
        bkgrdmsc.start();

        // Initialize gameView and set it as the view
        gameView = new SpriteSheetView(this, size.x, size.y, getAssets());
        setContentView(gameView);


    }

    // This method executes when the player starts the game
    @Override
    protected void onResume() {
        super.onResume();
        bkgrdmsc.start();
        gameView.resume();
    }

    // This method executes when the player quits the game
    @Override
    protected void onPause() {
        super.onPause();
        bkgrdmsc.release();
        finish();
        gameView.pause();
    }
}
