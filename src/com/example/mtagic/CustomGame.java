package com.example.mtagic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/*
 * CustomGame.java
 * This class allows you to create the settings
 * for a custom point or gesture game.
 */
 

//NOTE: buttons defined in xml file

public class CustomGame extends Activity {
	
	//data to pass on to games
	private final static String GAME_TYPE = "TYPE";
	private int level;
	private int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_custom_game);		
	}

	
	//linked to the startGesture button, this creates a new gesture game
	public void startGesture(View view){
		
		EditText scoreText = (EditText) findViewById(R.id.starting_score);
		score = Integer.parseInt(scoreText.getText().toString());
		
		EditText levelText = (EditText) findViewById(R.id.starting_level);
		level = Integer.parseInt(levelText.getText().toString());
		
		
		Intent intent = new Intent(this, NameActivity.class);
		intent.putExtra("LEVEL", level);
		intent.putExtra("SCORE", score);
		intent.putExtra(GAME_TYPE, "GESTURE");
		startActivity(intent);
		
	}
	
	//ditto for the startPoint button and point game
	public void startPoint(View view){
		
		EditText scoreText = (EditText) findViewById(R.id.starting_score);
		score = Integer.parseInt(scoreText.getText().toString());
		
		EditText levelText = (EditText) findViewById(R.id.starting_level);
		level = Integer.parseInt(levelText.getText().toString());
		
		Intent intent = new Intent(this, NameActivity.class);
		intent.putExtra("LEVEL", level);
		intent.putExtra("SCORE", score);
		intent.putExtra(GAME_TYPE, "POINT");
		startActivity(intent);
	}

}
