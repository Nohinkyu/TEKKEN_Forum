package com.nik.tkforum.ui.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.nik.tkforum.R

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (url != null) {
        view.load(url)
    } else {
        view.setImageResource(R.drawable.ic_image_not_supported)
    }
}

@BindingAdapter("imageUrlCircle")
fun loadCircleImage(view: ImageView, url: String?) {
    if (url != null) {
        view.load(url){
            transformations(CircleCropTransformation())
        }
    } else {
        view.setImageResource(R.drawable.ic_image_not_supported)
    }
}