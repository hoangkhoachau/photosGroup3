package com.example.photosGroup3.Callback;

import java.util.ArrayList;

public interface MainCallBack {
    void setCurrentDirectory(String Dir);

    String getSDDirectory();
    String getCurrentDirectory();
    void pushFolderPath (String inp );
    void popFolderPath();
    ArrayList<String> getFolderPath();
    String getDCIMDirectory();
    String getPictureDirectory();

    ArrayList<String> getFileinDir();
    void removeImageUpdate(String[] input);

    void removeImageUpdate(String input);

    void renameImageUpdate(String oldNam, String newName);
    void Holding(boolean isHolding);
    void SelectedTextChange();
    ArrayList<String> chooseToDeleteInList();
    ArrayList<String> adjustChooseToDeleteInList(String ListInp,String type );
    void clearChooseToDeleteInList();
    void addImageUpdate(String[] input);

    void shareImages(ArrayList<String> paths);
    boolean getIsDark();
    void setIsDark(boolean status);

}

