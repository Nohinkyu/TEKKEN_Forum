package com.nik.tkforum.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nik.tkforum.util.DateFormat

@BindingAdapter("dateFormat")
fun setDate(view: TextView, dateString: String) {
    view.text = DateFormat.convertToDisplayDate(dateString)
}

@BindingAdapter("playTime")
fun setPlayTime(view: TextView, playTime: Int) {
    view.text = "${playTime / 60}:${playTime % 60}"
}

