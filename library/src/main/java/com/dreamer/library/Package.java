package com.dreamer.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

import com.dreamer.library.curve.PathPoint;
import com.dreamer.library.utils.Utils;

/**
 * 作者： zhangzixu
 * 时间： 2016/1/15
 * 详情：
 */
public class Package extends FrameLayout {
    protected static final int CENTER = 1;
    protected static final int LEFT = 2;
    protected static final int TOP = 3;
    protected static final int RIGHT = 4;
    protected static final int BOTTOM = 5;

    private int DefaultBeginPointX = 100;
    private int DefaultBeginPointY = 500;
    private int DefaultControlOneX = DefaultBeginPointX + 100;
    private int DefaultControlOneY = DefaultBeginPointY - 300;
    private int DefaultControlTwoX = DefaultBeginPointX + 200;
    private int DefaultControlTwoY = DefaultBeginPointY + 300;
    private int DefaultEndPointX = DefaultBeginPointX + 300;
    private int DefaultEndPointY = DefaultBeginPointY + 300;
    private int DefaultGiftCount = 1;

    private PointF beginPoint = new PointF();    //起始点坐标
    private PointF controlOne = new PointF();    //控制点1的坐标
    private PointF controlTwo = new PointF();    //控制点2的坐标
    private PointF endPoint = new PointF();      //物品最终位置坐标
    private int giftCount;      //物品个数

    private int heightPixels;
    private int widthPixels;
    private PointF centerPoint;

    public Package(Context context) {
        this(context, null);
    }

    public Package(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Package(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        initAttrs(context, attrs);
        initGift();
    }

    private void init(Context context) {
        widthPixels = Utils.getDisplayWidth(context);
        heightPixels = Utils.getDisplayHeight(context);
        centerPoint = new PointF(widthPixels / 2, heightPixels / 2);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PackageGift);
        beginPoint.x = attributes.getInt(R.styleable.PackageGift_controlOneX, DefaultBeginPointX);
        beginPoint.y = attributes.getInt(R.styleable.PackageGift_controlOneY, DefaultBeginPointY);
        controlOne.x = attributes.getInt(R.styleable.PackageGift_controlOneX, DefaultControlOneX);
        controlOne.y = attributes.getInt(R.styleable.PackageGift_controlOneY, DefaultControlOneY);
        controlTwo.x = attributes.getInt(R.styleable.PackageGift_controlTwoX, DefaultControlTwoX);
        controlTwo.y = attributes.getInt(R.styleable.PackageGift_controlTwoY, DefaultControlTwoY);
        endPoint.x = attributes.getInt(R.styleable.PackageGift_endPointX, DefaultEndPointX);
        endPoint.y = attributes.getInt(R.styleable.PackageGift_endPointY, DefaultEndPointY);
        giftCount = attributes.getInt(R.styleable.PackageGift_giftCount, DefaultGiftCount);
        attributes.recycle();
    }

    private void initGift() {
        if (giftCount == 0) {
            return;
        }
        for (int i = 0; i < giftCount; i++) {
            Gift gift = new Gift(getContext());
            FrameLayout.LayoutParams params = new LayoutParams(120, 120);
            gift.setLayoutParams(params);
            gift.setImageResource(R.drawable.ic_launcher);
            gift.setPathPoint(PathPoint.moveTo(beginPoint), PathPoint.curveTo(controlOne, controlTwo, endPoint));
            this.addView(gift);
            gift.start();
        }
    }

    private PathPoint createPath() {
        //一分点

        PathPoint pathPoint = PathPoint.moveTo(new PointF(0, 0));

        return pathPoint;
    }
}
