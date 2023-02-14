package com.pajaga.utils.system

import android.content.Intent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

fun moveNavigationTo(view: View, destination: Int) {
    view.findNavController().navigate(destination)
}

fun moveIntentTo(start: ComponentActivity, destination: AppCompatActivity) {
    val intent = Intent(start, destination::class.java)
    start.startActivity(intent)
}