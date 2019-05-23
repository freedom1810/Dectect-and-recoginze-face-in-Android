package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    JavaCameraView javaCameraView;
    Mat inputMat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        OpenCVLoader.initDebug();

        javaCameraView = (JavaCameraView)findViewById(R.id.cameraView);
        javaCameraView.setCameraIndex(1);

        javaCameraView.setCvCameraViewListener(this);
        javaCameraView.enableView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        javaCameraView.disableView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        javaCameraView.enableView();
    }

    @Override
    protected void onDestroy() {
        javaCameraView.disableView();
        super.onDestroy();
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public void onCameraViewStarted(int width, int height)
    {
    }


    //not use opencv C++
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        inputMat = inputFrame.rgba();

        Bitmap temp = Bitmap.createBitmap(inputMat.cols(), inputMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(inputMat, temp);


        // xu ly anh

        DlibNative.draw_face(temp);

        //    mFace.detect(temp);

        Utils.bitmapToMat(temp, inputMat);

        return inputMat;
    }

}


