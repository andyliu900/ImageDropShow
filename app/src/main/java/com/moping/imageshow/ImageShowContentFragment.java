package com.moping.imageshow;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
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
import android.widget.RelativeLayout;

import com.moping.imageshow.adapter.ImageDraggingListener;
import com.moping.imageshow.adapter.ImageListDragDropListener;
import com.moping.imageshow.adapter.ImagePullOutListener;
import com.moping.imageshow.adapter.ImageShowAdapter;
import com.moping.imageshow.adapter.ImageTouchListener;
import com.moping.imageshow.base.BaseFragment;
import com.moping.imageshow.util.Constant;
import com.moping.imageshow.util.FileUtil;
import com.moping.imageshow.util.ScreenUtil;
import com.moping.imageshow.util.SharedPreferencesUtils;
import com.moping.imageshow.view.dispatchview.DispatchImageView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ImageShowContentFragment extends BaseFragment implements View.OnClickListener {

    public static final String RESET_IMAGEINDEX = "resetImageIndexBg";
    private static final int REQUEST_CODE_CHOOSE = 101;
    private static final String imagePath = Environment.getExternalStorageDirectory().getPath() + Constant.ROOT_FILE_PATH + Constant.IMAGE_PATH;

    private ViewGroup image_pick_layout;
    private RecyclerView image_pick_list;
    private View addimage_btn;
    private View indecator;
//    private DragHelperItemViewGroup dragItemViewGroup;
        private FrameLayout dragItemViewGroup;
    private View deleteLayout;

    private ImageShowAdapter adapter;
    private List<Uri> mSelected;

    private DispatchImageView currentCapturedView;
    private List<DispatchImageView> attachedImageViews = new ArrayList();

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

        image_pick_layout = (RelativeLayout)view.findViewById(R.id.image_pick_layout);
        image_pick_layout.setOnClickListener(null);
        image_pick_list = (RecyclerView) view.findViewById(R.id.image_pick_list);
        image_pick_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(mContext, R.drawable.listdevider_bg));
        image_pick_list.addItemDecoration(divider);
        addimage_btn = view.findViewById(R.id.addimage_btn);
        addimage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Matisse.from(ImageShowContentFragment.this)
                        .choose(MimeType.ofImage()) // 选择 mime 的类型
                        .countable(true)
                        .maxSelectable(9) // 图片选择的最多数量
                        .theme(R.style.Matisse_Dracula)
//                        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
                        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                        .thumbnailScale(0.85f) // 缩略图的比例
                        .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                        .forResult(REQUEST_CODE_CHOOSE); // 设置作为标记的请求码
            }
        });

        adapter = new ImageShowAdapter(mContext, new ImagePullOutListener() {
            @Override
            public void onImagePullOut(int position, View view) {
                if (view instanceof ImageView) {
                    adapter.setCurrentSelectedPosition(position);
                    adapter.notifyDataSetChanged();

                    ImageView selectImageView = (ImageView) view;
                    selectImageView.setOnDragListener(new ImageListDragDropListener(mContext));

                    ClipData.Item item = new ClipData.Item(view.getTag().toString());
                    ClipData dragData = new ClipData(view.getTag().toString(), new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);

                    View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);
                    view.startDrag(dragData,  // the data to be dragged
                            myShadow,  // the drag shadow builder
                            null,      // no need to use local data
                            0);

                }
            }
        });
        image_pick_list.setAdapter(adapter);

        indecator = view.findViewById(R.id.indecator);
        indecator.setOnClickListener(this);

        dragItemViewGroup = (FrameLayout) view.findViewById(R.id.dragItemViewGroup);
        dragItemViewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentToggleState == ToggleState.OPENED && currentToggleState != ToggleState.ACTIONING) {
                    hideImagePickLayout();
                }
            }
        });

        dragItemViewGroup.setOnDragListener(new ImageListDragDropListener(mContext, new ImageListDragDropListener.OnDragEndCallback() {

            @Override
            public void dragEnd(DispatchImageView dragView) {
                dragView.setOnTouchListener(new ImageTouchListener(new ImageDraggingListener() {
                    @Override
                    public void viewCaptured(View captureView) {
                        if (captureView instanceof DispatchImageView) {
                            DispatchImageView captureImageView = (DispatchImageView)captureView;
                            if (currentCapturedView == null || !currentCapturedView.equals(captureImageView)) {
                                currentCapturedView = captureImageView;

                                attachedImageViews.remove(captureImageView);
                                attachedImageViews.add(attachedImageViews.size(), currentCapturedView);

                                dragItemViewGroup.removeAllViews();
                                genAttacthedImageViews(false);
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
                    public void released(View releasedChild, float totalAngle, float scale, ResetCallBack callBack) {
                        deleteLayout.setVisibility(View.GONE);

                        if (releasedChild instanceof DispatchImageView) {
                            DispatchImageView captureImageView = (DispatchImageView) releasedChild;
                            captureImageView.setRotate(totalAngle, callBack);
                            captureImageView.setScale(scale);
                        }

                        if (deleteLayout != null && isMiddlePointInCycle(releasedChild)) {
                            attachedImageViews.remove(releasedChild);
                            dragItemViewGroup.removeView(releasedChild);
                        }
                    }
                }));

                currentCapturedView = dragView;
                attachedImageViews.add(dragView);
                genAttacthedImageViews(true);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);

            List<String> extendImages = new ArrayList();
            for (Uri uri : mSelected) {
                String path = FileUtil.getRealPathFromUri(mContext, uri);
                extendImages.add(path);
            }

            imagePathList.addAll(extendImages);
            adapter.setImageRes(imagePathList);
            adapter.notifyDataSetChanged();

            // 将图片复制到指定文件夹
            for (String path : extendImages) {
                String fileName = path.substring(path.lastIndexOf("/") + 1, path.length());
                String folderPath = null;
                String target = null;
                if (currentFolderId == 1) {
                    folderPath = imagePath + Constant.IMAGE_FOLDER_ONE;
                } else if (currentFolderId == 2) {
                    folderPath = imagePath + Constant.IMAGE_FOLDER_TWO;
                } else if (currentFolderId == 3) {
                    folderPath = imagePath + Constant.IMAGE_FOLDER_THREE;
                } else if (currentFolderId == 4) {
                    folderPath = imagePath + Constant.IMAGE_FOLDER_FOUR;
                } else if (currentFolderId == 5) {
                    folderPath = imagePath + Constant.IMAGE_FOLDER_FIVE;
                } else if (currentFolderId == 6) {
                    folderPath = imagePath + Constant.IMAGE_FOLDER_SIX;
                } else {
                    folderPath = imagePath + Constant.IMAGE_FOLDER_ONE;
                }
                target = folderPath + File.separator + fileName;
                FileUtil.copyFile(path, target);
            }
        }
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
        if ((boolean) SharedPreferencesUtils.getParam(mContext, Constant.PICKIMAGE_TIPS_KEY, true)) {
            Snackbar.make(dragItemViewGroup, "拖动图片到空白区域", Snackbar.LENGTH_LONG)
                    .setAction("知道了", new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            SharedPreferencesUtils.setParam(mContext, Constant.PICKIMAGE_TIPS_KEY, false);
                        }
                    }).show();
        }
        if (image_pick_layout != null) {
            ValueAnimator animator = ValueAnimator.ofInt(0, ScreenUtil.dp2px(mContext, 240));
            animator.setDuration(500);
            animator.setRepeatCount(0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int currentValue = (Integer) valueAnimator.getAnimatedValue();
                    image_pick_layout.getLayoutParams().width = currentValue;
                    image_pick_layout.requestLayout();
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
        if (image_pick_layout != null) {
            ValueAnimator animator = ValueAnimator.ofInt(ScreenUtil.dp2px(mContext, 240), 0);
            animator.setDuration(500);
            animator.setRepeatCount(0);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int currentValue = (Integer) valueAnimator.getAnimatedValue();
                    image_pick_layout.getLayoutParams().width = currentValue;
                    image_pick_layout.requestLayout();
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
                    imagePathList.clear();
                    adapter.setImageRes(imagePathList);
                    adapter.notifyDataSetChanged();

                    mFunctionManager.invokeFunc(RESET_IMAGEINDEX);
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

    private void genAttacthedImageViews(boolean isDrop) {
        if (attachedImageViews != null) {
            for (DispatchImageView imageView : attachedImageViews) {
                if (isDrop) {
                    if (imageView.equals(currentCapturedView)) {
                        imageView.setViewInFront(true);
                    } else {
                        imageView.setViewInFront(false);
                    }
                } else {
                    if (imageView.getParent() == null && dragItemViewGroup != null) {
                        if (imageView.equals(currentCapturedView)) {
                            imageView.setViewInFront(true);
                        } else {
                            imageView.setViewInFront(false);
                        }
                        dragItemViewGroup.addView(imageView);
                    }
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
