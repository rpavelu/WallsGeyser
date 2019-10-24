package com.ratushny.wallsgeyser.extensions

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager?

val Context.wallpaperManager: WallpaperManager
    get() = WallpaperManager.getInstance(this)

fun Context.loadBitmap(url: String, onBitmapLoaded: (Bitmap) -> Unit) {

    Glide.with(this)
        .asBitmap()
        .load(url)
        .into(object : CustomTarget<Bitmap>() {
            override fun onLoadCleared(placeholder: Drawable?) = Unit

            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                onBitmapLoaded(resource)
            }

        })
}

fun Context.showMessage(text: Int): Unit = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()