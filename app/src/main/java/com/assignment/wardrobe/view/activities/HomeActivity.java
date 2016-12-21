package com.assignment.wardrobe.view.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.assignment.wardrobe.R;
import com.assignment.wardrobe.controller.ClothesController;
import com.assignment.wardrobe.controller.FavourtiesController;
import com.assignment.wardrobe.model.Clothing;
import com.assignment.wardrobe.model.Favourite;
import com.assignment.wardrobe.model.database.WardrobeContract;
import com.assignment.wardrobe.utils.AppConstants;
import com.assignment.wardrobe.utils.FileUtils;
import com.assignment.wardrobe.utils.NotificationHelper;
import com.assignment.wardrobe.view.adapters.ClothesCursorPagerAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

@EActivity(R.layout.activity_home)
@WindowFeature(Window.FEATURE_NO_TITLE)
public class HomeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    @ViewById(R.id.view_pager_top)
    protected ViewPager viewPagerTop;

    @ViewById(R.id.view_pager_bottom)
    protected ViewPager viewPagerBottom;

    @ViewById(R.id.activity_home)
    protected CoordinatorLayout coordinatorLayout;

    @ViewById(R.id.btn_favourite)
    protected FloatingActionButton fabFavourite;

    private ClothesCursorPagerAdapter topsPagerAdapter, bottomsPagerAdapter;

    private AlertDialog pictureSelectionAlert;
    private int clothingType;
    private String photoFilePath;
    private boolean newTop, newBottom;
    private int topPositionOnConfigChange = -1, bottomPositionOnConfigChange = -1;

    private static final String TAG = HomeActivity.class.getSimpleName();
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICKER = 2;
    private static final int LOADER_TOPS = 1;
    private static final int LOADER_BOTTOMS = 2;

    @AfterViews
    public void initActivity() {
        getLoaderManager().initLoader(LOADER_TOPS, null, this);
        getLoaderManager().initLoader(LOADER_BOTTOMS, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        NotificationHelper.getInstance().clearAllNotifications();
    }

    @Click(R.id.btn_add_top)
    public void onAddTopClick() {
        clothingType = AppConstants.CLOTHING_TYPE_TOP;
        displayPictureSelectionOptions(R.string.dialog_title_add_top).show();
    }

    @Click(R.id.btn_add_bottom)
    public void onAddBottomClick() {
        clothingType = AppConstants.CLOTHING_TYPE_BOTTOM;
        displayPictureSelectionOptions(R.string.dialog_title_add_bottom).show();
    }

    @Click(R.id.btn_favourite)
    public void onFavouriteClick() {
        if (topsPagerAdapter.getCount() > 0 && bottomsPagerAdapter.getCount() > 0) {
            Cursor cursor = topsPagerAdapter.getCursor();
            cursor.moveToPosition(viewPagerTop.getCurrentItem());
            int topId = cursor.getInt(cursor.getColumnIndex(WardrobeContract.ClothesEntry._ID));

            cursor = bottomsPagerAdapter.getCursor();
            cursor.moveToPosition(viewPagerBottom.getCurrentItem());
            int bottomId = cursor.getInt(cursor.getColumnIndex(WardrobeContract.ClothesEntry._ID));

            final Favourite favourite = new Favourite(topId, bottomId);
            if (FavourtiesController.isFavourite(this, topId, bottomId)) {
                if (FavourtiesController.deleteFavourite(this, topId, bottomId)) {
                    fabFavourite.setImageResource(R.drawable.ic_mark_favourite);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.snackbar_favourite_removed, Snackbar.LENGTH_LONG)
                            .setAction(R.string.snackbar_option_undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (FavourtiesController.storeFavourite(HomeActivity.this, favourite) > 0) {
                                        fabFavourite.setImageResource(R.drawable.ic_favorite);
                                        Snackbar.make(coordinatorLayout, R.string.snackbar_favourite_saved, Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                    snackbar.show();
                }
            } else {

                final long favouriteId = FavourtiesController.storeFavourite(this, favourite);
                if (favouriteId > 0) {
                    fabFavourite.setImageResource(R.drawable.ic_favorite);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.snackbar_favourite_saved, Snackbar.LENGTH_LONG)
                            .setAction(R.string.snackbar_option_undo, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    undoFavourite(favouriteId);
                                }
                            });
                    snackbar.show();
                } else {
                    Snackbar.make(coordinatorLayout, R.string.snackbar_try_again, Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        } else {
            Snackbar.make(coordinatorLayout, R.string.snackbar_add_clothes, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    @Click(R.id.btn_shuffle)
    public void onShuffleClick() {
        if (topsPagerAdapter.getCount() > 0 && bottomsPagerAdapter.getCount() > 0) {
            Random random = new Random();
            int position = 0;
            if (topsPagerAdapter.getCount() > 1) {
                do {
                    position = random.nextInt(topsPagerAdapter.getCount());
                } while (position == viewPagerTop.getCurrentItem());
                viewPagerTop.setCurrentItem(position);
            }

            if (bottomsPagerAdapter.getCount() > 1) {
                do {
                    position = random.nextInt(bottomsPagerAdapter.getCount());
                } while (position == viewPagerBottom.getCurrentItem());
                viewPagerBottom.setCurrentItem(position);
            }
        } else {
            Snackbar.make(coordinatorLayout, R.string.snackbar_add_clothes, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void undoFavourite(long id) {
        boolean deletedFavourite = FavourtiesController.deleteFavourite(this, id);
        if (deletedFavourite) {
            fabFavourite.setImageResource(R.drawable.ic_mark_favourite);
            Snackbar.make(coordinatorLayout, R.string.snackbar_favourite_removed, Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private AlertDialog displayPictureSelectionOptions(int title) {
        if (pictureSelectionAlert == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(title)
                    .setItems(R.array.options_select_picture, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                // selected camera option
                                case 0:
                                    dispatchTakePictureIntent();
                                    break;
                                // selected gallery option
                                case 1:
                                    dispatchImagePickerIntent();
                                    break;
                            }
                        }
                    });
            return builder.create();
        }
        return pictureSelectionAlert;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = FileUtils.createImageFile(clothingType);

            } catch (IOException ex) {
                Log.e(TAG, ex.getMessage(), ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                // photoFilePath = Uri.fromFile(photoFile).toString();
                photoFilePath = photoFile.getAbsolutePath();
                Log.d(TAG, photoFilePath);
                Uri photoURI = FileProvider.getUriForFile(this,
                        AppConstants.FILE_PROVIDER,
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void dispatchImagePickerIntent() {
        Intent intent = new Intent();
        // Show only images
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    storeClothingInDb();
                } else {
                    FileUtils.deleteFile(photoFilePath);
                }
                break;
            case REQUEST_IMAGE_PICKER:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    if (selectedImage.toString().startsWith(AppConstants.GOOGLE_PHOTOS_CONTENT)) {
                        try {
                            InputStream in = getContentResolver().openInputStream(selectedImage);
                            File photoFile = FileUtils.createImageFile(clothingType);
                            OutputStream out = new FileOutputStream(photoFile);
                            if (in != null) {
                                // Transfer bytes from in to out
                                byte[] buf = new byte[1024];
                                int len;
                                while ((len = in.read(buf)) > 0) {
                                    out.write(buf, 0, len);
                                }
                                in.close();
                                out.close();

                                photoFilePath = photoFile.getAbsolutePath();
                                storeClothingInDb();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        if (cursor != null) {
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            photoFilePath = cursor.getString(columnIndex);
                            cursor.close();
                            storeClothingInDb();
                        }
                    }
                }
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;

        }
    }

    private void storeClothingInDb() {
        newTop = (clothingType == AppConstants.CLOTHING_TYPE_TOP);
        newBottom = (clothingType == AppConstants.CLOTHING_TYPE_BOTTOM);

        Clothing clothing = new Clothing(clothingType, photoFilePath);
        ClothesController.storeClothing(this, clothing);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        switch (id) {
            case LOADER_TOPS:
                return new CursorLoader(this,
                        WardrobeContract.ClothesEntry.buildClothesByTypeUri(AppConstants.CLOTHING_TYPE_TOP),
                        null,
                        null,
                        null,
                        null);
            case LOADER_BOTTOMS:
                return new CursorLoader(this,
                        WardrobeContract.ClothesEntry.buildClothesByTypeUri(AppConstants.CLOTHING_TYPE_BOTTOM),
                        null,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        switch (loader.getId()) {
            case LOADER_TOPS:
                if (topsPagerAdapter == null) {
                    topsPagerAdapter = new ClothesCursorPagerAdapter(getSupportFragmentManager(), cursor);
                    viewPagerTop.setAdapter(topsPagerAdapter);
                    viewPagerTop.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            checkIfFavourite(AppConstants.CLOTHING_TYPE_TOP, position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                } else {
                    topsPagerAdapter.swapCursor(cursor);
                }

                if (newTop)
                    viewPagerTop.setCurrentItem(topsPagerAdapter.getCount() - 1);
                else if (topPositionOnConfigChange > -1)
                    viewPagerTop.setCurrentItem(topPositionOnConfigChange);

                newTop = false;
                topPositionOnConfigChange = -1;
                break;
            case LOADER_BOTTOMS:
                if (bottomsPagerAdapter == null) {
                    bottomsPagerAdapter = new ClothesCursorPagerAdapter(getSupportFragmentManager(), cursor);
                    viewPagerBottom.setAdapter(bottomsPagerAdapter);
                    viewPagerBottom.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            checkIfFavourite(AppConstants.CLOTHING_TYPE_BOTTOM, position);
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    checkIfFavourite(AppConstants.CLOTHING_TYPE_BOTTOM, 0);
                } else {
                    bottomsPagerAdapter.swapCursor(cursor);
                }

                if (newBottom)
                    viewPagerBottom.setCurrentItem(bottomsPagerAdapter.getCount() - 1);
                else if (bottomPositionOnConfigChange > -1)
                    viewPagerBottom.setCurrentItem(bottomPositionOnConfigChange);

                newBottom = false;
                bottomPositionOnConfigChange = -1;
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId()) {
            case LOADER_TOPS:
                topsPagerAdapter.swapCursor(null);
                break;
            case LOADER_BOTTOMS:
                bottomsPagerAdapter.swapCursor(null);
                break;
            default:
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);

//        topPositionOnConfigChange = viewPagerTop.getCurrentItem();
//        bottomPositionOnConfigChange = viewPagerBottom.getCurrentItem();
    }

    private void checkIfFavourite(int clothingType, int position) {
        if (topsPagerAdapter != null && bottomsPagerAdapter != null) {
            long topId = -1, bottomId = -1;
            Cursor cursor = topsPagerAdapter.getCursor();
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToPosition(clothingType == AppConstants.CLOTHING_TYPE_TOP ? position : viewPagerTop.getCurrentItem());
                topId = cursor.getLong(cursor.getColumnIndex(WardrobeContract.ClothesEntry._ID));
            }

            cursor = bottomsPagerAdapter.getCursor();
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToPosition(clothingType == AppConstants.CLOTHING_TYPE_BOTTOM ? position : viewPagerBottom.getCurrentItem());
                bottomId = cursor.getLong(cursor.getColumnIndex(WardrobeContract.ClothesEntry._ID));
            }

            if (topId > -1 && bottomId > -1) {
                fabFavourite.setImageResource(FavourtiesController.isFavourite(this, topId, bottomId) ?
                        R.drawable.ic_favorite :
                        R.drawable.ic_mark_favourite);
            }
        }
    }
}
