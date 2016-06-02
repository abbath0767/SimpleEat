package com.luxary_team.simpleeat.Utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import com.luxary_team.simpleeat.MainActivity;

import java.io.File;
import java.io.IOException;

public class ThumbnailRotator {
    public static Bitmap rotateThumbnail(Bitmap thumb, File originalfile) {
//        File curFile = new File("path-to-file"); // ... This is an image file from my device.
        Bitmap rotatedBitmap = null;

        try {
            ExifInterface exif = new ExifInterface(originalfile.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
            rotatedBitmap = Bitmap.createBitmap(thumb,0,0, thumb.getWidth(), thumb.getHeight(), matrix, true);

        }catch(IOException ignored){
            Log.d(MainActivity.TAG, "rotate error" + ignored);
        }

        return rotatedBitmap;
    }

    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}
