package com.example.mtagic;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/* 
 * LevelComplete.java
 * This activity is to be called after each level is completed.
 * It displays the score, a congratulatory message, and then
 * increments the level before passing it along to the appropriate
 * activity.
 */

public class LevelComplete extends Activity implements OnClickListener{
	
	private int level;
	private int score;
	private int lineWidth;
	private int currentPointNumber;
	private String gameType;
	private String name;
	private String color;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_level_complete);		
		
		
		//receive the intent and bundled items
		Intent intent = getIntent();
		level = intent.getIntExtra("LEVEL", 0);
		score = intent.getIntExtra("SCORE", 0);
		gameType = intent.getStringExtra("TYPE");
		name = intent.getStringExtra("NAME");
		
		if (gameType.equalsIgnoreCase("GESTURE")){
			lineWidth = intent.getIntExtra("LINE_WIDTH", 0);
			color = intent.getStringExtra("COLOR");			
		}
		
		if (gameType.equalsIgnoreCase("POINT")){
			currentPointNumber = intent.getIntExtra("CURRENT_TARGET", 0);
		}
		
		//set up the next button; arm it with a listener.
		Button b = (Button) findViewById(R.id.button_next);
		b.setOnClickListener(this);
				
		TextView textView = (TextView) findViewById(R.id.txt_complete);
		
		
		if (level >= 6){
			textView.setTextSize(20);
			textView.setText("GAME OVER" + "\nCONGRATULATIONS" + "\n" + score + " Points Earned");
		}else{
			textView.setTextSize(40);
		textView.setText("Level " + level + " complete!" + "\nScore: " + score);
		}
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_complete, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onClick(View v){
		if (v.getId() == R.id.button_next){
			if(level >= 6){
				finish();
			}else if(gameType.equalsIgnoreCase("gesture")){
				startGesture();
			}
			else if(gameType.equalsIgnoreCase("point")){
				startPoint();
			}
		}
	}
	
	public void startPoint(){
		
		Intent intent = new Intent(this, PointGame.class);
		intent.putExtra("LEVEL", level + 1);
		intent.putExtra("SCORE", score);
		intent.putExtra("NAME", name);
		intent.putExtra("TYPE", gameType);
		intent.putExtra("CURRENT_TARGET", currentPointNumber);
		startActivity(intent);
		finish();
	
	}
	
	public void startGesture(){
		
		Intent intent = new Intent(this, GestureSettings.class);
		intent.putExtra("LEVEL", level + 1);
		intent.putExtra("SCORE", score);
		intent.putExtra("NAME", name);
		intent.putExtra("TYPE", gameType);
		intent.putExtra("LINE_WIDTH", lineWidth);
		intent.putExtra("COLOR", color);
		startActivity(intent);
		finish();
	}
	
	// disable the back button on the phone
		@Override
		public void onBackPressed() {
		}

}