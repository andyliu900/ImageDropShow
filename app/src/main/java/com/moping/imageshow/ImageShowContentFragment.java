package com.moping.imageshow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.moping.imageshow.adapter.ImageDraggingListener;
import com.moping.imageshow.adapter.ImageListDragDropListener;
import com.moping.imageshow.adapter.ImageLongPressListener;
import com.moping.imageshow.adapter.ImageShowAdapter;
import com.moping.imageshow.adapter.ImageTouchListener;
import com.moping.imageshow.base.BaseFragment;
import com.moping.imageshow.util.Constant;
import com.moping.imageshow.util.ScreenUtil;
import com.moping.imageshow.util.SharedPreferencesUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageShowContentFragment extends BaseFragment implements View.OnClickListener{

    private static final String imagePath = Environment.getExternalStorageDirectory().getPath() + Constant.ROOT_FILE_PATH + Constant.IMAGE_PATH;

    private RecyclerView image_pick_content;
    private View indecator;
    private FrameLayout dragItemViewGroup;
    private View deleteLayout;

    private ImageShowAdapter adapter;

    private ImageView currentCapturedView;
    private List<ImageView> attachedImageViews = new ArrayList();

    enum ToggleState {
        OPENED, // 已经打开面板
        ACTIONING,  // 正在执行打开活着关闭操作
        CLOSED  // 已经关闭面板
    }

    private ToggleState currentToggleState = ToggleState.CLOSED;
    private int currentFolderId = -1;
    private List<String> imagePathList = new ArrayList();

    // 当前拖动的图片的左上角坐标
    int leftX = 0;
    int topY = 0;
    int vibratorCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_imageshowcontent, null);

        image_pick_content = (RecyclerView) view.findViewById(R.id.image_pick_content);
        image_pick_content.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.listdevider_bg));
        image_pick_content.addItemDecoration(divider);

        adapter = new ImageShowAdapter(mContext, new ImageLongPressListener() {
            @Override
            public void onImageLongPress(int position, View view) {
                if (view instanceof ImageView) {
                    adapter.setCurrentSelectedPosition(position);
                    adapter.notifyDataSetChanged();

                    ImageView selectImageView = (ImageView)view;
                    selectImageView.setOnDragListener(new ImageListDragDropListener(mContext));

                    ClipData.Item item = new ClipData.Item(view.getTag().toString());
                    ClipData dragData = new ClipData(view.getTag().toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                    view.startDrag(dragData,  // the data to be dragged
                            myShadow,  // the drag shadow builder
                            null,      // no need to use local data
                            0 );

                }
            }
        });
        image_pick_content.setAdapter(adapter);

        indecator = view.findViewById(R.id.indecator);
        indecator.setOnClickListener(this);

        dragItemViewGroup = (FrameLayout)view.findViewById(R.id.dragItemViewGroup);

        dragItemViewGroup.setOnDragListener(new ImageListDragDropListener(mContext, new ImageListDragDropListener.OnDragEndCallback(){

            @Override
            public void dragEnd(ImageView dragView) {
                dragView.setOnTouchListener(new ImageTouchListener(new ImageDraggingListener() {
                    @Override
                    public void viewCaptured(View captureView) {
                        if (captureView instanceof ImageView) {
                            ImageView captureImageView = (ImageView)captureView;
                            if (currentCapturedView == null || !currentCapturedView.equals(captureImageView)) {
                                currentCapturedView = captureImageView;

                                attachedImageViews.remove(captureImageView);
                                attachedImageViews.add(attachedImageViews.size(), currentCapturedView);

                                dragItemViewGroup.removeAllViews();
                                genAttacthedImageViews();
                            }
                        }
                    }

                    @Override
                    public void clamLeftTop(View captureView, int left, int top) {
                        leftX = left;
                        topY = top;
                        deleteLayout.setVisibility(View.VISIBLE);

                        if (isMiddlePointInCycle(captureView)) {
                            Vibrator vibrator = (Vibrator)mContext.getSystemService(mContext.VIBRATOR_SERVICE);
                            if (vibratorCount == 0) {
                                vibrator.vibrate(100);
                                vibratorCount++;
                            }
                        } else {
                            vibratorCount = 0;
                        }
                    }

                    @Override
                    public void released(View releasedChild) {
                        deleteLayout.setVisibility(View.GONE);

                        if (deleteLayout != null && isMiddlePointInCycle(releasedChild)) {
                            attachedImageViews.remove(releasedChild);
                            dragItemViewGroup.removeView(releasedChild);
                        }
                    }
                }));
                attachedImageViews.add(dragView);
                genAttacthedImageViews();
            }
        }));

        deleteLayout = view.findViewById(R.id.delete_layout);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initCurrentFolderId(1);
    }

    @Override
    public void onClick(View view) {
        doActionImagePickLayout(currentFolderId);
    }

    public void initCurrentFolderId(int folderId) {
        currentFolderId = folderId;
        showPicList(folderId);
    }

    public void doActionImagePickLayout(int folderId) {
        if (currentFolderId != folderId) {
            if (currentToggleState == ToggleState.CLOSED && currentToggleState != ToggleState.ACTIONING) {
                showImagePickLayout(folderId);
            } else if (currentToggleState == ToggleState.OPENED && currentToggleState != ToggleState.ACTIONING) {
                showPicList(folderId);
            }
        } else {
            if (currentToggleState == ToggleState.CLOSED && currentToggleState != ToggleState.ACTIONING) {
                showImagePickLayout(folderId);
            } else if (currentToggleState == ToggleState.OPENED && currentToggleState != ToggleState.ACTIONING) {
                hideImagePickLayout();
            }
        }

        currentFolderId = folderId;
    }

    private void showImagePickLayout(final int folderId) {
        if ((boolean)SharedPreferencesUtils.getParam(mContext, Constant.PICKIMAGE_TIPS_KEY, true)) {
            Snackbar.make(dragItemViewGroup, "长按图片进行拖动", Snackbar.LENGTH_LONG)
                    .setAction("知道了", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            SharedPreferencesUtils.setParam(mContext, Constant.PICKIMAGE_TIPS_KEY, false);
                        }
                    }).show();
        }
        if (image_pick_content != null) {
            ValueAnimator animator = ValueAnimator.ofInt(0, ScreenUtil.dp2px(mContext, 240));
            animator.setDuration(500);
            animator.setRepeatCount(0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int currentValue = (Integer)valueAnimator.getAnimatedValue();
                    image_pick_content.getLayoutParams().width = currentValue;
                    image_pick_content.requestLayout();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    currentToggleState = ToggleState.ACTIONING;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    currentToggleState = ToggleState.OPENED;
                    showPicList(folderId);
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                    hideImagePickLayout();
                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            animator.start();
        }
    }

    private void hideImagePickLayout() {
        if (image_pick_content != null) {
            ValueAnimator animator = ValueAnimator.ofInt(ScreenUtil.dp2px(mContext, 240), 0);
            animator.setDuration(500);
            animator.setRepeatCount(0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int currentValue = (Integer)valueAnimator.getAnimatedValue();
                    image_pick_content.getLayoutParams().width = currentValue;
                    image_pick_content.requestLayout();
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    currentToggleState = ToggleState.ACTIONING;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    currentToggleState = ToggleState.CLOSED;
                    adapter.setCurrentSelectedPosition(-1);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            animator.start();
        }
    }

    private void showPicList(int folderId) {
        adapter.setCurrentSelectedPosition(-1);
        imagePathList.clear();
        if (folderId == 1) {
            String folderOne = imagePath + Constant.IMAGE_FOLDER_ONE;
            File folderOneFile = new File(folderOne);
            if (folderOneFile.exists()) {
                File[] files = folderOneFile.listFiles();
                for (File file : files) {
                    if (checkPrefix(file.getName())) {
                        imagePathList.add(file.getAbsolutePath());
                    }
                }
                adapter.setImageRes(imagePathList);
                adapter.notifyDataSetChanged();
            }
        } else if (folderId == 2) {
            String folderTwo = imagePath + Constant.IMAGE_FOLDER_TWO;
            File folderTwoFile = new File(folderTwo);
            if (folderTwoFile.exists()) {
                File[] files = folderTwoFile.listFiles();
                for (File file : files) {
                    if (checkPrefix(file.getName())) {
                        imagePathList.add(file.getAbsolutePath());
                    }
                }
                adapter.setImageRes(imagePathList);
                adapter.notifyDataSetChanged();
            }
        } else if (folderId == 3) {
            String folderThree = imagePath + Constant.IMAGE_FOLDER_THREE;
            File folderThreeFile = new File(folderThree);
            if (folderThreeFile.exists()) {
                File[] files = folderThreeFile.listFiles();
                for (File file : files) {
                    if (checkPrefix(file.getName())) {
                        imagePathList.add(file.getAbsolutePath());
                    }
                }
                adapter.setImageRes(imagePathList);
                adapter.notifyDataSetChanged();
            }
        } else if (folderId == 4) {
            String folderFour = imagePath + Constant.IMAGE_FOLDER_FOUR;
            File folderFourFile = new File(folderFour);
            if (folderFourFile.exists()) {
                File[] files = folderFourFile.listFiles();
                for (File file : files) {
                    if (checkPrefix(file.getName())) {
                        imagePathList.add(file.getAbsolutePath());
                    }
                }
                adapter.setImageRes(imagePathList);
                adapter.notifyDataSetChanged();
            }
        } else if (folderId == 5) {
            String folderFive = imagePath + Constant.IMAGE_FOLDER_FIVE;
            File folderFiveFile = new File(folderFive);
            if (folderFiveFile.exists()) {
                File[] files = folderFiveFile.listFiles();
                for (File file : files) {
                    if (checkPrefix(file.getName())) {
                        imagePathList.add(file.getAbsolutePath());
                    }
                }
                adapter.setImageRes(imagePathList);
                adapter.notifyDataSetChanged();
            }
        } else if (folderId == 6) {
            String folderSix = imagePath + Constant.IMAGE_FOLDER_SIX;
            File folderSixFile = new File(folderSix);
            if (folderSixFile.exists()) {
                File[] files = folderSixFile.listFiles();
                for (File file : files) {
                    if (checkPrefix(file.getName())) {
                        imagePathList.add(file.getAbsolutePath());
                    }
                }
                adapter.setImageRes(imagePathList);
                adapter.notifyDataSetChanged();
            }
        }
    }

    private boolean checkPrefix(String fileName) {
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (prefix.equals("jpg")
                || prefix.equals("jpeg")
                || prefix.equals("png")
                || prefix.equals("gif")
                || prefix.equals("JPG")
                || prefix.equals("JPEG")
                || prefix.equals("PNG")
                || prefix.equals("GIF")) {
            return true;
        }
        return false;
    }

    private void genAttacthedImageViews() {
        if (attachedImageViews != null) {
            for (ImageView imageView : attachedImageViews) {
                if (imageView.getParent() == null && dragItemViewGroup != null) {
                    dragItemViewGroup.addView(imageView);
                }
            }
        }
    }

    /**
     * 计算坐标点是否落在圆形区域内
     *
     * @param child
     * @return
     */
    private boolean isMiddlePointInCycle(View child) {
        if (dragItemViewGroup != null) {
            int x = leftX + child.getMeasuredWidth() / 2;
            int y = topY + child.getMeasuredHeight() / 2;

            int cycleX = dragItemViewGroup.getMeasuredWidth();
            int cycleY = dragItemViewGroup.getMeasuredHeight();

            int r = ScreenUtil.dp2px(mContext, 150);

            // 圆的一般方程  (x - a )^2 + (y - b)^2 = r^2
            int firstPart = (x - cycleX) * (x - cycleX);
            int secondPart = (y - cycleY) * (y - cycleY);

            if ((firstPart + secondPart) <= r * r) { // 中心点在园内
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
