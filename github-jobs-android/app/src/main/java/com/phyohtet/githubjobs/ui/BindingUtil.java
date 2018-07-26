package com.phyohtet.githubjobs.ui;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.phyohtet.githubjobs.R;
import com.squareup.picasso.Picasso;

public class BindingUtil {

    @BindingAdapter({"image"})
    public static void setImage(ImageView imageView, String url) {
        if (url != null) {
            Picasso.get().cancelRequest(imageView);
            Picasso.get().load(url).placeholder(R.drawable.loading).into(imageView);
        }
    }

}
