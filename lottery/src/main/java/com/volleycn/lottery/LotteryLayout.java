package com.volleycn.lottery;

import android.content.Context;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.blankj.utilcode.util.SizeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LotteryLayout extends FrameLayout {
    public static final int START_MARQUEE = 1001;
    public static final int START_GAME = 1002;
    public static final int RECOVER_IMG = 1003;
    private boolean marqueeRunning = false;
    private boolean running = false;
    private boolean tryToStop = false;
    private int currentIndex = 0;
    private int currentTotal = 0;
    private int stayIndex = 0;
    private static final int DEFAULT_SPEED = 250;
    private static final int MIN_SPEED = 50;
    private int currentSpeed = DEFAULT_SPEED;
    public final static int Lottery_ITEM_TYPE_START = 1001;
    public final static int Lottery_ITEM_TYPE_NORMAL = 1002;
    //顺时针
    public final static int MARQUEE_MODE_CW = 1001;
    //逆时针
    public final static int MARQUEE_MODE_CCW = 1002;
    //随机
    public final static int MARQUEE_MODE_RD = 1003;

    private int MARQUEE_MODE = MARQUEE_MODE_CW;

    public LotteryLayout(@NonNull Context context) {
        this(context, null);
    }

    public LotteryLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private List<LotteryGiftEntity> mLotteryItems = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LotteryAdapter mAdapter;
    private PathLayoutManager mPathLayoutManager;

    private void initView() {
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutManager(mPathLayoutManager = new PathLayoutManager(null, 0, RecyclerView.HORIZONTAL));
        mRecyclerView.setAdapter(mAdapter = new LotteryAdapter(mLotteryItems));
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mAdapter.getItem(position).getItemType() == Lottery_ITEM_TYPE_START) {
                    if (!isRunning()) {
                        start();
                    }
                }
            }
        });
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mRecyclerView.setLayoutParams(params);
        addView(mRecyclerView);
    }

    protected LotteryAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        layoutChild(currentSize);
    }

    public void tryToStop(int position) {
        postDelayed(() -> {
            stayIndex = position;
            tryToStop = true;
        }, 3000);
    }

    public void start() {
        if (!isRunning()) {
            if (panelStateListener != null) {
                panelStateListener.onPanelStateStart();
            }
        }
    }

    public void setData(List<LotteryGiftEntity> data) {
        try {
            mLotteryItems.clear();
            mLotteryItems.addAll(data);
            layoutChild(data.size());
            mAdapter.setNewData(mLotteryItems);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Path currentPath;
    private int currentSize = 8;
    private int preSize;
    private boolean square = false;

    public void setSquare(boolean square) {
        this.square = square;
        layoutChild(currentSize);
    }

    public void setMarqueeMode(int marqueeMode) {
        this.MARQUEE_MODE = marqueeMode;
    }

    private void layoutChild(int size) {
        if (currentSize != size) {
            currentSize = size;
        }
        if (preSize == currentSize && currentPath != null) {
            return;
        }
        int height = mRecyclerView.getMeasuredHeight();
        int width = mRecyclerView.getMeasuredWidth();
        if (height == 0 || width == 0) {
            return;
        }
        try {
            int itemSize = SizeUtils.dp2px(75) / 2;
            currentPath = new Path();
            RectF rectF = new RectF(itemSize, itemSize, width - itemSize, height - itemSize);
            currentPath.addRoundRect(rectF, !square ? (width - itemSize) / 2 : 0, !square ? (height - itemSize) / 2 : 0, Path.Direction.CW);
            mPathLayoutManager.updatePath(currentPath, size);
            preSize = size;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startMarquee();
    }

    @Override
    protected void onDetachedFromWindow() {
        stopMarquee();
        super.onDetachedFromWindow();
    }

    private void stopMarquee() {
        marqueeRunning = false;
        running = false;
        tryToStop = false;
    }

    private void startMarquee() {
        marqueeRunning = true;
    }

    private long getInterruptTime() {
        currentTotal++;
        if (tryToStop) {
            currentSpeed += 10;
            if (currentSpeed > DEFAULT_SPEED) {
                currentSpeed = DEFAULT_SPEED;
            }
        } else {
            if (currentTotal / mAdapter.getItemCount() > 0) {
                currentSpeed -= 10;
            }
            if (currentSpeed < MIN_SPEED) {
                currentSpeed = MIN_SPEED;
            }
        }
        return currentSpeed;
    }

    public boolean isRunning() {
        return running;
    }

    public void startGame() {
        mHandler.sendEmptyMessage(RECOVER_IMG);
        running = true;
        tryToStop = false;
        currentSpeed = DEFAULT_SPEED;
        mHandler.sendEmptyMessageDelayed(START_GAME, getInterruptTime());
    }


    private PanelStateListener panelStateListener;

    public void setPanelStateListener(PanelStateListener panelStateListener) {
        this.panelStateListener = panelStateListener;
    }

    private LotteryHandler mHandler = new LotteryHandler(this);

    private static class LotteryHandler extends Handler {
        private Random random;
        private WeakReference<LotteryLayout> mLotteryLayoutWeakReference;

        public LotteryHandler(LotteryLayout lotteryLayout) {
            mLotteryLayoutWeakReference = new WeakReference<>(lotteryLayout);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case START_MARQUEE: {
                    LotteryLayout panelView = this.mLotteryLayoutWeakReference.get();
                    if (panelView != null) {
                        if (panelView.marqueeRunning) {
                            panelView.mHandler.sendEmptyMessageDelayed(START_MARQUEE, 350);
                        }
                    }
                }
                break;
                case START_GAME: {
                    LotteryLayout panelView = this.mLotteryLayoutWeakReference.get();
                    if (panelView != null) {
                        int preIndex = panelView.currentIndex;
                        int itemCount = panelView.getAdapter().getItemCount();
                        if (panelView.MARQUEE_MODE == MARQUEE_MODE_CW) {
                            panelView.currentIndex++;
                            if (panelView.currentIndex >= itemCount) {
                                panelView.currentIndex = 0;
                            }
                        } else if (panelView.MARQUEE_MODE == MARQUEE_MODE_CCW) {
                            panelView.currentIndex--;
                            if (panelView.currentIndex >= 0) {
                                panelView.currentIndex = itemCount;
                            }
                        } else if (panelView.MARQUEE_MODE == MARQUEE_MODE_RD) {
                            if (random == null) {
                                random = new Random(1);
                            }
                            int nextInt;
                            do {
                                nextInt = random.nextInt(itemCount);
                            } while (nextInt == preIndex);
                            panelView.currentIndex = nextInt;
                        }
                        int currentIndex = panelView.currentIndex;
                        panelView.getAdapter().getItem(preIndex).setFocus(false);
                        panelView.getAdapter().notifyItemChanged(preIndex);
                        panelView.getAdapter().getItem(currentIndex).setFocus(true);
                        panelView.getAdapter().notifyItemChanged(currentIndex);
                        if (panelView.tryToStop && panelView.currentSpeed == DEFAULT_SPEED
                                && panelView.stayIndex == panelView.currentIndex) {
                            panelView.running = false;
                            if (panelView.panelStateListener != null) {
                                panelView.panelStateListener.onPanelStateStop();
                            }
                        }
                        if (panelView.running) {
                            panelView.mHandler.sendEmptyMessageDelayed(START_GAME, panelView.getInterruptTime());
                        }
                    }
                }
                break;
                case RECOVER_IMG:
                    LotteryLayout lotteryLayout = this.mLotteryLayoutWeakReference.get();
                    if (lotteryLayout != null) {
                        for (LotteryGiftEntity item : lotteryLayout.getAdapter().getData()) {
                            item.setFocus(false);
                        }
                        lotteryLayout.getAdapter().notifyDataSetChanged();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public interface PanelStateListener {
        default void onPanelStateStart() {
        }

        default void onPanelStateStop() {
        }
    }
}
