package com.phyohtet.githubjobs.ui;

import android.databinding.BindingAdapter;
import android.os.Build;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.phyohtet.githubjobs.R;
import com.squareup.picasso.Picasso;

public class BindingUtil {

    @BindingAdapter({"image"})
    public static void setImage(ImageView imageView, String url) {
        if (url != null) {
            Picasso p = Picasso.get();
            p.load(url).placeholder(R.drawable.loading).into(imageView);
        }
    }

    @BindingAdapter({"html"})
    public static void setHtmlText(TextView textView, String text) {
        if (text != null) {
            textView.setText(Html.fromHtml(text));
        }
    }

}
