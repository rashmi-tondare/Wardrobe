package com.assignment.wardrobe.view.fragments;


import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;

import com.assignment.wardrobe.R;
import com.assignment.wardrobe.model.Clothing;
import com.assignment.wardrobe.utils.AppConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_image)
public class ImageFragment extends Fragment {

    private static final String TAG = ImageFragment.class.getSimpleName();

    @ViewById(R.id.img_clothes)
    protected ImageView imgClothes;

    private PhotoViewAttacher photoViewAttacher;

    public ImageFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ImageFragment newInstance() {
        ImageFragment fragment = ImageFragment_.builder().build();
        return fragment;
    }

    @AfterViews
    public void initFragment() {
        Clothing clothing = getArguments().getParcelable(AppConstants.EXTRAS_CLOTHING);
        Log.d(TAG, "initFragment: " + clothing.getFilePath());
        Glide.with(this)
                .load(new File(clothing.getFilePath()))
                .fitCenter()
                .listener(new RequestListener<File, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, File model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, File model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        photoViewAttacher = new PhotoViewAttacher(imgClothes);
                        return false;
                    }
                })
                .placeholder(R.drawable.ic_placeholder)
                .into(imgClothes);
    }

}
