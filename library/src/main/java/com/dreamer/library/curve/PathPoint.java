package com.dreamer.library.curve;

import android.graphics.PointF;

/**
 * A class that holds information about a location and how the path should get to that
 * location from the previous path location (if any). Any PathPoint holds the information for
 * its location as well as the instructions on how to traverse the preceding interval from the
 * previous location.
 */
public class PathPoint {

    /**
     * The possible path operations that describe how to move from a preceding PathPoint to the
     * location described by this PathPoint.
     */
    public static final int MOVE = 0;
    public static final int LINE = 1;
    public static final int CURVE = 2;

    /**
     * The location of this PathPoint
     */
    private PointF current;

    /**
     * The first control point, if any, for a PathPoint of type CURVE
     */
    private PointF controlOne;

    /**
     * The second control point, if any, for a PathPoint of type CURVE
     */
    private PointF controlTwo;

    /**
     * The motion described by the path to get from the previous PathPoint in an AnimatorPath
     * to the location of this PathPoint. This can be one of MOVE, LINE, or CURVE.
     */
    private int mOperation;


    /**
     * Line/Move constructor
     */
    private PathPoint(int operation, PointF pointF) {
        mOperation = operation;
        current = pointF;
    }

    /**
     * Curve constructor
     */
    private PathPoint(PointF controlOne, PointF controlTwo, PointF current) {
        this.controlOne = controlOne;
        this.controlTwo = controlTwo;
        this.current = current;
        mOperation = CURVE;
    }

    /**
     * Constructs and returns a PathPoint object that describes a line to the given xy location.
     */
    public static PathPoint lineTo(PointF pointF) {
        return new PathPoint(LINE, pointF);
    }

    /**
     * Constructs and returns a PathPoint object that describes a cubic BŽzier curve to the
     * given xy location with the control points at c0 and c1.
     */
    public static PathPoint curveTo(PointF controlOne, PointF controlTwo, PointF current) {
        return new PathPoint(controlOne, controlTwo, current);
    }

    /**
     * Constructs and returns a PathPoint object that describes a discontinuous move to the given
     * xy location.
     */
    public static PathPoint moveTo(PointF pointF) {
        return new PathPoint(MOVE, pointF);
    }

    public PointF getCurrent() {
        return current;
    }

    public void setCurrent(PointF current) {
        this.current = current;
    }

    public PointF getControlOne() {
        return controlOne;
    }

    public void setControlOne(PointF controlOne) {
        this.controlOne = controlOne;
    }

    public PointF getControlTwo() {
        return controlTwo;
    }

    public void setControlTwo(PointF controlTwo) {
        this.controlTwo = controlTwo;
    }

    public int getOperation() {
        return mOperation;
    }

    public void setOperation(int mOperation) {
        this.mOperation = mOperation;
    }
}