package com.example.photosGroup3.Callback;

public interface ISelectedPicture {
    void preventSwipe();
    void allowSwipe();

    void setCurrentSelectedName(String name);
    void setCurrentPosition(int pos);

    void removeImageUpdate(String input);

    void showNav();
    void hiddenNav();

    void notifyChanged();


}
