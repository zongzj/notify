package com.zong.call.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import com.zong.common.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;

public class IconUtil {
    private IconUtil() {
        // 不允许实例化这个对象
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 将手机上所有的图标存放到saveDir目录下.<br/>
     * 使用时需要在Manifest中声明两个权限:<br/>
     * android.permission.WRITE_EXTERNAL_STORAGE.<br/>
     * <p>
     * android.permission.MOUNT_UNMOUNT_FILESYSTEMS.<br/>
     *
     * @param context
     * @param saveDir 存放的手机文件夹目录，如new File("/sdcard/Pictures/");
     * @return i表示图标数量，i=0表示未获取到图标
     */
    public static void saveIcon(Drawable appIcon, String externalStorageDirectory, String iconPath) {
    LogUtils.d("IconUtil","externalStorageDirectory:"+externalStorageDirectory);
        // 将drawable转成Bitmap对象
        Bitmap bm = drawableToBitmap(appIcon);
        File file = new File(externalStorageDirectory);
        file.mkdirs();

        // 指定存储路径以及存储文件名格式
        File outputImg = new File(externalStorageDirectory,iconPath+".png");

        // Bitmap存储过程
        try {
            FileOutputStream out = new FileOutputStream(outputImg);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 将Drawable转换成Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(

                drawable.getIntrinsicWidth(),

                drawable.getIntrinsicHeight(),

                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888

                        : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        // canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }
}