// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the Apache License, Version 2.0
// http://www.apache.org/licenses/LICENSE-2.0

//package com.google.appinventor.components.runtime;
package com.google.appinventor.components.runtime.util;

//import com.google.appinventor.components.runtime.util.BoundingBox;
import android.util.Log;

/**
 * Bounding box abstraction, similar to {@link android.graphics.Rect}, but
 * compatible with rotation.
 *
 */
public class OrientedBoundingBox extends BoundingBox{
	private boolean rotates;
	
	private double left;
	private double top;
	private double right;
	private double bottom;
	
	private double centerX;
	private double centerY;
	private double extentLength;
	private double extentHeight;
	// The following axes are meant to be components of unit vectors.
	private double axisLengthX;
	private double axisLengthY;
	private double axisHeightX;
	private double axisHeightY;

	// Vertices
	private double vertTopRightX;
	private double vertTopRightY;
	private double vertTopLeftX;
	private double vertTopLeftY;
	private double vertBottomRightX;
	private double vertBottomRightY;
	private double vertBottomLeftX;
	private double vertBottomLeftY;

	/**
	 * Constructor for a bounding box. All coordinates are inclusive.
	 *
	 * @param l
	 *            leftmost x-coordinate
	 * @param t
	 *            topmost y-coordinate
	 * @param r
	 *            rightmost x-coordinate
	 * @param b
	 *            bottommost y-coordinate
	 */
	public OrientedBoundingBox(double l, double t, double r, double b) {
		super(l, t, r, b);
		rotates = false;
	}
	
	/**
	 * Constructor for an oriented bounding box. TODO: Confident I only need 3
	 * of the vertices. Revise and refactor.
	 *
	 * @param vertTopRightX
	 *            x-coordinate for Top Right vertex
	 * @param vertTopRightY
	 *            y-coordinate for Top Right vertex
	 * @param vertTopLeftX
	 *            x-coordinate for Top Left vertex
	 * @param vertTopLeftY
	 *            y-coordinate for Top Left vertex
	 * @param vertBottomRightX
	 *            x-coordinate for Bottom Right vertex
	 * @param vertBottomRightY
	 *            y-coordinate for Bottom Right vertex
	 * @param vertBottomLeftX
	 *            x-coordinate for Bottom Left vertex
	 * @param vertBottomLeftY
	 *            y-coordinate for Bottom Left vertex
	 */	
	public OrientedBoundingBox(double centerX, double centerY, double vertTopRightX, double vertTopRightY,
			double vertTopLeftX, double vertTopLeftY, double vertBottomRightX, double vertBottomRightY,
			double vertBottomLeftX, double vertBottomLeftY) {
		// Gabriel Assumption: Y axis values increase downward, and X axis
		// values increase to the right.
		// TODO: Confident that from one axis, the other can be derived. So
		// perhaps just need center and
		// two vertices.
		super(Math.min(vertTopLeftX, vertBottomLeftX), Math.min(vertTopLeftY, vertTopRightY),
				Math.max(vertTopRightX, vertBottomRightX), Math.max(vertBottomLeftY, vertBottomRightY));
		rotates = true;
		axisLengthX = vertBottomRightX - vertBottomLeftX;
		axisLengthY = vertBottomRightY - vertBottomLeftY;
		axisHeightX = vertBottomLeftX - vertTopLeftX;
		axisHeightY = vertBottomLeftY - vertTopLeftY;

		double axisLengthMagnitude = Math.sqrt(axisLengthX * axisLengthX + axisLengthY * axisLengthY);
		axisLengthX = axisLengthX / axisLengthMagnitude;
		axisLengthY = axisLengthY / axisLengthMagnitude;
		extentLength = axisLengthMagnitude / 2;
		
		double axisHeightMagnitude = Math.sqrt(axisHeightX * axisHeightX + axisHeightY * axisHeightY);
		axisHeightX = axisHeightX / axisHeightMagnitude;
		axisHeightY = axisHeightY / axisHeightMagnitude;
		extentHeight = axisHeightMagnitude / 2;
		
		this.centerX = centerX;
		this.centerY = centerY;
		this.vertTopRightX = vertTopRightX;
		this.vertTopRightY = vertTopRightY;
		this.vertTopLeftX = vertTopLeftX;
		this.vertTopLeftY = vertTopLeftY;
		this.vertBottomRightX = vertBottomRightX;
		this.vertBottomRightY = vertBottomRightY;
		this.vertBottomLeftX = vertBottomLeftX;
		this.vertBottomLeftY = vertBottomLeftY;
		
		this.left = Math.min(this.vertBottomLeftX, this.vertTopLeftX);
		this.top = Math.min(this.vertTopLeftY, this.vertTopRightY);
		this.right = Math.max(this.vertBottomRightX, this.vertTopRightX);
		this.bottom = Math.max(this.vertBottomLeftY, this.vertBottomRightY);
	}
	/**
	 * Gets the x-coordinate of the top left vertex.
	 *
	 * @return the x-coordinate for the top left vertex
	 */
	public double getTopLeftX() {

		return vertTopLeftX;
	}

	/**
	 * Gets the y-coordinate of the top left vertex.
	 *
	 * @return the y-coordinate for the top left vertex
	 */
	public double getTopLeftY() {

		return vertTopLeftY;
	}

	/**
	 * Gets the x-coordinate of the top right vertex.
	 *
	 * @return the x-coordinate for the top right vertex
	 */
	public double getTopRightX() {

		return vertTopRightX;
	}

	/**
	 * Gets the y-coordinate of the top right vertex.
	 *
	 * @return the y-coordinate for the top right vertex
	 */
	public double getTopRightY() {

		return vertTopRightY;
	}

	/**
	 * Gets the x-coordinate of the bottom left vertex.
	 *
	 * @return the x-coordinate for the bottom left vertex
	 */
	public double getBottomLeftX() {

		return vertBottomLeftX;
	}

	/**
	 * Gets the y-coordinate of the bottom left vertex.
	 *
	 * @return the y-coordinate for the bottom left vertex
	 */
	public double getBottomLeftY() {

		return vertBottomLeftY;
	}

	/**
	 * Gets the x-coordinate of the bottom right vertex.
	 *
	 * @return the x-coordinate for the bottom right vertex
	 */
	public double getBottomRightX() {

		return vertBottomRightX;
	}

	/**
	 * Gets the y-coordinate of the bottom right vertex.
	 *
	 * @return the y-coordinate for the bottom right vertex
	 */
	public double getBottomRightY() {

		return vertBottomRightY;
	}

	/**
	 * Gets the x-coordinate of the center's ordered pair.
	 *
	 * @return the x-coordinate of the center's ordered pair
	 */
	public double getCenterX() {

		return centerX;
	}
	
	/**
	 * Gets the y-coordinate of the center's ordered pair.
	 *
	 * @return the y-coordinate of the center's ordered pair
	 */
	public double getCenterY() {

		return centerY;
	}
	
	/**
	 * Gets half the length of the bounding box (transformed x-axis).
	 *
	 * @return half the length of the bounding box (transformed x-axis)
	 */
	public double getExtentLength() {

		return extentLength;
	}
	
	/**
	 * Gets half the height of the bounding box (transformed y-axis).
	 *
	 * @return half the height of the bounding box (transformed y-axis)
	 */
	public double getExtentHeight() {

		return extentHeight;
	}
	
	/**
	 * Gets the value for the x-axis component of the bounding box's lengthwise unit vector.
	 *
	 * @return x-axis component of the bounding box's lengthwise unit vector.
	 */
	public double getAxisLengthX() {

		return axisLengthX;
	}
	
	/**
	 * Gets the value for the y-axis component of the bounding box's lengthwise unit vector.
	 *
	 * @return y-axis component of the bounding box's lengthwise unit vector.
	 */
	public double getAxisLengthY() {

		return axisLengthY;
	}
	
	/**
	 * Gets the value for the x-axis component of the bounding box's heightwise unit vector.
	 *
	 * @return x-axis component of the bounding box's heightwise unit vector.
	 */
	public double getAxisHeightX() {

		return axisHeightX;
	}
	
	/**
	 * Gets the value for the y-axis component of the bounding box's heightwise unit vector.
	 *
	 * @return y-axis component of the bounding box's heightwise unit vector.
	 */
	public double getAxisHeightY() {

		return axisHeightY;
	}
	
	/**
	 * Determines whether this bounding box intersects with the passed
	 * bounding box.
	 * 
	 * This is done by checking the other bounding box's vertices to see if they fall
	 * within this bounding box. If they do, they intersect.
	 * 
	 * @param bb
	 *            oriented bounding box to intersect with this oriented bounding
	 *            box
	 * @return {@code true} if they intersect, {@code false} otherwise
	 */
	@Override
	public boolean intersectDestructively(BoundingBox bb) {
		double currentX;
		double currentY;
		double currentLength;
		double currentHeight;
		if(bb instanceof OrientedBoundingBox) {
			//START: Check if bb's vertices lie within bounding box's boundaries			
			currentX = ((OrientedBoundingBox)bb).getBottomLeftX();
			currentY = ((OrientedBoundingBox)bb).getBottomLeftY();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				return true;
			}

			currentX = ((OrientedBoundingBox)bb).getBottomRightX();
			currentY = ((OrientedBoundingBox)bb).getBottomRightY();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				return true;
			}

			currentX = ((OrientedBoundingBox)bb).getTopLeftX();
			currentY = ((OrientedBoundingBox)bb).getTopLeftY();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				return true;
			}

			currentX = ((OrientedBoundingBox)bb).getTopRightX();
			currentY = ((OrientedBoundingBox)bb).getTopRightY();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				return true;
			}
			//END: Check if bb's vertices lie within bounding box's boundaries
			
			//START: Check if this instance's vertices lie within bb's boundaries			
			currentX = this.getBottomLeftX();
			currentY = this.getBottomLeftY();
			// Shift the coordinates to bb's reference frame.
			currentX -= ((OrientedBoundingBox)bb).getCenterX();
			currentY -= ((OrientedBoundingBox)bb).getCenterY();
			currentLength = currentX * ((OrientedBoundingBox)bb).getAxisLengthX() + currentY * ((OrientedBoundingBox)bb).getAxisLengthY();
			currentHeight = currentX * ((OrientedBoundingBox)bb).getAxisHeightX() + currentY * ((OrientedBoundingBox)bb).getAxisHeightY();

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= ((OrientedBoundingBox)bb).getExtentLength() 
					&& Math.abs(currentHeight) <= ((OrientedBoundingBox)bb).getExtentHeight()) {
				return true;
			}

			currentX = this.getBottomRightX();
			currentY = this.getBottomRightY();
			// Shift the coordinates to bb's reference frame.
			currentX -= ((OrientedBoundingBox)bb).getCenterX();
			currentY -= ((OrientedBoundingBox)bb).getCenterY();
			currentLength = currentX * ((OrientedBoundingBox)bb).getAxisLengthX() + currentY * ((OrientedBoundingBox)bb).getAxisLengthY();
			currentHeight = currentX * ((OrientedBoundingBox)bb).getAxisHeightX() + currentY * ((OrientedBoundingBox)bb).getAxisHeightY();

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= ((OrientedBoundingBox)bb).getExtentLength()
					&& Math.abs(currentHeight) <= ((OrientedBoundingBox)bb).getExtentHeight()) {
				return true;
			}

			currentX = this.getTopLeftX();
			currentY = this.getTopLeftY();
			// Shift the coordinates to bb's reference frame.
			currentX -= ((OrientedBoundingBox)bb).getCenterX();
			currentY -= ((OrientedBoundingBox)bb).getCenterY();
			currentLength = currentX * ((OrientedBoundingBox)bb).getAxisLengthX() + currentY * ((OrientedBoundingBox)bb).getAxisLengthY();
			currentHeight = currentX * ((OrientedBoundingBox)bb).getAxisHeightX() + currentY * ((OrientedBoundingBox)bb).getAxisHeightY();

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= ((OrientedBoundingBox)bb).getExtentLength()
					&& Math.abs(currentHeight) <= ((OrientedBoundingBox)bb).getExtentHeight()) {
				return true;
			}

			currentX = this.getTopRightX();
			currentY = this.getTopRightY();
			// Shift the coordinates to bb's reference frame.
			currentX -= ((OrientedBoundingBox)bb).getCenterX();
			currentY -= ((OrientedBoundingBox)bb).getCenterY();
			currentLength = currentX * ((OrientedBoundingBox)bb).getAxisLengthX() + currentY * ((OrientedBoundingBox)bb).getAxisLengthY();
			currentHeight = currentX * ((OrientedBoundingBox)bb).getAxisHeightX() + currentY * ((OrientedBoundingBox)bb).getAxisHeightY();

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= ((OrientedBoundingBox)bb).getExtentLength()
					&& Math.abs(currentHeight) <= ((OrientedBoundingBox)bb).getExtentHeight()) {
				return true;
			}
			////END: Check if bb's vertices lie within bounding box's boundaries

			return false;
		} else {
			//START: Check if bb's vertices lie within bounding box's boundaries
			currentX = bb.getLeft();
			currentY = bb.getBottom();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				return true;
			}

			currentX = bb.getRight();
			currentY = bb.getBottom();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				Log.e("AppInventor", "Checked using prior implementation");
				return true;
			}

			currentX = bb.getLeft();
			currentY = bb.getTop();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				Log.e("AppInventor", "Checked using prior implementation");
				return true;
			}

			currentX = bb.getRight();
			currentY = bb.getTop();
			// Shift the coordinates to this bounding box's reference frame.
			currentX -= centerX;
			currentY -= centerY;
			currentLength = currentX * axisLengthX + currentY * axisLengthY;
			currentHeight = currentX * axisHeightX + currentY * axisHeightY;

			// Check if shifted coordinates fall inside the bounding box
			if (Math.abs(currentLength) <= extentLength && Math.abs(currentHeight) <= extentHeight) {
				Log.e("AppInventor", "Checked using prior implementation");
				return true;
			}
			//END: Check if bb's vertices lie within bounding box's boundaries
			
			//START: Check if this instance's vertices lie within bb's boundaries
			currentX = this.getBottomLeftX();
			currentY = this.getBottomLeftY();
			if(currentX >= bb.getLeft() && currentX <= bb.getRight() && currentY >= bb.getTop() && currentY <= bb.getBottom()){
				return true;
			}
			
			currentX = this.getBottomRightX();
			currentY = this.getBottomRightY();
			if(currentX >= bb.getLeft() && currentX <= bb.getRight() && currentY >= bb.getTop() && currentY <= bb.getBottom()){
				return true;
			}
			
			currentX = this.getTopLeftX();
			currentY = this.getTopLeftY();
			if(currentX >= bb.getLeft() && currentX <= bb.getRight() && currentY >= bb.getTop() && currentY <= bb.getBottom()){
				return true;
			}
			
			currentX = this.getTopRightX();
			currentY = this.getTopRightY();
			if(currentX >= bb.getLeft() && currentX <= bb.getRight() && currentY >= bb.getTop() && currentY <= bb.getBottom()){
				return true;
			}
			//END: Check if this instance's vertices lie within bb's boundaries

			return false;
		}
	}
	
	/**
	 * Gets the leftmost x-coordinate
	 *
	 * @return the leftmost x-coordinate
	 */
	public double getLeft() {

		return left;
	}

	/**
	 * Gets the uppermost y-coordinate
	 *
	 * @return the uppermost y-coordinate
	 */
	public double getTop() {
		return top;
	}

	/**
	 * Gets the rightmost x-coordinate
	 *
	 * @return the rightmost x-coordinate
	 */
	public double getRight() {
		return right;
	}

	/**
	 * Gets the bottommost y-coordinate
	 *
	 * @return the bottommost y-coordinate
	 */
	public double getBottom() {
		return bottom;
	}

	//TODO: Update this
	public String toString() {
		return "<OrientedBoundingBox (left = " + left + ", top = " + top + ", right = " + right + ", bottom = " + bottom
				+ ">";
	}
}
