package com.dreamer.library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.dreamer.library.curve.PathEvaluator;
import com.dreamer.library.curve.PathPoint;
import com.dreamer.library.utils.Utils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

/**
 * 作者： zhangzixu
 * 时间： 2016/1/15
 * 详情：
 */
public class Gift extends ImageView {
    private AnimatorSet set;
    private PathPoint beginPoint;
    private PathPoint pathPoint;
    private AnimatorListener animatorListener;

    public Gift(Context context) {
        this(context, null);
    }

    public Gift(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Gift(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        set = new AnimatorSet();
    }

    private void initAnimator() {
        long duration = (long) (Utils.nextFloat(3, 5) * 1000);
        ValueAnimator objectAnimator = new ValueAnimator();
        objectAnimator.setObjectValues(beginPoint, pathPoint);
        objectAnimator.setEvaluator(new PathEvaluator());
        objectAnimator.setDuration(duration);
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //更新坐标
                PathPoint PathPoint = (PathPoint) animation.getAnimatedValue();
                params.leftMargin = (int) PathPoint.getCurrent().x;
                params.topMargin = (int) PathPoint.getCurrent().y;
                Gift.this.setLayoutParams(params);
            }
        });
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 0, 1, 0);
        alphaAnimator.setDuration(duration);

        ValueAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0, 1);
        scaleXAnimator.setDuration(duration);

        ValueAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0, 1);
        scaleYAnimator.setDuration(duration);
        set.playTogether(objectAnimator, alphaAnimator, scaleXAnimator, scaleYAnimator);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (animatorListener != null) {
                    animatorListener.onAnimationEnd(Gift.this);
                }
            }
        });
        set.setInterpolator(new AccelerateInterpolator());
    }

    public void start() {
        if (set != null) {
            set.start();
        }
    }

    public boolean isRunning(){
        return set.isRunning();
    }

    public void setPathPoint(PathPoint beginPoint, PathPoint pathPoint) {
        this.beginPoint = beginPoint;
        this.pathPoint = pathPoint;
        initAnimator();
    }

    public void addListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public interface AnimatorListener{
        void onAnimationEnd(Gift gift);
    }
}
