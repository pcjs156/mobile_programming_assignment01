package com.example.assignment01.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ImageUtil {
    static public Bitmap compressBitmap(File f) {
        return compressBitmap(f, 600);
    }

    static public Bitmap compressBitmap(File f, int required_size) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            int scale = 1;
            while (o.outWidth / scale / 2 >= required_size &&
                    o.outHeight / scale / 2 >= required_size) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
