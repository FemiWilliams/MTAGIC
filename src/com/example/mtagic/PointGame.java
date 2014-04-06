package com.example.mtagic;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.graphics.Canvas;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.TransitionDrawable;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PointGame extends Activity {

	// for logging
	String userID = "test";
	BufferedWriter outFile;
	Random r = new Random();
	
	List<List<String>> allTargets;
	// keep track of which target we're on
	private long currentTargetTime = -1;
	private Rect currentTarget = null;
	private int targetNumber;
	private int numAttempts = 0;
	private int targets_needed;
	
	// set up grids
	private GridSquares grid; 
	private int screen_width = 0;
	private int screen_height = 0;
	private float xdpi = 0; // don't use ydpi because it's assumed to be mostly square and if not, we want it to be square anyway
	
	//parameters from other activities
	private int level;
	private int score;
	private String gameType;
	
	private LinearLayout point_canvas;
	private PointBoard mPointBoard = null;
	
	private Logger logger;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// load targets list
		loadTargets();

		// set up Grid
		Display display = getWindowManager().getDefaultDisplay();
		screen_width = display.getWidth();
        screen_height = display.getHeight();
		grid = new GridSquares(screen_width, screen_height, 3, 5, 10);
		
		// do view set-up
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		setContentView(R.layout.activity_point_game);
		
		point_canvas = (LinearLayout) findViewById(R.id.point_canvas);
		mPointBoard = new PointBoard(this);
		point_canvas.addView(mPointBoard);
		
		//receive the intent and bundled data
		Intent intent = getIntent();
		level = intent.getIntExtra("LEVEL", 0);
		score = intent.getIntExtra("SCORE", 0);
		
		
		userID = intent.getStringExtra("NAME");
		Log.d("note:", "name = " + userID);
		
		gameType = intent.getStringExtra("TYPE");
		targetNumber = intent.getIntExtra("CURRENT_TARGET", 0);
		
		if (level > 4){
			targets_needed = targetNumber + 18; 
		}else{
			targets_needed = targetNumber + 17;
		}
		
		
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		// draw the first target
		// translate the target to the right location

		Log.d("note:", "row = " + Integer.parseInt(allTargets.get(targetNumber).get(0)) + ", col = " +
				Integer.parseInt(allTargets.get(targetNumber).get(1)));
		this.currentTarget = grid.getLocation(
				Integer.parseInt(allTargets.get(targetNumber).get(0)),
				Integer.parseInt(allTargets.get(targetNumber).get(1)),
				Integer.parseInt(allTargets.get(targetNumber).get(2)),
				Boolean.parseBoolean(allTargets.get(targetNumber).get(4)));
		mPointBoard.moveFish(this.currentTarget);
		//setContentView(mPointBoard); //Caution
	}

	@Override
	public void onStart() {
		super.onStart();
		setUpLogFile(userID);
		// save the time the first target was visible and allowed to be touched by the user
		currentTargetTime = System.currentTimeMillis();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		logger.close();
	}
	
	// load all the targets to process
	private void loadTargets() {
		try {
			allTargets = new ArrayList<List<String>>();
			XmlResourceParser xpp = this.getAssets().openXmlResourceParser("res/xml/targets.xml");
			xpp.next();
            int eventType = xpp.getEventType();
            Log.d("note:","eventType = " + eventType);
            
            while (eventType != XmlPullParser.END_DOCUMENT) {

            	if (eventType == XmlPullParser.START_TAG) {
            		String elemName = xpp.getName();
            		Log.d("note:","elemName = " + elemName);

            		if (elemName.equals("target")) {
            			List<String> target = new ArrayList<String>();
            			target.add(xpp.getAttributeValue(null, "row"));
            			target.add(xpp.getAttributeValue(null, "column"));
            			//target.add(xpp.getAttributeValue(null, "android:drawable"));
            			// we need to first compute the size in pixels for the appropriate device
            			// http://developer.android.com/guide/practices/screens_support.html#dips-pels
            			// the desired target size in physical inches on the screen
            			float raw_target_size = Float.parseFloat(xpp.getAttributeValue(null, "size"));
            			// get the screen's xdpi and ydpi
            			//final float scale = getResources().getDisplayMetrics().density;
            			float xdpi = getResources().getDisplayMetrics().xdpi;
            			if (Build.MODEL.equals("VM760"))
            				xdpi = 180;
            			this.xdpi = xdpi;
            			final float ydpi = getResources().getDisplayMetrics().ydpi;
            			// convert the inches to pixels, based on dpi
            			//int scaled_target_size = (int) (raw_target_size * scale + 0.5f);
            			int scaled_target_size = (int)(raw_target_size * xdpi + 0.5f); // assume square for now, but it's not really
            			// store both scaled (in pixels) target size and raw target size (in inches)
            			target.add(""+scaled_target_size);
            			target.add(""+raw_target_size);
            			
            			//target.add(xpp.getAttributeValue(null, "size"));
            			target.add(xpp.getAttributeValue(null, "edge"));
            			allTargets.add(target);
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
	
	// set up and name the logfile
	private void setUpLogFile(String user_id) {
		
		File root = Environment.getExternalStorageDirectory();
		logger = new Logger(userID, this, root);
	}

	// write a log of this event
	protected void writeLogOfTouch(MotionEvent event, long time, boolean isTouchInside) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		sdf.format(new Date(System.currentTimeMillis()));
		logger.logTouch(event, time, isTouchInside, sdf, targetNumber, numAttempts, currentTargetTime, currentTarget, allTargets, screen_width, screen_height, xdpi);

		
		
		
	}
	
	public void processTouch(MotionEvent event) {
		// check if this touch event is within this target's bounding
		// box; if not, increment numAttempts but not targetNumber
		long time = System.currentTimeMillis();
		numAttempts++;
		if (currentTarget.contains((int) event.getX(), (int) event.getY())) {
			writeLogOfTouch(event, time, true);
			// success, reset vars and go to next target
			numAttempts = 0;
			score += 5;
			targetNumber++;
			
			mPointBoard.starburst(this.currentTarget);
			
			// at last target, call finish to exit
			if (targetNumber >= targets_needed) {
				
				logger.close();
				nextLevel();
			} else {
				// translate the target to the right location
				Log.d("note:", "row = " + Integer.parseInt(allTargets.get(targetNumber).get(0)) + ", col = " +
						Integer.parseInt(allTargets.get(targetNumber).get(1)));
				
				Log.d("note:", "Current Target: " + targetNumber);
				
				this.currentTarget = grid.getLocation(
						Integer.parseInt(allTargets.get(targetNumber).get(0)),
						Integer.parseInt(allTargets.get(targetNumber).get(1)),
						Integer.parseInt(allTargets.get(targetNumber).get(2)),
						Boolean.parseBoolean(allTargets.get(targetNumber).get(4)));

				// save the time this target was drawn
				this.currentTargetTime = System.currentTimeMillis();
				
				mPointBoard.moveFish(this.currentTarget);
			}
		} else {
			writeLogOfTouch(event, time, false);
			
		}
	}

	// disable the back button on the phone
	@Override
	public void onBackPressed() {
		
		//This crashes both MTAGIC and TargetTaskMain
		//It throws a NullPointerException; not sure what to do about it
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		//logger.logBack(sdf.format(new Date(System.currentTimeMillis())));
		
		
		
	}
	
	public void nextLevel(){
			Intent intent = new Intent(this, LevelComplete.class);
			intent.putExtra("LEVEL", level);
			intent.putExtra("SCORE", score);
			intent.putExtra("NAME", userID);
			intent.putExtra("TYPE", gameType);
			intent.putExtra("CURRENT_TARGET", targetNumber);
			startActivity(intent);
			finish();
		}
	
}