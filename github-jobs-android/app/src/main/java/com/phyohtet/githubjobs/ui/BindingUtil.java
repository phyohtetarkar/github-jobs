package com.phyohtet.githubjobs.ui;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.phyohtet.githubjobs.R;
import com.squareup.picasso.Picasso;

public class BindingUtil {

    @BindingAdapter({"image"})
    public static void setImage(ImageView imageView, String url) {
        if (url != null) {
            Picasso p = Picasso.get();
            p.cancelRequest(imageView);
            p.load(url).placeholder(R.drawable.loading).into(imageView);
        }
    }

}
