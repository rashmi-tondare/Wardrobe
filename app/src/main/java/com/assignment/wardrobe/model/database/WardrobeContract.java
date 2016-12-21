package com.assignment.wardrobe.model.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Rashmi on 20/12/16.
 */

public class WardrobeContract {
    //this is the name for the content provider
    public static final String CONTENT_AUTHORITY = "com.assignment.wardrobe";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //this is the path used by the content provider to access the tables
    public static final String PATH_CLOTHES = "clothes";
    public static final String PATH_FAVOURITES = "favourites";

    public static final class ClothesEntry implements BaseColumns {
        public static final String TABLE_NAME = "clothes";

        public static final String COLUMN_FILE_PATH = "file_path";
        public static final String COLUMN_CLOTHING_TYPE = "type";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CLOTHES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLOTHES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLOTHES;

        public static Uri buildClothesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildClothesByTypeUri(int type) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(type)).build();
        }
    }

    public static final class FavouritesEntry implements BaseColumns {
        public static final String TABLE_NAME = "favourites";

        public static final String COLUMN_TOP_ID = "top_id";
        public static final String COLUMN_BOTTOM_ID = "bottom_id";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVOURITES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLOTHES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CLOTHES;

        public static Uri buildFavouriteUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
