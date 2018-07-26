package com.phyohtet.githubjobs.ui;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class BindingUtil {

    @BindingAdapter({"image"})
    public static void setImage(ImageView imageView, String url) {
        if (url != null) {
            Glide.with(imageView.getContext()).load(url).into(imageView);
        }
    }

}
