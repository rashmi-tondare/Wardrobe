package com.assignment.wardrobe.view.adapters;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.assignment.wardrobe.controller.ClothesController;
import com.assignment.wardrobe.utils.AppConstants;
import com.assignment.wardrobe.view.fragments.ImageFragment;

/**
 * Created by Rashmi on 20/12/16.
 */

public class ClothesCursorPagerAdapter extends FragmentStatePagerAdapter {

    private Cursor cursor;

    public ClothesCursorPagerAdapter(FragmentManager fm, Cursor cursor) {
        super(fm);
        this.cursor = cursor;
    }

    @Override
    public Fragment getItem(int position) {
        cursor.moveToPosition(position);
        ImageFragment imageFragment = ImageFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.EXTRAS_CLOTHING, ClothesController.convertFromCursorToObject(cursor));
        imageFragment.setArguments(bundle);

        return imageFragment;
    }

    @Override
    public int getCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor c) {
        if (cursor == c)
            return;

        this.cursor = c;
        notifyDataSetChanged();
    }

    public Cursor getCursor() {
        return cursor;
    }
}
