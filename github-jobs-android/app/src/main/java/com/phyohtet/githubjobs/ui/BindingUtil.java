package com.phyohtet.githubjobs.ui;

import android.databinding.BindingAdapter;
import android.text.Html;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.phyohtet.githubjobs.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;

public class BindingUtil {

    @BindingAdapter({"image"})
    public static void setImage(ImageView imageView, String url) {
        if (url != null) {
            Picasso p = Picasso.get();
            p.load(url).placeholder(R.drawable.loading).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder);
        }
    }

    @BindingAdapter({"html"})
    public static void setHtmlText(TextView textView, String text) {
        if (text != null) {
            textView.setText(Html.fromHtml(text.trim()));
        }
    }

    @BindingAdapter({"time"})
    public static void setTime(TextView textView, Date date) {
        if (date != null) {
            textView.setText(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS));
        }
    }

}
