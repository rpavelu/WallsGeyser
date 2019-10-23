package com.ratushny.wallsgeyser.databinding

import android.app.Activity
import android.view.View
import androidx.databinding.BindingAdapter
import com.ratushny.wallsgeyser.extensions.hideSoftKeyboard

@BindingAdapter("closeSoftKeyboardClickListener")
fun View.setCloseSoftKeyboardClickListener(listener: () -> Unit) {
    setOnClickListener {
        (context as? Activity)?.hideSoftKeyboard()
        listener()
    }
}