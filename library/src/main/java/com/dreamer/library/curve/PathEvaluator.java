package com.dreamer.library.curve;


import android.graphics.PointF;
import android.util.Log;

import com.nineoldandroids.animation.TypeEvaluator;

/**
 * This evaluator interpolates between two PathPoint values given the value t, the
 * proportion traveled between those points. The value of the interpolation depends
 * on the operation specified by the endValue (the operation for the interval between
 * PathPoints is always specified by the end point of that interval).
 */
public class PathEvaluator implements TypeEvaluator<PathPoint> {
    @Override
    public PathPoint evaluate(float t, PathPoint startValue, PathPoint endValue) {
        //Log.e("test","1111");
        PointF pointF = new PointF();
        if (endValue.getOperation() == PathPoint.CURVE) {
            float oneMinusT = 1 - t;
            pointF.x = oneMinusT * oneMinusT * oneMinusT * startValue.getCurrent().x +
                    3 * oneMinusT * oneMinusT * t * endValue.getControlOne().x +
                    3 * oneMinusT * t * t * endValue.getControlOne().x +
                    t * t * t * endValue.getCurrent().x;
            pointF.y = oneMinusT * oneMinusT * oneMinusT * startValue.getCurrent().y +
                    3 * oneMinusT * oneMinusT * t * endValue.getControlOne().y +
                    3 * oneMinusT * t * t * endValue.getControlTwo().y +
                    t * t * t * endValue.getCurrent().y;
        } else if (endValue.getOperation() == PathPoint.LINE) {
            pointF.x = startValue.getCurrent().x + t * (endValue.getCurrent().x - startValue.getCurrent().x);
            pointF.y = startValue.getCurrent().y + t * (endValue.getCurrent().y - startValue.getCurrent().y);
        } else {
            pointF.x = endValue.getCurrent().x;
            pointF.y = endValue.getCurrent().y;
        }
        return PathPoint.moveTo(pointF);
    }
}