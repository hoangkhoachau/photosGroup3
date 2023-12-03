package com.example.photosGroup3;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class SelectedViewPagerItem {
    String selectedName;
    Bitmap itemBitmap = null;
    public SelectedViewPagerItem(String selectedName) {
        this.selectedName = selectedName;

    }
    public String getSelectedName() {
        return selectedName;
    }
    public Bitmap getItemBitmap() {

        if (itemBitmap == null) {
            File imgFile = new File(selectedName);
            itemBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return itemBitmap;
    }
}
