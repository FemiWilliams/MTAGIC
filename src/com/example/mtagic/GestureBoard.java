package com.example.mtagic;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/* 
 * GestureBoard.java
 * The canvas of the game, this creates the gesture objects,
 * draws the points to the screen and 
 * sends them to the main activity for logging.
 */

public class GestureBoard extends View{
	 
	//for the lines to be drawn
	private ArrayList<Path> _graphics = new ArrayList<Path>();
	public Path currentPath;
	private Paint mPaint;
	
	//links to the gesturegame instance for context 
	public GestureGame GG;
	
	//for the starburst
	private Bitmap starburst = null;
	private boolean starShown = false;
	private Thread thread;

	//for the gestures being shown
	private Bitmap[] gestureImg = new Bitmap[20];
	private boolean gestureShown = true;
	private int currentGimg;
	
	//background image
	private Bitmap mTree = null;
	
	
	private int lineWidth = 3;
	private String mycolor;
	
	private Handler h;
	private int delay = 1050;
	
	/*
	 * constructor
	 * Accepts the parent gestureGame, an into for the line width, the color for the line
	 * and an int for the current gesture
	*/
	public GestureBoard(GestureGame parent, int lw, String color, int current){
		super(parent);
		GG = parent;
		
		lineWidth = lw;
		mycolor = color;
		currentGimg = current;
		Log.d("note:", "Current Gesture Number" + Integer.toString(currentGimg));
		
		setupPaint();
		
		h = new Handler();
		
		this.setOnTouchListener(new CustomOnTouchListener());
				
		mTree = BitmapFactory.decodeResource(getResources(), R.drawable.tree);
		starburst= BitmapFactory.decodeResource(getResources(), R.drawable.starburst);
		assignGestures();
	}
	
	//sets up the paint object for drawing lines
	public void setupPaint() {
		mPaint = new Paint();  
		mPaint.setDither(true);  
		mPaint.setColor(Color.parseColor(mycolor));  
		mPaint.setStyle(Paint.Style.STROKE);  
		mPaint.setStrokeJoin(Paint.Join.ROUND);  
		mPaint.setStrokeCap(Paint.Cap.ROUND);  
		mPaint.setStrokeWidth(lineWidth); 
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		mTree = Bitmap.createScaledBitmap(mTree, getWidth(), getHeight(), false);
		canvas.drawBitmap(mTree, 0, 0, null);
		
		if(gestureShown == true){
		canvas.drawBitmap(gestureImg[currentGimg], 100, 180, null);
		}
		
		Log.d("note:", "Current Gesture Number" + Integer.toString(currentGimg));
		
		if (starShown == true){	
			canvas.drawBitmap(starburst, 100, 180, null);			
		}
		
		for (Path path : _graphics) {   
		    canvas.drawPath(path, mPaint);  
		  }  
		
		h.postDelayed(r, delay);
		
	}
	
	//clear the canvas and draw again; this clears up the pen drawing
	public void Clear() {
		_graphics.clear();
		invalidate();
	}
	
	//loads all gesture images into an array, so we can draw according to the current gesture
	public void assignGestures(){
		gestureImg[0] = BitmapFactory.decodeResource(getResources(), R.drawable.a);
		gestureImg[1] = BitmapFactory.decodeResource(getResources(), R.drawable.e);
		gestureImg[2] = BitmapFactory.decodeResource(getResources(), R.drawable.k);
		gestureImg[3] = BitmapFactory.decodeResource(getResources(), R.drawable.q);
		gestureImg[4] = BitmapFactory.decodeResource(getResources(), R.drawable.x);
		gestureImg[5] = BitmapFactory.decodeResource(getResources(), R.drawable.two);
		gestureImg[6] = BitmapFactory.decodeResource(getResources(), R.drawable.four);
		gestureImg[7] = BitmapFactory.decodeResource(getResources(), R.drawable.five);
		gestureImg[8] = BitmapFactory.decodeResource(getResources(), R.drawable.seven);
		gestureImg[9] = BitmapFactory.decodeResource(getResources(), R.drawable.eight);
		gestureImg[10] = BitmapFactory.decodeResource(getResources(), R.drawable.line);
		gestureImg[11] = BitmapFactory.decodeResource(getResources(), R.drawable.plus);
		gestureImg[12] = BitmapFactory.decodeResource(getResources(), R.drawable.arch);
		gestureImg[13] = BitmapFactory.decodeResource(getResources(), R.drawable.arrowhead);
		gestureImg[14] = BitmapFactory.decodeResource(getResources(), R.drawable.checkmark);
		gestureImg[15] = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
		gestureImg[16] = BitmapFactory.decodeResource(getResources(), R.drawable.square);
		gestureImg[17] = BitmapFactory.decodeResource(getResources(), R.drawable.triangle);
		gestureImg[18] = BitmapFactory.decodeResource(getResources(), R.drawable.diamond);
		gestureImg[19] = BitmapFactory.decodeResource(getResources(), R.drawable.heart);
	}
	
	//changes the visibility of the gesture and starburst, and affects the delay of the draw loop
	public void starburst(){
		starShown = true;
		gestureShown = false;
		delay = 2050;
	}
	
	public void next(int current){
		currentGimg = current;
		Log.d("note:", "Current Gesture Number" + Integer.toString(currentGimg));
		invalidate();
	}
	
	// this internal class handles creating the Gesture object from touch events received by the main class
			private class CustomOnTouchListener implements OnTouchListener {

				@SuppressWarnings("null")
				public boolean onTouch(View v, MotionEvent ev) {
					final float x = ev.getX();
					final float y = ev.getY();
					final float pressure = ev.getPressure();
					final float size = ev.getSize();
					final float eventTime = ev.getEventTime();
					
					Log.d("note:", GG.currentGesture.name + " event x = " + x + ", y = " + y + ", p = " + pressure + ", s = " + size + ", t = " + eventTime);
					final int actions = ev.getAction();

					switch (actions) {
						case MotionEvent.ACTION_DOWN: {			
							// pen down -- start new stroke and add first point
							GG.currentGesture.NewStroke();
							GG.currentGesture.AddPointToCurrentStroke(x, y, pressure, size, eventTime);
							currentPath = new Path();  
							currentPath.moveTo(x, y);  
							currentPath.lineTo(x, y);  
							break;
						} // end of action down
						case MotionEvent.ACTION_MOVE: {
							// pen moving -- continue to add current point and all historical points to current Gesture
							// we should first consume the historical batch, then the current event
							// according to: http://developer.android.com/reference/android/view/MotionEvent.html
							for (int i = 0; i < ev.getHistorySize(); i++) {
								Log.d("note:", GG.currentGesture.name + " historical x = " + ev.getHistoricalX(i) + ", y = " +  ev.getHistoricalY(i) + ", p = " + ev.getHistoricalPressure(i) + ", s = " + ev.getHistoricalSize(i) + ", t = " +  ev.getHistoricalEventTime(i));
								GG.currentGesture.AddPointToCurrentStroke(ev.getHistoricalX(i), ev.getHistoricalY(i), ev.getHistoricalPressure(i), ev.getHistoricalSize(i), ev.getHistoricalEventTime(i));
							}
							GG.currentGesture.AddPointToCurrentStroke(x, y, pressure, size, eventTime);
							currentPath.lineTo(x, y);  
							_graphics.add(currentPath);
							break;
						} // end of action move
						case MotionEvent.ACTION_UP: {
							// pen up -- add last point
							GG.currentGesture.AddPointToCurrentStroke(x, y, pressure, size, eventTime);
							currentPath.lineTo(x, y);  
						    _graphics.add(currentPath);
							break;
						} // end of action up
					} // end of switch

					invalidate();
					return true; // return from onTouch method
				}

			}

		private Runnable r = new Runnable() {
                @Override
                public void run() {
                	starShown = false;
   				 	gestureShown = true;
                    invalidate();
                    delay = 1050;
                }
        };

	}