package com.evan.xtool.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * Created by evan on 2015/10/11.
 */
public class FlowLayout extends ViewGroup {

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    /**
     * 进行测量，负责设置子控件的测试模式和大小，根据所有的子控件设置自己的宽和高
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获得它的父容器为它设置的测试模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //如果是warp_content情况下，记录宽和高
        int width = 0;
        int height = 0;

        //每一行的高度，width不断取最大宽度
        int lineWidth = 0;
        //每一行的高度，累加至height
        int lineHeight = 0;

        int count = getChildCount();

        //循环子控件
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            //测量每一个View的宽和高，
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到child的LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            //当前子控件实际占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //当前子控件实际占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            //如果加入当前child,超出了最大宽度，则把最大宽度设置为width,累加height，然后开启新行
            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);
                height += lineHeight;
                lineWidth = childWidth; //另起一行，记录新行宽度
                lineHeight = childHeight; //记录新行的高度
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }

            //如果是最后一个，则将当前记录的最大宽度和当前的lineWidth做比较
            if (i == count - 1) {
                width = Math.max(lineWidth, childWidth);
                height += lineHeight;
            }

            setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width, (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
        }
    }

    /**
     * 存储所有的View，按行记录
     */
    private List<List<View>> mAllViews = new ArrayList<List<View>>();
    /**
     * 记录每一行的最大高度
     */
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAllViews.clear();
        mLineHeight.clear();

        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        List<View> mLineViews = new ArrayList<View>();
        int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            MarginLayoutParams lp = ((MarginLayoutParams) child.getLayoutParams());
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > width) {

                //记录这一行所有的View以及最大高度
                mLineHeight.add(lineHeight);
                mAllViews.add(mLineViews);
                lineWidth = 0;
                mLineViews = new ArrayList<>();
            }

            //不需要换行，直接累加
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            mLineViews.add(child);
        }

        // 记录最后一行
        mLineHeight.add(lineHeight);
        mAllViews.add(mLineViews);

        int left = 0;
        int top = 0;
        // 得到总行数
        int lineNums = mAllViews.size();
        for (int i = 0; i < lineNums; i++) {
            //每一行的Views
            mLineViews = mAllViews.get(i);
            //当前行的高度
            lineHeight = mLineHeight.get(i);

            //遍历当前行所有的View
            for (int j = 0; j < mLineViews.size(); j++) {
                View child = mLineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }
                MarginLayoutParams lp = ((MarginLayoutParams) child.getLayoutParams());

                //计算child的left,right,top,bottom
                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

                child.layout(lc, tc, rc, bc);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }

            left = 0;
            top += lineHeight;

        }
    }
}
