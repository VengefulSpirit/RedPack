package com.dreamer.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import com.dreamer.library.curve.PathPoint;
import com.dreamer.library.utils.Utils;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * 作者： zhangzixu
 * 时间： 2016/1/15
 * 详情：
 */
public class Package extends FrameLayout {
    private final String Tag = "Package";
    private final int start = 1;
    private final int controlTopMin = 200;
    private final int controlTop = 300;
    private final int controlBottomMin = 200;
    private final int controlBottom = 300;
    private final int controlLeft = 200;        //控制点左区间
    private final int controlRight = 200;       //控制点右区间
    private final int DefaultGiftCount = 1;

    private Queue<Gift> waitQueue = new LinkedBlockingQueue<>();

    private PointF beginPoint = new PointF();   //起始点坐标
    private int poolSize = 20;
    private int giftCount;                      //物品个数

    private PointF center;                      //二分之一点
    private boolean runAble = false;

    public Package(Context context) {
        this(context, null);
    }

    public Package(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Package(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init(context);
    }

    private void init(Context context) {
        int w = Utils.getDisplayWidth(context);
        int h = Utils.getDisplayHeight(context);
        center = new PointF(w / 2, h / 2);
        beginPoint = center;
        initGiftPool();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PackageGift);
        giftCount = attributes.getInt(R.styleable.PackageGift_giftCount, DefaultGiftCount);
        attributes.recycle();
    }

    private void initGiftPool() {
        if (giftCount == 0) {
            return;
        }
        for (int i = 0; i < Math.min(poolSize, giftCount); i++) {
            Gift gift = new Gift(getContext());
            FrameLayout.LayoutParams giftParams = new LayoutParams(120, 120);
            gift.setLayoutParams(giftParams);
            gift.setImageResource(R.drawable.red_package);
            gift.setPathPoint(PathPoint.moveTo(beginPoint), createPath());
            gift.addListener(new Gift.AnimatorListener() {
                @Override
                public void onAnimationEnd(Gift gift) {
                    if (!waitQueue.contains(gift)) {
                        waitQueue.add(gift);
                        if (gift.getParent() != null) {
                            removeView(gift);
                        }
                        Log.e(Tag, "animation---" + gift.toString());
                    }
                }
            });
            waitQueue.add(gift);
        }
    }

    private PathPoint createPath() {
        PointF controlOne = new PointF();   //控制点1的坐标
        PointF controlTwo = new PointF();   //控制点2的坐标
        PointF endPoint = new PointF();     //物品最终位置坐标
        if (Utils.nextInt(-2, 2) > 0) {//左侧
            controlOne.x = Utils.nextFloat(beginPoint.x - controlLeft, beginPoint.x);
            controlOne.y = Utils.nextFloat(beginPoint.y - controlTop, beginPoint.y - controlTopMin);
            controlTwo.x = controlOne.x;
            controlTwo.y = beginPoint.y;
        } else {//右侧
            controlOne.x = Utils.nextFloat(beginPoint.x, beginPoint.x + controlRight);
            controlOne.y = Utils.nextFloat(beginPoint.y - controlTop, beginPoint.y - controlTopMin);
            controlTwo.x = controlOne.x;
            controlTwo.y = beginPoint.y;
        }
        endPoint.x = controlOne.x;
        endPoint.y = Utils.nextFloat(beginPoint.y + controlBottomMin, beginPoint.y + controlBottom);
        //Log.d(Tag, controlOne.x + "====" + controlOne.y + "====" + controlTwo.x + "====" + controlTwo.y + "====" + endPoint.x + "====" + endPoint.y);
        return PathPoint.curveTo(controlOne, controlTwo, endPoint);
    }

    public void start() {
        if (giftCount == 0) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                runAble = true;
                while (runAble) {
                    if (waitQueue == null || waitQueue.size() == 0) {
                        continue;
                    }
                    Gift gift = waitQueue.remove();
                    Log.e(Tag, "get---" + gift.toString());
                    if (gift.isRunning()) {
                        Log.e(Tag, "running---" + gift.toString());
                        waitQueue.add(gift);
                        continue;
                    }
                    Message message = new Message();
                    message.what = start;
                    message.obj = gift;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep((long) (Utils.nextFloat(0, 1) * 1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void stop() {
        runAble = false;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case start:
                    Gift gift = (Gift) msg.obj;
                    Log.e(Tag, "handle---" + gift.toString());
                    if (gift != null && !gift.isRunning()) {
                        gift.setPathPoint(PathPoint.moveTo(beginPoint), createPath());
                        if (gift.getParent() == null) {
                            addView(gift);
                        }
                        gift.start();
                    }
                    break;
                default:
                    break;
            }
        }
    };
}
