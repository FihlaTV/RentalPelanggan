package com.example.meita.rentalpelanggan.Base;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.meita.rentalpelanggan.R;

/**
 * Created by buivu on 10/10/2016.
 */
public class ImageLoader {
    private static final String TAG = "ImageLoader";
    private static ImageLoader instance;

    private ImageLoader() {

    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            instance = new ImageLoader();
        }
        return instance;
    }

    public void loadImageAvatar(final Activity activity, final String url, final ImageView imageView) {

        try {
            Glide.with(activity)
                    .load(url)
                    .error(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(imageView);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }


    }

    public void loadImageOther(final Context context, final String url, final ImageView imageView) {

        try {
            Glide.with(context)
                    .load(url)
                    .error(R.drawable.no_image)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.no_image)
                    .centerCrop()
                    .into(imageView);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
    }

//    public void loadImageSlide(final Fragment fragment, final Drawable url, final ImageView imageView) {
//
//        try {
//            Glide.with(fragment)
//                    .load(url)
//                    .error(R.drawable.no_image)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(draw)
//                    .centerCrop()
//                    .into(imageView);
//        } catch (Exception e) {
//            Log.d(TAG, e.getMessage());
//        }
//    }

//    public void loadImageChat(final Activity activity, final String url, final ImageView imageView) {
//
//        try {
//            Glide.with(activity)
//                    .load(url)
//                    .error(R.drawable.no_image)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .skipMemoryCache(false)
//                    .placeholder(R.drawable.no_image)
//                    .centerCrop()
//                    .into(imageView);
////            Picasso.with(activity)
////                    .load(url)
////                    .error(R.drawable.no_image)
////                    .placeholder(R.drawable.no_image)
////                   .into(imageView, new Callback() {
////                       @Override
////                       public void onSuccess() {
////
////                       }
////
////                       @Override
////                       public void onError() {
////
////                       }
////                   });
//        } catch (Exception e) {
//            Log.d(TAG, e.getMessage());
//        }
//    }
}
