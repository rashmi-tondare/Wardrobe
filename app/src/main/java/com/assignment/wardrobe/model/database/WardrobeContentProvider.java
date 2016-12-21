package com.assignment.wardrobe.model.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;

public class WardrobeContentProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String UNKNOWN_URI = "Unknown URI: ";
    private static final String SQL_EXCEPTION_INSERTION_FAILED = "Failed to insert row into ";

    private static final int CLOTHES = 100;
    private static final int CLOTHES_BY_TYPE = 101;
    private static final int FAVOURITES = 200;

    private WardrobeDbHelper mOpenHelper;
    public WardrobeContentProvider() {
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = WardrobeDbHelper.getInstance(getContext());
        return true;
    }

    private static UriMatcher buildUriMatcher() {
        // The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(WardrobeContract.CONTENT_AUTHORITY, WardrobeContract.PATH_CLOTHES, CLOTHES);
        matcher.addURI(WardrobeContract.CONTENT_AUTHORITY, WardrobeContract.PATH_CLOTHES + "/#", CLOTHES_BY_TYPE);
        matcher.addURI(WardrobeContract.CONTENT_AUTHORITY, WardrobeContract.PATH_FAVOURITES, FAVOURITES);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        //This is to handle requests for the MIME type of the data at the given URI.
        switch (sUriMatcher.match(uri)) {
            case CLOTHES:
            case CLOTHES_BY_TYPE:
                return WardrobeContract.ClothesEntry.CONTENT_TYPE;
            case FAVOURITES:
                return WardrobeContract.FavouritesEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //This is to handle requests to insert a new row.
        Uri returnUri;

        switch (sUriMatcher.match(uri)) {
            case CLOTHES:
                long clothesId = mOpenHelper.getWritableDatabase()
                        .insert(WardrobeContract.ClothesEntry.TABLE_NAME, null, values);
                if (clothesId > 0) {
                    returnUri = WardrobeContract.ClothesEntry.buildClothesUri(clothesId);
                }
                else
                    throw new SQLException(SQL_EXCEPTION_INSERTION_FAILED + uri);
                break;
            case FAVOURITES:
                long favouriteId = mOpenHelper.getWritableDatabase()
                        .insert(WardrobeContract.FavouritesEntry.TABLE_NAME, null, values);
                if (favouriteId > 0) {
                    returnUri = WardrobeContract.FavouritesEntry.buildFavouriteUri(favouriteId);
                }
                else
                    throw new SQLException(SQL_EXCEPTION_INSERTION_FAILED + uri);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        //This is to handle requests to update one or more rows.
        int noOfRowsUpdated = 0;

        switch (sUriMatcher.match(uri)) {
            case CLOTHES:
                noOfRowsUpdated = mOpenHelper.getWritableDatabase()
                        .update(WardrobeContract.ClothesEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs);
                break;
            case FAVOURITES:
                noOfRowsUpdated = mOpenHelper.getWritableDatabase()
                        .update(WardrobeContract.FavouritesEntry.TABLE_NAME,
                                values,
                                selection,
                                selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        if (noOfRowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return noOfRowsUpdated;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //This is to handle query requests from clients.
        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {
            case CLOTHES:
                retCursor = mOpenHelper.getReadableDatabase()
                        .query(WardrobeContract.ClothesEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            case CLOTHES_BY_TYPE:
                String type = uri.getPathSegments().get(1);
                retCursor = mOpenHelper.getReadableDatabase()
                            .query(WardrobeContract.ClothesEntry.TABLE_NAME,
                                    projection,
                                    WardrobeContract.ClothesEntry.COLUMN_CLOTHING_TYPE + " = ?",
                                    new String[]{type},
                                    null,
                                    null,
                                    sortOrder);
                break;
            case FAVOURITES:
                retCursor = mOpenHelper.getReadableDatabase()
                        .query(WardrobeContract.FavouritesEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //This is to handle requests to delete one or more rows.
        int noOfRowsDeleted = 0;

        //This deletes all rows and returns the no. of rows deleted
        if (selection == null) {
            selection = "1";
        }
        switch (sUriMatcher.match(uri)) {
            case CLOTHES:
                noOfRowsDeleted = mOpenHelper.getWritableDatabase()
                        .delete(WardrobeContract.ClothesEntry.TABLE_NAME,
                                selection,
                                selectionArgs);
                break;
            case FAVOURITES:
                noOfRowsDeleted = mOpenHelper.getWritableDatabase()
                        .delete(WardrobeContract.FavouritesEntry.TABLE_NAME,
                                selection,
                                selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException(UNKNOWN_URI + uri);
        }

        if (noOfRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return noOfRowsDeleted;
    }
}
