package com.example.photosGroup3;

import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.example.photosGroup3.Callback.updateCallBack;
import com.example.photosGroup3.Utils.DatabaseManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

public class ImageLabelWrapper {
    ImageLabeler labeler;
    DatabaseManager dbManager;
    private static ImageLabelWrapper INSTANCE = null;
    private float confidence=0.7f;

    public ImageLabelWrapper() {
        ImageLabelerOptions options =
                new ImageLabelerOptions.Builder()
                        .setConfidenceThreshold(confidence)
                        .build();
        labeler = ImageLabeling.getClient(options);
        dbManager = new DatabaseManager(MainActivity.mainActivity);
    }
    public static ImageLabelWrapper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageLabelWrapper();
        }
        // put args
        return INSTANCE;
    }

    public ArrayList<String> getLabels(String path, updateCallBack callback) {
        Cursor cursor = dbManager.getLabelsForImage(path);
        if (cursor!=null && cursor.getCount() > 0) {
                    ArrayList<String> labels = new ArrayList<>();
                    while (cursor.moveToNext()) {
                        labels.add(cursor.getString(1));
                    }
                    if (callback!=null)
                        callback.onSuccessLabeling(labels);
                    return labels;
                }
        ArrayList<String> labels = new ArrayList<>();
        try {
            labeler.process(InputImage.fromFilePath(MainActivity.mainActivity, Uri.fromFile(new File(path))))
                    .addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                        @Override
                        public void onSuccess(List<ImageLabel> imageLabels) {
                            for (ImageLabel label : imageLabels) {
                                String text = label.getText();
                                float confidence = label.getConfidence();
                                int index = label.getIndex();
                                labels.add(text);
                            }
                            dbManager.insertImage(path);
                            for (String label : labels) {
                                dbManager.insertLabel(label);
                                dbManager.associateLabelWithImage(dbManager.getLabelId(label), dbManager.getImageId(path));
                            }
                            if (callback!=null)
                                callback.onSuccessLabeling(labels);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            if (callback!=null)
                                callback.onSuccessLabeling(labels);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return labels;
    }

}
