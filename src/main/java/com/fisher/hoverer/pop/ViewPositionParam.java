package com.fisher.hoverer.pop;

/**
 * Created by fisher at 12:16 AM on 12/6/16.
 * <p>
 * Entity to store info about the left top corner position and right bottom corner position.
 */

public class ViewPositionParam {

	private static final float MIN_WIDTH = 1;
	private static final float MIN_HEIGHT = 1;

	private float mMinWidth = MIN_WIDTH;
	private float mMinHeight = MIN_HEIGHT;

	private float x1;
	private float y1;
	private float x2;
	private float y2;

	public ViewPositionParam(float x1, float y1, float x2, float y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	public float getX1() {
		return x1;
	}

	public void setX1(float x1) {
		if (0 > x1) {x1 = 0;}
		this.x1 = x1;
	}

	public float getY1() {
		return y1;
	}

	public void setY1(float y1) {
		if (0 > y1) {y1 = 0;}
		this.y1 = y1;
	}

	public float getX2() {
		return x2;
	}

	public void setX2(float x2) {
		this.x2 = x2;
	}

	public float getY2() {
		return y2;
	}

	public void setY2(float y2) {
		this.y2 = y2;
	}

	/**
	 * Set right bottom point of view.
	 *
	 * @param x X value of the coordination.
	 * @param y Y value of the coordination.
	 */
	public void setC1(float x, float y) {
		setX1(x);
		setY1(y);
	}

	/**
	 * Set left top point of view.
	 *
	 * @param x X value of the coordination.
	 * @param y Y value of the coordination.
	 */
	public void setC2(float x, float y) {
		setX2(x);
		setY2(y);
	}

	/**
	 * Set center point of view.
	 *
	 * @param x X value of the coordination.
	 * @param y Y value of the coordination.
	 */
	public void setCenter(float x, float y) {
		float width = (x2 - x1) / 2;
		float height = (y2 - y1) / 2;
		this.x1 = x - width;
		this.y1 = y - height;
		this.x2 = x + width;
		this.y2 = y + height;
	}

	public void setMin(float x, float y) {
		this.mMinWidth = x;
		this.mMinHeight = y;
	}

	public float getWidth() {
		float width = x2 - x1;
		if (mMinWidth >= width) {return mMinWidth;}
		return width;
	}

	public float getHeight() {
		float height = y2 - y1;
		if (mMinHeight >= height) {return mMinHeight;}
		return height;
	}
}
