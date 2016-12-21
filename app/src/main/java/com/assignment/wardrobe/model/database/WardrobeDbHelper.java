package com.assignment.wardrobe.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Rashmi on 20/12/16.
 */

public class WardrobeDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "wardrobe.sqlite";
    private static WardrobeDbHelper mOpenHelper;

    private final String SQL_CREATE_TABLE_CLOTHES = "CREATE TABLE " + WardrobeContract.ClothesEntry.TABLE_NAME + "(" +
            WardrobeContract.ClothesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            WardrobeContract.ClothesEntry.COLUMN_CLOTHING_TYPE + " INTEGER," +
            WardrobeContract.ClothesEntry.COLUMN_FILE_PATH + " TEXT" +
            ")";
    //TODO: add foreign key constraints
    private final String SQL_CREATE_TABLE_FAVOURITES = "CREATE TABLE " + WardrobeContract.FavouritesEntry.TABLE_NAME + "(" +
            WardrobeContract.ClothesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            WardrobeContract.FavouritesEntry.COLUMN_TOP_ID + " INTEGER," +
            WardrobeContract.FavouritesEntry.COLUMN_BOTTOM_ID + " INTEGER," +
            "FOREIGN KEY (" + WardrobeContract.FavouritesEntry.COLUMN_TOP_ID + ")" +
            " REFERENCES " + WardrobeContract.ClothesEntry.TABLE_NAME + "(" + WardrobeContract.ClothesEntry._ID + ")," +
            "FOREIGN KEY (" + WardrobeContract.FavouritesEntry.COLUMN_BOTTOM_ID + ")" +
            " REFERENCES " + WardrobeContract.ClothesEntry.TABLE_NAME + "(" + WardrobeContract.ClothesEntry._ID + ")" +
            ")";

    public WardrobeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CLOTHES);
        db.execSQL(SQL_CREATE_TABLE_FAVOURITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static WardrobeDbHelper getInstance(Context context) {
        if (mOpenHelper == null) {
            mOpenHelper = new WardrobeDbHelper(context);
        }
        return mOpenHelper;
    }
}
