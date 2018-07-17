package com.moping.imageshow.base;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.moping.imageshow.util.Constant;
import com.moping.imageshow.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ImageShowApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("Time", "start:" + System.currentTimeMillis());

        initImageFolders();

        initImages();

        Log.i("Time", "end:" + System.currentTimeMillis());
    }

    private void initImageFolders() {
        String imagePath = Environment.getExternalStorageDirectory().getPath() + Constant.ROOT_FILE_PATH + Constant.IMAGE_PATH;
        File imageRootFile = new File(imagePath);
        if (!imageRootFile.exists()) {
            imageRootFile.mkdirs();
        }

        String folderOne = imagePath + Constant.IMAGE_FOLDER_ONE;
        File folderOneFile = new File(folderOne);
        if (!folderOneFile.exists()) {
            folderOneFile.mkdirs();
        }

        String folderTwo = imagePath + Constant.IMAGE_FOLDER_TWO;
        File folderTwoFile = new File(folderTwo);
        if (!folderTwoFile.exists()) {
            folderTwoFile.mkdirs();
        }

        String folderThree = imagePath + Constant.IMAGE_FOLDER_THREE;
        File folderThreeFile = new File(folderThree);
        if (!folderThreeFile.exists()) {
            folderThreeFile.mkdirs();
        }

        String folderFour = imagePath + Constant.IMAGE_FOLDER_FOUR;
        File folderFourFile = new File(folderFour);
        if (!folderFourFile.exists()) {
            folderFourFile.mkdirs();
        }

        String folderFive = imagePath + Constant.IMAGE_FOLDER_FIVE;
        File folderFiveFile = new File(folderFive);
        if (!folderFiveFile.exists()) {
            folderFiveFile.mkdirs();
        }

        String folderSix = imagePath + Constant.IMAGE_FOLDER_SIX;
        File folderSixFile = new File(folderSix);
        if (!folderSixFile.exists()) {
            folderSixFile.mkdirs();
        }

    }

    private void initImages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String imageBasePath = Environment.getExternalStorageDirectory().getPath() + Constant.ROOT_FILE_PATH + Constant.IMAGE_PATH;

                    String[] folderList = getAssets().list("init");
                    for (int i = 0; i < folderList.length; i++) {
                        String folder = folderList[i];

                        String[] fileList = getAssets().list("init" + File.separator + folder);
                        for (int j = 0; j < fileList.length; j++) {
                            String target = "";
                            String file = fileList[j];

                            InputStream inputStream = getAssets().open("init" + File.separator + folder + File.separator + file);
                            String folderPath = "";
                            if (i == 0) {
                                folderPath = imageBasePath + Constant.IMAGE_FOLDER_ONE;
                            } else if (i == 1) {
                                folderPath = imageBasePath + Constant.IMAGE_FOLDER_TWO;
                            } else if (i == 2) {
                                folderPath = imageBasePath + Constant.IMAGE_FOLDER_THREE;
                            } else if (i == 3) {
                                folderPath = imageBasePath + Constant.IMAGE_FOLDER_FOUR;
                            } else if (i == 4) {
                                folderPath = imageBasePath + Constant.IMAGE_FOLDER_FIVE;
                            } else if (i == 5) {
                                folderPath = imageBasePath + Constant.IMAGE_FOLDER_SIX;
                            } else {
                                folderPath = imageBasePath + Constant.IMAGE_FOLDER_ONE;
                            }
                            target = folderPath + File.separator + file;
                            File targetFile = new File(target);
                            if (!targetFile.exists()) {
                                FileUtil.copyFile(inputStream, target);
                                Thread.sleep(300);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
