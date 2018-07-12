package com.moping.imageshow.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 文件处理工具类
 */
public class FileUtil {

    public static String getRealPathFromUri(Context context, Uri uri) {
        return getRealPathFromUri_BelowApi11(context, uri);
//        int sdkVersion = Build.VERSION.SDK_INT;
//        if (sdkVersion < 11) {
//            return getRealPathFromUri_BelowApi11(context, uri);
//        } else if (sdkVersion < 19) {
//            return getRealPathFromUri_Api11To18(context, uri);
//        } else {
//            return getRealPathFromUri_AboveApi19(context, uri);
//        }
    }

    /**
     * 适配api19以上,根据uri获取图片的绝对路径
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    /**
     * 适配api11-api18,根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_Api11To18(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();

        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 适配api11以下(不包括api11),根据uri获取图片的绝对路径
     */
    private static String getRealPathFromUri_BelowApi11(Context context, Uri uri) {
        String filePath = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            filePath = cursor.getString(cursor.getColumnIndex(projection[0]));
            cursor.close();
        }
        return filePath;
    }

    /**
     * 文件复制方法
     *
     * @param source
     * @param target
     */
    public static void copyFile(String source, String target) {
        FileChannel input = null;
        FileChannel output = null;

        try {
            input = new FileInputStream(source).getChannel();
            output = new FileOutputStream(target).getChannel();
            /**
             * Transfers bytes into this channel's file from the given readable byte channel.
             *  @param  src
             *         The source channel
             *
             * @param  position
             *         The position within the file at which the transfer is to begin;
             *         must be non-negative
             *
             * @param  count
             *         The maximum number of bytes to be transferred; must be
             *         non-negative
             */
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
