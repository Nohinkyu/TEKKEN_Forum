package com.nik.tkforum.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import com.nik.tkforum.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url != null) {
        view.load(url)
    } else {
        view.setImageResource(R.drawable.ic_image_not_supported)
    }
}