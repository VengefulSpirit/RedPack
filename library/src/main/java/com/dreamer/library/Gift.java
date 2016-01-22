package com.dreamer.library;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
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
    private final int controlGap = 50;
    private final int controlTopMin = 200;
    private final int controlTop = 300;
    private final int controlBottomMin = 200;
    private final int controlBottom = 300;
    private final int controlLeft = 200;        //控制点左区间
    private final int controlRight = 200;       //控制点右区间

    private AnimatorSet set = new AnimatorSet();
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
    }

    private void resetAnimator() {
        set = new AnimatorSet();
        long duration = (long) (Utils.nextFloat(3, 5) * 1000);
        ValueAnimator objectAnimator = new ValueAnimator();
        objectAnimator.setObjectValues(beginPoint, pathPoint);
        objectAnimator.setEvaluator(new PathEvaluator());
        objectAnimator.setDuration(duration);
        final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PathPoint PathPoint = (PathPoint) animation.getAnimatedValue();
                params.leftMargin = (int) PathPoint.getCurrent().x;
                params.topMargin = (int) PathPoint.getCurrent().y;
                Gift.this.setLayoutParams(params);
            }
        });
        ValueAnimator alphaAnimator = ObjectAnimator.ofFloat(this, "alpha", 0, 1, 0);
        alphaAnimator.setDuration(duration);

        ValueAnimator scaleXAnimator = ObjectAnimator.ofFloat(this, "scaleX", 0.2f, 1);
        scaleXAnimator.setDuration(duration);

        ValueAnimator scaleYAnimator = ObjectAnimator.ofFloat(this, "scaleY", 0.2f, 1);
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
            pathPoint = createPath();
            resetAnimator();
            set.start();
        }
    }

    public boolean isRunning() {
        if (set == null)
            return false;
        return set.isRunning();
    }

    public void addListener(AnimatorListener animatorListener) {
        this.animatorListener = animatorListener;
    }

    public interface AnimatorListener {
        void onAnimationEnd(Gift gift);
    }

    private PathPoint createPath() {
        int parentW = ((View) getParent()).getWidth();
        int parentH = ((View) getParent()).getHeight();
        beginPoint = PathPoint.moveTo(new PointF((parentW - getLayoutParams().width) / 2, (parentH - getLayoutParams().height) / 2));
        PointF controlOne = new PointF();   //控制点1的坐标
        PointF controlTwo = new PointF();   //控制点2的坐标
        PointF endPoint = new PointF();     //物品最终位置坐标
        if (Utils.nextInt(-2, 2) > 0) {//左侧
            controlOne.x = Utils.nextFloat(beginPoint.getCurrent().x - controlLeft, beginPoint.getCurrent().x);
            controlOne.y = Utils.nextFloat(beginPoint.getCurrent().y - controlTop, beginPoint.getCurrent().y - controlTopMin);
            controlTwo.x = controlOne.x - controlGap;
            controlTwo.y = beginPoint.getCurrent().y;
            endPoint.x = controlTwo.x - controlGap;
            endPoint.y = Utils.nextFloat(beginPoint.getCurrent().y + controlBottomMin, beginPoint.getCurrent().y + controlBottom);
        } else {//右侧
            controlOne.x = Utils.nextFloat(beginPoint.getCurrent().x, beginPoint.getCurrent().x + controlRight);
            controlOne.y = Utils.nextFloat(beginPoint.getCurrent().y - controlTop, beginPoint.getCurrent().y - controlTopMin);
            controlTwo.x = controlOne.x + controlGap;
            controlTwo.y = beginPoint.getCurrent().y;
            endPoint.x = controlTwo.x + controlGap;
            endPoint.y = Utils.nextFloat(beginPoint.getCurrent().y + controlBottomMin, beginPoint.getCurrent().y + controlBottom);
        }
        //Log.d(Tag, controlOne.x + "====" + controlOne.y + "====" + controlTwo.x + "====" + controlTwo.y + "====" + endPoint.x + "====" + endPoint.y);
        return PathPoint.curveTo(controlOne, controlTwo, endPoint);
    }
}
