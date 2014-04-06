package com.example.mtagic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;

/* 
 * Logger.java
 * Does the logging for BOTH the point activity and the gesture activity.
 */

public class Logger {
	
	/* 
	 * Some of these variables must be declared as static
	 * because Java will have a temper tantrum if you don't.
	 * Just go with it.
	 */
	 
	static BufferedWriter outFile;
	private static Random r = new Random();
	private static String User;
	private static int height;
	private static int width;
	private static int level;
	
	File gpxfile;
	File root = Environment.getExternalStorageDirectory();
	
	public Logger(String u, int w, int h, int l){
		User = u;	
		width = w;
		height = h;
		level = l;
	}
	
	public Logger(String u, Context c, File f){
		User = u;
		gpxfile = f;
		setupPoints(c);
		
	}
	
	
	
	public static void logGesture(Context c, Gesture gesture, File root){
		
		try{
			
			File mainLogDir = new File(root, "MTAGIC Logs");
			mainLogDir.mkdirs(); // won't make new directory unless it doesn't exist
			
			File gestureDir = new File(mainLogDir, "Gesture Logs");
			gestureDir.mkdirs();
			
			File userDir = new File(gestureDir, "User - " + User);
			userDir.mkdirs();
					
			File gesturePath = new File(userDir, "p" + User + "fb_pts-" + level + "-" + gesture.name + ".xml");
			
			Log.d("note:", "Current Gesture Sample: " + gesture.sample);
			Log.d("note:", "Current Gesture Name: " + gesture.name);
		
			FileWriter gpxwriter = new FileWriter(gesturePath, true);
			outFile = new BufferedWriter(gpxwriter);

			//write xml header
			outFile.write("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>");
			outFile.newLine();
		
			//write screen dimensions
			outFile.write("<Gesture Name=\"" + gesture.name + "-" + gesture.sample 
				+ "\" Subject=\"" + User + "fb\" NumPts=\"" + gesture.GetNumPts() 
				+ "\" ScreenWidth=\"" + width + "\" ScreenHeight=\"" + height + "\">");
		
			outFile.newLine();
		
			//log the points of the gesture
			int idx = 1;
			for (Gesture.Stroke str : gesture.GetStrokes()) {
				outFile.write("\t<Stroke index=\"" + idx + "\">");
				outFile.newLine();
				// write ALL the points
				for (Gesture.EnhancedPoint pt : str.GetPoints()) {
					outFile.write("\t\t<Point X=\"" + pt.x + "\" Y=\"" + pt.y + "\" T=\"" + (long)pt.time + "\" P=\"" + pt.pressure + "\" S=\"" + pt.size + "\" />");
					outFile.newLine();
				}
				// close the stroke
				outFile.write("\t</Stroke>");
				outFile.newLine();
				idx++;
			}
			//close out the XML file
			outFile.write("</Gesture>");
			outFile.newLine();
			outFile.close();
	}
	catch (IOException ioe) {

        ioe.printStackTrace();

	}
	}
	
	
	public void setupPoints(Context c){
		// set up output file
		try {
			if (root.canWrite()) {
				
				File mainLogDir = new File(root, "MTAGIC Logs");
				mainLogDir.mkdirs(); // won't make new directory unless it doesn't exist
				
				File touchDir = new File(mainLogDir, "Touch Logs");
				touchDir.mkdirs();
				
				File userDir = new File(touchDir, "User - " + User);
				userDir.mkdirs();
				
				File gpxfile = new File(userDir, "p" + User + "_target_task_pts.csv");
				
				while (gpxfile.exists()) {
					gpxfile = new File(userDir, "p" + User + "_target_task_pts"	+ r.nextInt() + ".csv");
				}
				FileWriter gpxwriter = new FileWriter(gpxfile, true);
				outFile = new BufferedWriter(gpxwriter);
				outFile.write("Timestamp,Username,TargetNumber,AttemptNumber,FirstAttempt?,TimeTargetAppeared,TimeOfEvent,TouchDelay(ms),"
						+ "TouchX,TouchY,TouchPressure,TouchSize,TargetXMin,TargetYMin,TargetSize(pixels),TargetSize(in),"
						+ "UseEdgePadding,ScreenWidth,ScreenHeight,XDPI,Brand,Manufacturer,Model,IsTouchInsideTarget,TargetCenterX,TargetCenterY,DistanceOfTouch(px),DistanceOfTouch(in)");
				outFile.newLine();
			} else {
				Log.e("Error", "WARNING: No permission to write.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logTouch(MotionEvent event, long time, boolean isTouchInside, SimpleDateFormat sdf, int targetNumber, int numAttempts, long currentTargetTime, Rect currentTarget, List<List<String>> allTargets, int screen_width, int screen_height, float xdpi  ){
		
		final float x = event.getX();
		final float y = event.getY();
		final float pressure = event.getPressure();
		final float size = event.getSize();
		
		final float currentTargetCenterX = (currentTarget.left + (currentTarget.width() / 2));
		final float currentTargetCenterY = (currentTarget.top + (currentTarget.width() / 2));
		final double distanceFromTouchToTargetInPixels = Math.sqrt(Math.pow(currentTargetCenterX - x, 2) + Math.pow(currentTargetCenterY - y, 2));
		final double distanceFromTouchToTargetInInches = distanceFromTouchToTargetInPixels / xdpi;
		
		try {
			if (root.canWrite()) {
				
				outFile.write("'" + sdf.format(new Date(System.currentTimeMillis()))
						+ ","
						+ User
						+ ","
						+ targetNumber
						+ ","
						+ numAttempts
						+ ","
						+ (numAttempts == 1 ? '1' : '0')
						+ ","
						+ currentTargetTime
						+ ","
						+ time
						+ ","
						+ (time - currentTargetTime)
						+ ","
						+ x
						+ ","
						+ y
						+ ","
						+ pressure
						+ ","
						+ size
						+ ","
						+ currentTarget.left
						+ ","
						+ currentTarget.top
						+ ","
						+ currentTarget.width()
						+ ","
						+ allTargets.get(targetNumber).get(3) + ","
						+ Boolean.parseBoolean(allTargets.get(targetNumber).get(4)) + "," 
						+ screen_width + ","
						+ screen_height + "," 
						+ xdpi + ","
						+ Build.BRAND + ","
						+ Build.MANUFACTURER + ","
						+ Build.MODEL + ","
						+ isTouchInside
						+ ","
						+ currentTargetCenterX
						+ ","
						+ currentTargetCenterY
						+ ","
						+ distanceFromTouchToTargetInPixels + ","
						+ distanceFromTouchToTargetInInches);
				outFile.newLine();
				// outFile.close();
			} else {
				Log.e("Error", "WARNING: No permission to write.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// Log.e(TAG, "Could not write file " + e.getMessage());
		}
		
	}
	
	public void close(){
		try {
			if (outFile != null)
				outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void logBack(String sdf){
		try {
			outFile.write("'" + sdf
					+ ","
					+ User
					+ ","
					+ "-1"
					+ ","
					+ "Phone Back Button Pressed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
