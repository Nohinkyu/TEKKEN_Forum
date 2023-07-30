package com.nik.tkforum.binding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.nik.tkforum.R
import com.nik.tkforum.data.ChatRoomInfo
import com.nik.tkforum.util.DateFormat

@BindingAdapter("dateFormat")
fun setDate(view: TextView, dateString: String) {
    view.text = DateFormat.convertToDisplayDate(dateString)
}

@BindingAdapter("playTime")
fun setPlayTime(view: TextView, playTime: Int) {
    view.text = "${playTime / 60}:${playTime % 60}"
}

@BindingAdapter("userCount")
fun setUserCount(view: TextView, userCount: Int) {
    view.text = "${userCount}명"
}

@BindingAdapter("lastChat")
fun lastChat(view: TextView, chatRoomInfo: ChatRoomInfo) {
    val context = view.context
    view.text =
        chatRoomInfo.chatRoom.chatList.values.toList().lastOrNull()?.message ?: context.getString(
            R.string.empty_chat
        )
}
