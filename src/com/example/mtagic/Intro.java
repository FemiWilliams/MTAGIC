package com.example.mtagic;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.content.Intent;
import android.content.pm.ActivityInfo;

/* 
 * Intro.java
 * Pretty self-explanatory, this is the introduction to the entire application.
 * It creates three buttons, leading us down the road to a standard point game,
 * a standard gesture game, or a custom version of either game.
 */

public class Intro extends Activity {
	//Default values for each game
	private final int defaultLevel = 1;
	private final int defaultScore = 0;
	public final static String DEFAULT_LEVEL = "LEVEL";
	public final static String DEFAULT_SCORE = "SCORE";
	private final static String GAME_TYPE = "TYPE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_intro);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.intro, menu);
		return true;
	}
	
	//Each button sends out an intent to the appropriate activity
	//bundling with the intent the appropriate values.
	
	public void startGesture(View view){
		Intent intent = new Intent(this, NameActivity.class);
		intent.putExtra(DEFAULT_LEVEL, defaultLevel);
		intent.putExtra(DEFAULT_SCORE, defaultScore);
		intent.putExtra(GAME_TYPE, "GESTURE");
		startActivity(intent);
		
	}
	
	public void startPoint(View view){
		Intent intent = new Intent(this, NameActivity.class);
		intent.putExtra(DEFAULT_LEVEL, defaultLevel);
		intent.putExtra(DEFAULT_SCORE, defaultScore);
		intent.putExtra(GAME_TYPE, "POINT");
		startActivity(intent);
	}
	
	/*
	 * You don't need to bundle anything with this intent because
	 * you'll be creating your own settings in that activity.
	 * That's why it's called a CUSTOM game ;)
	 */
	
	public void startCustom(View view){
		Intent intent = new Intent(this, CustomGame.class);
		startActivity(intent);
	}

	// disable the back button on the phone
		@Override
		public void onBackPressed() {
		}
}
