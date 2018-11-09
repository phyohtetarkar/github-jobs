package com.phyohtet.githubjobs.ui

import android.os.Build
import android.text.Html
import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.phyohtet.githubjobs.R
import com.phyohtet.githubjobs.model.Status
import com.squareup.picasso.Picasso
import java.util.*

class BindingUtil {

    companion object {
        @BindingAdapter("image")
        @JvmStatic
        fun setImage(imageView: ImageView, url: String?) {
            if (url != null) {
                val p = Picasso.get()
                p.load(url).placeholder(R.drawable.loading).into(imageView)
            } else {
                imageView.setImageResource(R.drawable.placeholder)
            }
        }

        @BindingAdapter("html")
        @JvmStatic
        fun setHtmlText(textView: TextView, text: String?) {
            text?.also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textView.text = Html.fromHtml(it.trim { it <= ' ' }, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    textView.text = Html.fromHtml(it.trim { it <= ' ' })
                }
            }
        }

        @BindingAdapter("time")
        @JvmStatic
        fun setTime(textView: TextView, date: Date?) {
            date?.apply {
                textView.text = DateUtils.getRelativeTimeSpanString(time, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS)
            }
        }

        @BindingAdapter("android:visibility")
        @JvmStatic
        fun setVisibility(layout: LinearLayout, status: Status) {
            when (status) {
                Status.LOADING -> layout.visibility = View.VISIBLE
                else -> layout.visibility = View.GONE
            }
        }
    }

}