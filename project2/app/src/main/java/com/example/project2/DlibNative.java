package com.example.project2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class DlibNative {
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("openblas");
    }

    public static void draw_face(Bitmap Input){
        int[] rect = face_detection(Input);

        Canvas canvas = new Canvas(Input);
        Paint p = new Paint();

        p.setColor(Color.RED);
        p.setStrokeWidth(10.0f);

        for(int i = 1; i <= rect[0]; i ++){
            int index = (i - 1) * 4 + 1;
            canvas.drawLine(rect[index + 0], rect[index + 1], rect[index + 2], rect[index + 1], p);
            canvas.drawLine(rect[index + 0], rect[index + 1], rect[index + 0], rect[index + 3], p);
            canvas.drawLine(rect[index + 0], rect[index + 3], rect[index + 2], rect[index + 3], p);
            canvas.drawLine(rect[index + 2], rect[index + 1], rect[index + 2], rect[index + 3], p);
        }

    }

    public static  int[] face_detection(Bitmap origin_image){

//        Log.d("hihi", "face_detection height: " + origin_image.getHeight());
//        Log.d("hihi", "face_detection width: " + origin_image.getWidth());

//        float scale = 480.f/ Math.max(origin_image.getHeight(), origin_image.getWidth());


        //resize anh 1280x720 - 240x?
        float scale = 240.f/ Math.max(origin_image.getHeight(), origin_image.getWidth());
        int width = (int)(origin_image.getWidth()*scale);
        int height = (int)(origin_image.getHeight()*scale);
        Bitmap resize_image=Bitmap.createScaledBitmap(origin_image,width,height , false);


        int[] pixels = new int[width * height];

        resize_image.getPixels(pixels, 0, width, 0, 0, width, height);

        int[] rect= detecFace(pixels,width,height);


        int[] result_rect=new int[rect.length];
//        Log.d("test", "size: " + result_rect.length);

        result_rect[0] = rect[0];
        for(int i = 1; i <= result_rect[0]; i ++){
            int index = (i - 1) * 4 + 1;

            result_rect[index + 0] = (int)(rect[index + 0] / scale);
            result_rect[index + 1] = (int)(rect[index + 1] / scale);
            result_rect[index + 2] = (int)(rect[index + 2] / scale);
            result_rect[index + 3] = (int)(rect[index + 3] / scale);

            result_rect[index + 2] = result_rect[index + 2] + result_rect[index + 0];
            result_rect[index + 3] = result_rect[index + 3] + result_rect[index + 1];
        }
        if(result_rect.length > 2)
            Log.d("test", "face_detection hihii: " + result_rect[0] +" " +   result_rect[1]);

        return  result_rect;
    }


    public native static int[] detecFace(int[] data, int witdh, int height);
}
