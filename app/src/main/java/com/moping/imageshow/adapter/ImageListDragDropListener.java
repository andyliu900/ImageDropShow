package com.moping.imageshow.adapter;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moping.imageshow.util.ScreenUtil;

public class ImageListDragDropListener implements View.OnDragListener {

    private Context mContext;
    private OnDragEndCallback mOnDragEndCallback;

    public ImageListDragDropListener(Context context) {
        this.mContext = context;
    }

    public ImageListDragDropListener(Context context, OnDragEndCallback callback) {
        this.mContext = context;
        this.mOnDragEndCallback = callback;
    }

    @Override
    public boolean onDrag(View v, DragEvent event) {
        
        final int action = event.getAction();
        
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    v.invalidate();
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.invalidate();

                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                Log.i("TAG", "ACTION_DRAG_LOCATION x:" + event.getX() + "  y:" + event.getY());
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                v.invalidate();

                return true;
            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                String dragData = item.getText().toString();

                System.out.println("dragData:" + dragData);

                if(v instanceof FrameLayout){

                    ImageView dragImageView = new ImageView(mContext);
                    Bitmap bitmap = ScreenUtil.decodeSampleBitmapFromFile(dragData, ScreenUtil.getScreenWidth(mContext) / 4);
                    dragImageView.setImageBitmap(bitmap);

                    int[] resultWH = ScreenUtil.calculateZoomWHByFile(dragData, ScreenUtil.getScreenWidth(mContext) / 4);

                    ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(
                            resultWH[0],
                            resultWH[1]);
                    marginParams.setMargins((int)event.getX() - resultWH[0] / 2, (int)event.getY() - resultWH[1] / 2, 0, 0);

                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(marginParams);
                    dragImageView.setLayoutParams(layoutParams);

                    ((FrameLayout)v).addView(dragImageView);

                    if (mOnDragEndCallback != null) {
                        mOnDragEndCallback.dragEnd(dragImageView);
                    }
                }

                return true;
            case DragEvent.ACTION_DRAG_ENDED:

                if (event.getResult()) {
                    System.out.println("The drop was handled.");
                } else {
                    System.out.println("The drop didn't work.");
                }

                return true;
            default:
                Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                break;
        }

        return false;
    }

    public interface OnDragEndCallback {
        void dragEnd(ImageView dragView);
    }

}
