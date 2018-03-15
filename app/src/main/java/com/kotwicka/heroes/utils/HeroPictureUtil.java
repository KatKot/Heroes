package com.kotwicka.heroes.utils;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.kotwicka.heroes.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public final class HeroPictureUtil {

    private static final String TAG = HeroPictureUtil.class.getSimpleName();
    private static final String IMAGE_NOT_FOUND = "image_not_available";
    private static final String IMAGE_NOT_FOUND_2 = "4c002e0305708.gif";

    private HeroPictureUtil() {

    }

    public static void loadPicture(final Context context, final ImageView imageView, final String photoPath) {
        if (photoPath.contains(IMAGE_NOT_FOUND) || photoPath.contains(IMAGE_NOT_FOUND_2)) {
            imageView.setImageDrawable(context.getDrawable(R.drawable.blank_avatar));
        } else {
            Picasso.with(context)
                    .load(photoPath)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Loaded hero image");
                        }

                        @Override
                        public void onError() {
                            Log.d(TAG, "Could not load hero image");
                        }
                    });
        }

    }
}
