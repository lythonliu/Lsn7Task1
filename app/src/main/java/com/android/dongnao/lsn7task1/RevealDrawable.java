package com.android.dongnao.lsn7task1;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;

/**
 * Created by user on 2017/5/20.
 */

public class RevealDrawable extends Drawable {

    private Drawable mGrayDrawable;
    private Drawable mColorDrawable;

    private Rect rect = new Rect();

    public RevealDrawable(Drawable gray, Drawable color) {
        mGrayDrawable = gray;
        mColorDrawable = color;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // 得到level值
        int level = getLevel();

        int icon_width = getBounds().width();
        int icon_height = getBounds().height();

        // 显示灰色
        if (level == 0) {
            mGrayDrawable.draw(canvas);
        } else {
            // level值<=width为index_left图片
            if (level <= icon_width) {
                canvas.save();
                // 取得灰色区域
                Gravity.apply(Gravity.LEFT, icon_width - level, icon_height, getBounds(), rect);
                // clip灰色区域
                canvas.clipRect(rect);
                // 绘制灰色区域
                mGrayDrawable.draw(canvas);
                canvas.restore();

                canvas.save();
                // 取得彩色区域
                Gravity.apply(Gravity.RIGHT, level, icon_height, getBounds(), rect);
                // clip彩色区域
                canvas.clipRect(rect);
                // 绘制彩色区域
                mColorDrawable.draw(canvas);
                canvas.restore();
            }
            // level值>width为index_right图片
            else {
                canvas.save();
                // 取得彩色区域
                Gravity.apply(Gravity.LEFT, level - icon_width, icon_height, getBounds(), rect);
                // clip彩色区域
                canvas.clipRect(rect);
                // 绘制彩色区域
                mColorDrawable.draw(canvas);
                canvas.restore();

                canvas.save();
                // 取得灰色区域
                Gravity.apply(Gravity.RIGHT, icon_width - (level - icon_width), icon_height, getBounds(), rect);
                // clip灰色区域
                canvas.clipRect(rect);
                // 绘制灰色区域
                mGrayDrawable.draw(canvas);
                canvas.restore();
            }
        }
    }

    @Override
    public boolean onLevelChange(int level) {
        invalidateSelf(); // 重绘自己
        return true;
    }

    @Override
    public void onBoundsChange(Rect bounds) {
        mGrayDrawable.setBounds(bounds);
        mColorDrawable.setBounds(bounds);
        super.onBoundsChange(bounds);
    }

    @Override
    public int getIntrinsicWidth() {
        return Math.max(mColorDrawable.getIntrinsicWidth(), mGrayDrawable.getIntrinsicWidth());
    }

    @Override
    public int getIntrinsicHeight() {
        return Math.max(mColorDrawable.getIntrinsicHeight(), mGrayDrawable.getIntrinsicHeight());
    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }
}
