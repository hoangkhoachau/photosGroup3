package com.example.photosGroup3.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.photosGroup3.Callback.tagAdapterCallback;

import java.util.ArrayList;

public class DatabaseManager {
    private DBHelper dbHelper;
    private ArrayList<tagAdapterCallback> callbacks=new ArrayList<>();

    public DatabaseManager(Context context) {
        dbHelper = new DBHelper(context);
    }

    public long insertLabel(String label) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("label", label);

        long rowId = db.insert("labels", null, values);
        db.close();

        callbacks.forEach(tagAdapterCallback::update);
        return rowId;
    }

    public long insertImage(String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("image_path", imagePath);

        long rowId = db.insert("images", null, values);
        db.close();

        return rowId;
    }

    public void associateLabelWithImage(long labelId, long imageId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("label_id", labelId);
        values.put("image_id", imageId);

        db.insert("label_image", null, values);
        db.close();
    }

    public void associateLabelWithImage(String label, String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("label_id", getLabelId(label));
        values.put("image_id", getImageId(imagePath));

        db.insert("label_image", null, values);
        db.close();
    }

    public Cursor getImagesForLabel(long labelId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery("SELECT images.* FROM images " +
                "JOIN label_image ON images.id = label_image.image_id " +
                "WHERE label_image.label_id = ?", new String[]{String.valueOf(labelId)});
    }

    public Cursor getLabelsForImage(long imageId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery("SELECT labels.* FROM labels " +
                "JOIN label_image ON labels.id = label_image.label_id " +
                "WHERE label_image.image_id = ?", new String[]{String.valueOf(imageId)});
    }

    public Cursor getLabelsForImage(String imagePath) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery("SELECT labels.* FROM labels " +
                "JOIN label_image ON labels.id = label_image.label_id " +
                "JOIN images ON images.id = label_image.image_id " +
                "WHERE images.image_path = ?", new String[]{imagePath});
    }

    public Cursor getImagesForLabel(String label) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery("SELECT images.* FROM images " +
                "JOIN label_image ON images.id = label_image.image_id " +
                "JOIN labels ON labels.id = label_image.label_id " +
                "WHERE labels.label = ?", new String[]{label});
    }

    public long getLabelId(String label) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM labels WHERE label = ?", new String[]{label});
        cursor.moveToFirst();
        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public long getImageId(String imagePath) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT id FROM images WHERE image_path = ?", new String[]{imagePath});
        cursor.moveToFirst();
        long id = cursor.getLong(0);
        cursor.close();
        return id;
    }

    public Cursor getAllLabels() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery("SELECT * FROM labels", null);
    }

    public Cursor getAllImages() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.rawQuery("SELECT * FROM images", null);
    }
    // Other methods for updating, deleting, and querying the database
    public void deleteLabel(String label) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("labels", "label = ?", new String[]{label});
        db.close();
        callbacks.forEach(tagAdapterCallback::update);
    }
    public void deleteImage(String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("images", "image_path = ?", new String[]{imagePath});
        db.close();
    }

    public void purgeDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM labels");
        db.execSQL("DELETE FROM images");
        db.execSQL("DELETE FROM label_image");
        db.close();
        callbacks.forEach(tagAdapterCallback::update);
    }

    public void addCallback(tagAdapterCallback callback) {
        callbacks.add(callback);
    }
}

