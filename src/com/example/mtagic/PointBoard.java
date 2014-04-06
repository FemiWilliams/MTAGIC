package com.example.mtagic;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/*
 * PointBoard.java
 * The canvas board of the point game, where the penguin
 * background and fish are drawn. Handles the actual game 
 * logic.
 */

public class PointBoard extends View {
	private Context mContext;
	PointGame parent;	
	
	private Bitmap mBitmapPenguin = null;

	// the target to draw
	private Bitmap mBitmapFish = null;
	private Bitmap[] fishArray = new Bitmap[5];
	private BitmapDrawable myFish;
	
	private Bitmap starburst;
	private boolean shown = false;
	private Rect starRect;
	
	private Handler h;
	private int delay = 1050;
	
	public PointBoard(final PointGame parent) { //Context context) {
		super(parent); //context;
		
		loadFish();
		mBitmapPenguin = BitmapFactory.decodeResource(getResources(), R.drawable.penguin);
		starburst = BitmapFactory.decodeResource(getResources(), R.drawable.starburst);

		this.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				// Log.d("note:", "in on touch listener");

				parent.processTouch(event);
				return false;
			}
		});
		
		h = new Handler();
		
	}

	protected void moveFish(Rect target) {
		
		mBitmapFish = fishArray[(int) Math.ceil(Math.random() * fishArray.length - 1)];
		myFish = new BitmapDrawable(mBitmapFish);
		myFish.setBounds(target);
		
		// force a repaint
		this.invalidate();
	}

	private void initialize()
	{
	    mContext = getContext();
	}

	protected void onDraw(Canvas canvas) {
		
		mBitmapPenguin = Bitmap.createScaledBitmap(mBitmapPenguin, getWidth(), getHeight(), false);
		canvas.drawBitmap(mBitmapPenguin, 0, 0, null);
		
		myFish.draw(canvas);
	    
		if(shown == true){
			canvas.drawBitmap(starburst, starRect.left, starRect.top, null);
		}
		
		h.postDelayed(r, delay);
	     
	}
		
	public void loadFish(){
		fishArray[0] = BitmapFactory.decodeResource(getResources(), R.drawable.bluestripefish);
		fishArray[1] = BitmapFactory.decodeResource(getResources(), R.drawable.pinkfish);
		fishArray[2] = BitmapFactory.decodeResource(getResources(), R.drawable.purplefish);
		fishArray[3] = BitmapFactory.decodeResource(getResources(), R.drawable.redfish);
		fishArray[4] = BitmapFactory.decodeResource(getResources(), R.drawable.yellowfish);
	}
		
	private Runnable r = new Runnable() {
        @Override
        public void run() {
        	shown = false;
            invalidate();
            delay = 1050;
        }
	};
	
	public void starburst(Rect target){
		
		starRect = new Rect(target);
		shown = true;
		delay = 2050;
	}
}