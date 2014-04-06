package com.example.mtagic;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/*
 * NameActivity.java
 * The second step of initiating a new game, this activity asks
 * for the users name, which is important to the creation of 
 * the logging file. We then pass on the name, level, and score
 * to the appropriate game.
 */

public class NameActivity extends Activity {
	
	private int level;
	private int score;
	private String gameType;
	private String name = "test";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_name);
		
		
		
		Intent intent = getIntent();
		level = intent.getIntExtra("LEVEL", 0);
		score = intent.getIntExtra("SCORE", 0);
		gameType = intent.getStringExtra("TYPE");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.name, menu);
		return true;
	}
	
	// disable the back button on the phone
		@Override
		public void onBackPressed() {
		}
	
	//starts the correct game based on which button was clicked in the Intro/Custom
	public void gameStart(View view){
		
		EditText nameText = (EditText) findViewById(R.id.user_name);
		if(nameText.getText().length() < 1){
			Builder alert = new AlertDialog.Builder(this);
			alert.setTitle("Invalid Entry");
			alert.setMessage("Please enter a name.");
			alert.setPositiveButton("OK",null);
			alert.show();    
		}
		else{
			
			name = nameText.getText().toString();
			Log.d("note:", "name = " + name);
		
			if(gameType.equals("GESTURE")){
				Intent intent = new Intent(this, GestureSettings.class);
				intent.putExtra("LEVEL", level);
				intent.putExtra("SCORE", score);
				intent.putExtra("NAME", name);
				intent.putExtra("TYPE", gameType);
				startActivity(intent);
				finish();
			}
		
			if(gameType.equals("POINT")){
				Intent intent = new Intent(this, PointGame.class);
				intent.putExtra("LEVEL", level);
				intent.putExtra("SCORE", score);
				intent.putExtra("NAME", name);
				intent.putExtra("TYPE", gameType);
				startActivity(intent);
				finish();
			}
		}
	}

}
