package com.moping.dragsample;

import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String IMAGEVIEW_TAG = "icon bitmap";

    private ImageView imageView;

    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=(ImageView)findViewById(R.id.imageView);
        frameLayout=(FrameLayout)findViewById(R.id.content_fragment);
        frameLayout2=(FrameLayout)findViewById(R.id.content_fragment2);

        imageView.setTag(IMAGEVIEW_TAG);

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                view.getBackground().setAlpha(100);

                ClipData.Item item = new ClipData.Item(view.getTag().toString());

                ClipData dragData = new ClipData(view.getTag().toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                view.startDrag(dragData,  // the data to be dragged
                        myShadow,  // the drag shadow builder
                        null,      // no need to use local data
                        0 );

                return true;
            }
        });

        frameLayout.setOnDragListener(new myDragEventListener());
        frameLayout2.setOnDragListener(new myDragEventListener());
        imageView.setOnDragListener(new myDragEventListener());

    }

    class myDragEventListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {

            final int action = dragEvent.getAction();

            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                        view.invalidate();
                        return true;
                    }
                    return false;
                case DragEvent.ACTION_DRAG_ENTERED:
                    view.invalidate();

                    return true;
                case DragEvent.ACTION_DRAG_LOCATION:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    view.invalidate();

                    return true;
                case DragEvent.ACTION_DROP:
                    ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                    String dragData = item.getText().toString();

                    Toast.makeText(MainActivity.this, "Dragged data is " + dragData, Toast.LENGTH_LONG).show();

                    if(view instanceof FrameLayout){
                        ((ViewGroup)imageView.getParent()).removeView(imageView);
                        ((FrameLayout)view).addView(imageView);
                    }

                    view.invalidate();

                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    view.invalidate();

//                    if (dragEvent.getResult()) {
//                        Toast.makeText(MainActivity.this, "The drop was handled.", Toast.LENGTH_LONG).show();
//
//                    } else {
//                        Toast.makeText(MainActivity.this, "The drop didn't work.", Toast.LENGTH_LONG).show();
//
//                    }
                    imageView.getBackground().setAlpha(255);

                    return true;
                default:
                    Log.e("DragDrop Example","Unknown action type received by OnDragListener.");
                    break;
            }

            return false;
        }
    }
}
