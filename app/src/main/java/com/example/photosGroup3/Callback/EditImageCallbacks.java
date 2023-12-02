package com.example.photosGroup3.Callback;

import android.graphics.Bitmap;

public interface EditImageCallbacks {
    void TransformVertical();
    void TransformHorizontal();

    void BackFragment();
    Bitmap blurFast(int radius);
    void ConfirmBlur(Bitmap input);
    void BitmapFilterChoose(Bitmap input,String name);

    void recreateOnDarkMode();

}
