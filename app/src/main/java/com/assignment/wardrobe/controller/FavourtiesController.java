package com.assignment.wardrobe.controller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.assignment.wardrobe.model.Favourite;
import com.assignment.wardrobe.model.database.WardrobeContract;

/**
 * Created by Rashmi on 20/12/16.
 */

public class FavourtiesController {

    private static final String TAG = FavourtiesController.class.getSimpleName();

    public static long storeFavourite(Context context, Favourite favourite) {
        if (favourite != null) {
            ContentResolver contentResolver = context.getContentResolver();

            ContentValues contentValues = new ContentValues();
            contentValues.put(WardrobeContract.FavouritesEntry.COLUMN_TOP_ID, favourite.getTopId());
            contentValues.put(WardrobeContract.FavouritesEntry.COLUMN_BOTTOM_ID, favourite.getBottomId());

            long id = ContentUris.parseId(contentResolver.insert(WardrobeContract.FavouritesEntry.CONTENT_URI, contentValues));
            return id;
        } else {
            Log.e(TAG, "cannot store null object");
        }
        return 0;
    }

    public static boolean deleteFavourite(Context context, long id) {
        ContentResolver contentResolver = context.getContentResolver();

        return contentResolver.delete(WardrobeContract.FavouritesEntry.CONTENT_URI,
                WardrobeContract.FavouritesEntry._ID + " = ?",
                new String[]{String.valueOf(id)}) > 0;

    }

    public static boolean deleteFavourite(Context context, long topId, long bottomId) {
        ContentResolver contentResolver = context.getContentResolver();

        return contentResolver.delete(WardrobeContract.FavouritesEntry.CONTENT_URI,
                WardrobeContract.FavouritesEntry.COLUMN_TOP_ID + " = ? AND " +
                        WardrobeContract.FavouritesEntry.COLUMN_BOTTOM_ID + " = ?",
                new String[]{String.valueOf(topId), String.valueOf(bottomId)}) > 0;
    }

    public static boolean isFavourite(Context context, long topId, long bottomId) {
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(WardrobeContract.FavouritesEntry.CONTENT_URI,
                null,
                WardrobeContract.FavouritesEntry.COLUMN_TOP_ID + " = ? AND " +
                        WardrobeContract.FavouritesEntry.COLUMN_BOTTOM_ID + " = ?",
                new String[]{String.valueOf(topId), String.valueOf(bottomId)},
                null);
        if (cursor != null) {
            int count = cursor.getCount();
            cursor.close();

            return count > 0;
        }
        return false;
    }
}
