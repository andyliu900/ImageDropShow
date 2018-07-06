package com.moping.imageshow.base;

import android.app.Application;
import android.os.Environment;

import com.moping.imageshow.util.Constant;

import java.io.File;

public class ImageShowApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initImageFolders();
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
}
