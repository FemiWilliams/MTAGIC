package com.example.mtagic;

import android.graphics.Rect;
import android.util.Log;

// this class handles drawing the targets in their proper positions
// based on knowing the screen resolution (width x height),
// the number of columns and rows to break the screen into,
// and what the padding is (when drawing near-edge)
public class GridSquares {

	private final int width;
	private final int height;
	private final int columns;
	private final int rows;
	private final int padding;
	private final float gridWidth;
	private final float gridHeight;

	// set up the grid
	public GridSquares(int width, int height, int columns, int rows, int padding) {
		if (columns < 2)
			throw new IllegalArgumentException();
		if (rows < 2)
			throw new IllegalArgumentException();
		if (width <= 2 * padding)
			throw new IllegalArgumentException();
		if (height <= 2 * padding)
			throw new IllegalArgumentException();

		this.width = width;
		this.height = height;
		this.columns = columns;
		this.rows = rows;
		this.padding = padding;

		this.gridWidth = (float) width / (float) columns;
		this.gridHeight = (float) height / (float) rows;
	}

	// this method returns the Rectangle to draw based on the desired row, column, 
	// size and edge padding for the target onscreen
	public Rect getLocation(int row, int column, int size, boolean padEdges) {
		//Log.d("note:", "row = " + row + ", col = " + column + ", size = " + size);

		if (row < 0 || row >= rows || column < 0 || column >= columns)
			throw new IllegalArgumentException();

		final int padding = padEdges ? this.padding : 0;
		final int halfSize = size / 2;

		final int x;
		final int y;

		if (row == 0) {
			y = padding;
		} else if (row == rows - 1) {
			y = height - padding - size;
		} else {
			y = Math.round(((float) row + 0.5f) * gridHeight) - halfSize;
		}

		if (column == 0) {
			x = padding;
		} else if (column == columns - 1) {
			x = width - padding - size;
		} else {
			x = Math.round(((float) column + 0.5f) * gridWidth) - halfSize;
		}
		
		//Log.d("note:", "(" + x + "," + y + ") -> (" + (x+size) + "," + (y+size) + ")");
		return new Rect(x, y, x + size, y + size);
	}
	
}
