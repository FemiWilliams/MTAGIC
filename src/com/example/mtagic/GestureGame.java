package com.example.mtagic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

/*
 * GestureGame.java
 * The activity for the gesture game, this creates the game canvas
 * and then logs the gestures made.
 */
 

public class GestureGame extends Activity implements OnTouchListener, OnClickListener {

	private GestureBoard mGestureBoard;
	private int level;
	private int score;
	private int lineWidth;
	private int screen_width;
	private int screen_height;
	private int gestures_needed = 20;
	private String name;
	private String gameType;
	private String color;
	
	public Button button_ok;
	
	private Logger lg;
	File root = Environment.getExternalStorageDirectory();
	
	private LinearLayout gesture_canvas;
			
	List<String> allGestures;
 	// keep track of which iteration and gesture we're on
 	public int currentIteration = 1;
 	public int currentGestureNumber = 0;
 	public Gesture currentGesture = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//This is a depreciated method of getting the screen dimensions
		//But it's quick and easy. Just go with it.
		Display display = getWindowManager().getDefaultDisplay();
		screen_width = display.getWidth();
        screen_height = display.getHeight();
		
		loadGestures();
		
        setContentView(R.layout.activity_gesture_game);
	
		//receive the intent 
		Intent intent = getIntent();
		level = intent.getIntExtra("LEVEL", 0);
		score = intent.getIntExtra("SCORE", 0);
		name = intent.getStringExtra("NAME");
		gameType = intent.getStringExtra("TYPE");
		lineWidth = intent.getIntExtra("LINE_WIDTH", 1);
		color = intent.getStringExtra("COLOR");
		
		//Programmatically placing the gesture on the screen
		//Instead of using the XML
		//So that we can pass things in the constructor.
        gesture_canvas = (LinearLayout) findViewById(R.id.gesture_canvas);
        mGestureBoard = new GestureBoard(this, lineWidth, color, currentGestureNumber);
        gesture_canvas.addView(mGestureBoard);
		

		
		
		lg = new Logger(name, screen_width, screen_height, level);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		//loads a gesture from the list into the currentgesture variable 
		currentGesture = new Gesture(allGestures.get(currentGestureNumber), currentIteration);
	}
	
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event){
		return true;
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
		getMenuInflater().inflate(R.menu.gesture_game, menu);
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
	
	//loads the gesture list from the xml file into a linked list
	private void loadGestures() {
		try {
			allGestures = new ArrayList<String>();
			XmlResourceParser xpp = this.getAssets().openXmlResourceParser("res/xml/gestures.xml");
			xpp.next();
            int eventType = xpp.getEventType();
            Log.d("note:","eventType = " + eventType);
            
            while (eventType != XmlPullParser.END_DOCUMENT) {

               if (eventType == XmlPullParser.START_TAG) {
            	   String elemName = xpp.getName();
            	   Log.d("note:","elemName = " + elemName);
                   
                   if (elemName.equals("gesture")) {
                	   allGestures.add(xpp.getAttributeValue(null, "name"));
                   }
               }
               eventType = xpp.next();
            }
            
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//clear the screen, log the current gesture
	public void next_gesture(View view){
		mGestureBoard.Clear();
		Logger lg = new Logger(name, screen_width, screen_height, level);
		lg.logGesture(this, currentGesture, root);
		
		currentGestureNumber ++;
		if(currentGestureNumber < 20){
		currentGesture = new Gesture(allGestures.get(currentGestureNumber), currentIteration);
		}
		//Log.d("note:", "Current Gesture: " + allGestures.get(currentGestureNumber));
		score += 5;
		
		mGestureBoard.starburst();
		
		if (currentGestureNumber >= gestures_needed){
			nextLevel();
		}else{
		mGestureBoard.next(currentGestureNumber);
		}
		
	}
	
	public void nextLevel(){
		
		Log.d("note:", "End of Level Gesture Number" + Integer.toString(currentGestureNumber));
		
		Intent intent = new Intent(this, LevelComplete.class);
		intent.putExtra("LEVEL", level);
		intent.putExtra("SCORE", score);
		intent.putExtra("NAME", name);
		intent.putExtra("TYPE", gameType);
		intent.putExtra("COLOR", color);
		intent.putExtra("LINE_WIDTH", lineWidth);
		startActivity(intent);
		finish();
	}
	
	// disable the back button on the phone
	@Override
	public void onBackPressed() {
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
}