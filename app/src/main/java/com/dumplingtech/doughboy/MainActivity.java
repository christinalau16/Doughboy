package com.dumplingtech.doughboy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String PREFS_NAME = "MyPrefsFile";
    static int highScore;
    static SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore preferences
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int hs = settings.getInt("highScore", 0);
        setHighScore(hs);

        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        startActivity(intent);
    }

    public static int getHighScore(){
        return highScore;
    }

    public static void setHighScore(int hs){
        highScore = hs;
        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("highScore", highScore);

        // Commit the edits!
        editor.commit();
    }
}
