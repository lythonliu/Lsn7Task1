package com.android.dongnao.lsn7task1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by user on 2017/5/20.
 */

public class Lsn7HorizontalScrollView extends HorizontalScrollView {
    private final static String TAG = "Lsn7HScrollView";

    private LinearLayout container;

    // 指示图标的宽度
    private int icon_width;
    // 中心X坐标值
    private int mCenterX;
    private boolean isFirstAdd = true;

    public Lsn7HorizontalScrollView(Context context) {
        super(context, null);
    }

    public Lsn7HorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        container = new LinearLayout(context);
        container.setLayoutParams(layoutparams);
        addView(container);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onLayout");

        if (container.getChildCount() > 0) {
            icon_width = container.getChildAt(0).getWidth();
        }

        mCenterX = getWidth() / 2;

        // 在container中间只留出一张图片的宽度，只显示一张图片，就只仅仅留下足够一张图片显示彩色
        container.setPadding(mCenterX - icon_width / 2, 0, mCenterX - icon_width / 2, 0);

        // 第一次addView时，给第一张设置level值，它就会显示彩色
        if (isFirstAdd && container.getChildCount() > 0) {
            ((ImageView) container.getChildAt(0)).setImageLevel(icon_width);
            isFirstAdd = false;
        }
    }

    private void reveal() {
        if (container.getChildCount() <= 0) {
            return;
        }
        //滚动距离
        int scrollX = getScrollX();
        scrollX = scrollX <= 0 ? 0 : scrollX;

        // 最多有两张图片会有部分显示彩色，两张图片的彩色部分相加刚好等于一张图片的宽度
        // 滚动出了多少张图之和的宽度，就是第几张图片0<width<=icon_width的宽度显示彩色。这里要的是索引值，得到的值刚好是第n张-1的值
        // 两张图片用index_left和index_right来表示,它们的值是图片的索引值
        int index_left = scrollX / icon_width; // 两张图片的第一张图片
        int index_right = index_left + 1; // 因为只能是紧邻的两张图片有可能，所以就是两张图片的第一张图片的下一张图片

        // index_left图片先在container可显示空间内的宽度的大小
        int leftSelectWidth = icon_width - scrollX % icon_width;

        for (int i = 0; i < container.getChildCount(); i++) {

            ImageView iv = (ImageView) container.getChildAt(i);

            // 根据图片在container可显示空间的大小，给图片设置一个Level值
            // 当图片完全不在container可显示空间内，都给设置一个level值为0
            // 除了index_left和index_right外其余图片都不现在在container可显示空间内，所以设置leve值为0
            if (i != index_left && i != index_right) {
                iv.setImageLevel(0);
            } else {
                // 当index_left图片有部分显示在container可显示空间内，显示的宽度就是level的值
                if (index_left == i) {
                    iv.setImageLevel(leftSelectWidth);
                } else {
                    // 如果为index_right图片就设置level值为icon_width + (icon_width - leftSelectWidth)
                    // 在计算index_right图片显示彩色宽度时，直接用level值-icon_width就得到了
                    iv.setImageLevel(leftSelectWidth == icon_width ? 0 : (icon_width + (icon_width - leftSelectWidth)));
                }
            }
        }
    }

    public void addContainerChildViews(Drawable[] drawables) {
        if (drawables == null) {
            return;
        }
        for (int i = 0; i < drawables.length; i++) {
            ImageView iv = new ImageView(getContext());
            iv.setImageDrawable(drawables[i]);
            container.addView(iv);
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        reveal();
    }
}
