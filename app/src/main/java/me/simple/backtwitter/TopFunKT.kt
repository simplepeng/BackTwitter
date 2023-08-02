package me.simple.backtwitter

import android.widget.Toast

fun showToast(text: String) {
    Toast.makeText(App.context, text, Toast.LENGTH_SHORT).show()
}