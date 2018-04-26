package cn.edu.zwu.repairbao.Util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.edu.zwu.repairbao.BuildConfig;

/**
 * @author Richard_Y_Wang
 * @version $Rev$
 * @des 2018/4/12
 * @updateAuthor $Author$
 * @updateDes ${TODO}
 */
public class PictureUtil {

    /**
     * 创建一个对应的文件夹 如果这个文件夹不存在的话 用于储存图片
     *
     * @param dir
     */
    public static void createDir(String dir) {
        File file = new File(dir);
        //如果文件目录不存在 那么就去创建这个目录
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 查看图片
     */
    public static void seePicture(File file, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.setDataAndType(contentUri, "image/*");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 保存图片 存为jpg格式的
     *
     * @param file   注意！file这个String后面要自带.jpg
     * @param bitmap
     */
    public static void savePicture(File file, Bitmap bitmap) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片以后发送广播来通知系统刷新图片列表
     *
     * @param file
     */
    public static void flushGallery(File file, Context context) {
        Uri uri = Uri.fromFile(file);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
    }
}
