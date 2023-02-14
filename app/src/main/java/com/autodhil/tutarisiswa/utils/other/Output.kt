package com.pajaga.utils.other

import android.content.Context
import android.util.Log
import android.widget.Toast

fun showLogAssert(tag: String, msg: String) {
    Log.println(Log.ASSERT, tag, msg)
}

fun showToast(context: Context, msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}

fun Int.toSongTime(): String{
    var elapsedTime: String?
    val minutes = this / 1000 / 60
    val seconds = this / 1000 % 60
    elapsedTime = "$minutes:"
    if (seconds < 10){
        elapsedTime += "0"
    }
    elapsedTime += seconds
    return elapsedTime
}



