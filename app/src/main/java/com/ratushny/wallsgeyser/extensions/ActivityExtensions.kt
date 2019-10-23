package com.ratushny.wallsgeyser.extensions

import android.app.Activity

fun Activity.hideSoftKeyboard() {
    val decorView = window.decorView

    inputMethodManager?.apply {
        hideSoftInputFromWindow(decorView.windowToken, 0)
        hideSoftInputFromWindow(decorView.applicationWindowToken, 0)
    }
}