package com.ccnu.nrcci.hbnmhmap.HttpService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 二进制数组转换为特定对象
 * Created by xinjay on 2017/9/25.
 */

public class BytesReaderUtil {

    /**
     * @param bytes 欲转换的二进制数组
     * @param t     欲转换成的类别
     * @return 返回Object对象，在外部强制转换成特定对象
     */
    public Object getObject(byte[] bytes, Class t) {
        if (t.equals(String.class)) {
            return getString(bytes);
        } else if (t.equals(Bitmap.class)) {
            return getBitmap(bytes);
        } else
            return bytes;
    }


    /**
     * 将二进制数组转换为字符串
     *
     * @param bytes 欲转换的二进制数组
     * @return
     */
    private String getString(byte[] bytes) {
        return new String(bytes);
    }

    /**
     * 将二进制数组转换为Bitmap对象
     *
     * @param bytes 欲转换的二进制数组
     * @return
     */
    private Bitmap getBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
