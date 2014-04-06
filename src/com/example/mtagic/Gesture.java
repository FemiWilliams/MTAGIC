package com.example.mtagic;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/*
 * Gesture.java
 * Gesture objects contain all of the strokes that are made
 * to be logged by the logger class.
 */

public class Gesture {

	private List<Stroke> strokes = null;
	public String name = null;
	public int sample = -1;
	
	public Gesture(String name, int sample) {
		this.strokes = new ArrayList<Stroke>();
		this.name = name;
		this.sample = sample;
	}
	
	public void NewStroke() {
		this.strokes.add(new Stroke());
	}
	
	public void AddPointToCurrentStroke(float x, float y, float pressure, float size, float time) {
		this.strokes.get(this.strokes.size()-1).AddPoint(new EnhancedPoint(x, y, pressure, size, time));
	}
	
	public List<Stroke> GetStrokes() {
		return this.strokes;
	}
	
	public int GetNumPts() {
		int numpts = 0;
		for (Stroke str : strokes) {
			numpts += str.GetPoints().size();
		}
		return numpts;
	}
	
	protected class Stroke {
		
		private List<EnhancedPoint> points = null;
		
		public Stroke() {
			this.points = new ArrayList<EnhancedPoint>();
		}
		
		public void AddPoint(EnhancedPoint p) {
			this.points.add(p);
		}
		
		public List<EnhancedPoint> GetPoints() {
			return this.points;
		}
		
	}
	
	protected class EnhancedPoint extends Point {
		
		public float pressure;
		public float size;
		public float time;
		
		public EnhancedPoint(float x, float y, float pressure, float size, float time)
		{
			super((int)x, (int)y);
			this.pressure = pressure;
			this.size = size;
			this.time = time;
		}
		
	}

}
