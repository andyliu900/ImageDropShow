package com.moping.imageshow.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moping.imageshow.R;
import com.moping.imageshow.util.ScreenUtil;

import java.util.List;

public class ImageShowAdapter extends RecyclerView.Adapter<ImageShowAdapter.ViewHolder> {

    private Context mContext;
    private int currentSelectedPosition = -1;
    private List<String> mImageRes;
    private ImagePullOutListener mListener;

    // 纵向滚动不触发drag事件
    private float x, y;

    public ImageShowAdapter(Context context, ImagePullOutListener listener) {
        mContext = context;
        mListener = listener;
    }

    public void setImageRes(List<String> imageRes) {
        this.mImageRes = imageRes;
    }

    public void setCurrentSelectedPosition(int selectedPosition) {
        currentSelectedPosition = selectedPosition;
    }

    @Override
    public ImageShowAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        ImageShowAdapter.ViewHolder viewHolder = new ImageShowAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageShowAdapter.ViewHolder holder, final int position) {
        if (position == currentSelectedPosition) {
            holder.imageView.setPadding(5,5,5,5);
            holder.imageView.setBackgroundResource(R.drawable.selected_img_bg);
        } else {
            holder.imageView.setPadding(0,0,0,0);
            holder.imageView.setBackgroundResource(android.R.color.transparent);
        }
        holder.imageView.setImageBitmap(ScreenUtil.decodeSampleBitmapFromFile(mImageRes.get(position),
                ScreenUtil.dp2px(mContext, 240)));
        holder.imageView.setTag(mImageRes.get(position)); // 设定图片路径

        int[] resultWH = ScreenUtil.calculateZoomWHByFile(mImageRes.get(position), ScreenUtil.dp2px(mContext, 220));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resultWH[0], resultWH[1]);
        layoutParams.gravity = Gravity.CENTER;
        holder.imageView.setLayoutParams(layoutParams);

        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        x = event.getX();
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX();
                        float dy = event.getY();

                        float absX = Math.abs(dx - x);
                        float absY = Math.abs(dy - y);

                        if (absX == 0 || absY == 0) {
                            return false;
                        } else {
                            if ((absX / absY) >= 1.732) {
                                if (mListener != null) {
                                    mListener.onImagePullOut(position, view);
                                }
                            } else {
                                return false;
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mImageRes == null ? 0 : mImageRes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

}
