package com.dreamer.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
    private final int DefaultGiftCount = 1;

    private Queue<Gift> waitQueue = new LinkedBlockingQueue<>();

    private int poolSize = 20;
    private int giftCount;                      //物品个数
    private boolean runAble = false;

    private Paint paint;
    private Thread task;
    private boolean stop = false;

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
        setWillNotDraw(false);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        paint.setStyle(Paint.Style.FILL);
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


    public void start() {
        if (giftCount == 0 || runAble) {
            return;
        }
        task = new Thread(new Runnable() {
            @Override
            public void run() {
                runAble = true;
                while (runAble) {
                    if (stop) {
                        return;
                    }
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
        });
        task.start();
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
                    if (!gift.isRunning()) {
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(Tag,getWidth()+"====----------");
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ImageView imageView = new ImageView(getContext());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        initGiftPool();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stop = true;
    }
}
