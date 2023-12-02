package com.example.photosGroup3.Callback;

import android.graphics.Bitmap;

public interface ZoomCallBack {
    void BackToInit();
    Bitmap RotateDegree(String currentImg,float degree,int pos);
    void setImageView(String currentImg,int pos);


}
