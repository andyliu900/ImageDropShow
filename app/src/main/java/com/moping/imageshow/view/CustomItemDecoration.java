package com.moping.imageshow.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class CustomItemDecoration extends RecyclerView.ItemDecoration {

    private int mLeftSpace;
    private int mTopSpace;
    private int mRightSpace;
    private int mBottomSpace;

    public CustomItemDecoration(int topSpace) {
        this(0, topSpace, 0, 0);
    }

    public CustomItemDecoration(int leftSpace, int topSpace, int rightSpace, int bottomSpace) {
        mLeftSpace = leftSpace;
        mTopSpace = topSpace;
        mRightSpace = rightSpace;
        mBottomSpace = bottomSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.left = mLeftSpace;
        outRect.right = mRightSpace;
        outRect.bottom = mBottomSpace;

        if (parent.getChildPosition(view) == 0) {
            outRect.top = 0;
        } else {
            outRect.top = mTopSpace;
        }
    }
}
