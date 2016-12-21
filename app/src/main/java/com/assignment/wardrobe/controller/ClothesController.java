package com.assignment.wardrobe.controller;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.assignment.wardrobe.model.Clothing;
import com.assignment.wardrobe.model.database.WardrobeContract;

/**
 * Created by Rashmi on 20/12/16.
 */

public class ClothesController {

    private static final String TAG = ClothesController.class.getSimpleName();

    public static void storeClothing(Context context, Clothing clothing) {
        if (clothing != null) {
            ContentResolver contentResolver = context.getContentResolver();

            ContentValues contentValues = new ContentValues();
            contentValues.put(WardrobeContract.ClothesEntry.COLUMN_CLOTHING_TYPE, clothing.getClothingType());
            contentValues.put(WardrobeContract.ClothesEntry.COLUMN_FILE_PATH, clothing.getFilePath());

            contentResolver.insert(WardrobeContract.ClothesEntry.CONTENT_URI, contentValues);
        }
        else {
            Log.e(TAG, "cannot store null object");
        }
    }

    public static Clothing convertFromCursorToObject(Cursor cursor) {
        return new Clothing(cursor.getInt(cursor.getColumnIndex(WardrobeContract.ClothesEntry._ID)),
                cursor.getInt(cursor.getColumnIndex(WardrobeContract.ClothesEntry.COLUMN_CLOTHING_TYPE)),
                cursor.getString(cursor.getColumnIndex(WardrobeContract.ClothesEntry.COLUMN_FILE_PATH)));
    }
}
