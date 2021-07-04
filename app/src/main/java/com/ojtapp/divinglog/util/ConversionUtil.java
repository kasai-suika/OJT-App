package com.ojtapp.divinglog.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import androidx.annotation.NonNull;

import java.io.FileDescriptor;
import java.io.IOException;

public class ConversionUtil {
    /**
     * データがない場合の定数
     */
    public static final int NO_DATA = -1;

    ConversionUtil() {
    }

    /**
     * Uri型をBitmap型の画像に変換する。
     *
     * @param uri     Bitmap型に変換するUri
     * @param context コンテクスト
     * @return Uriから変換したBitmap型の画像
     * @throws IOException 例外
     */
    public static Bitmap getBitmapFromUri(@NonNull Uri uri, @NonNull Context context) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    /**
     * String型をint型に変換する。
     * データがない場合は-1を返す。
     *
     * @param str 　String型のデータ
     * @return int型に変換したデータ
     */
    public static int getIntFromStr(@NonNull String str) {
        if (0 == str.length()) {
            return NO_DATA;
        } else {
            return Integer.parseInt(str);
        }
    }

    /**
     * int型をString型に変換する。
     * データがない場合は空文字を返す。
     *
     * @param intData 　int型のデータ
     * @return String型に変換したデータ
     */
    public static String getStrFromInt(int intData) {
        if (ConversionUtil.NO_DATA == intData) {
            return "";
        } else {
            return String.valueOf(intData);
        }
    }
}
