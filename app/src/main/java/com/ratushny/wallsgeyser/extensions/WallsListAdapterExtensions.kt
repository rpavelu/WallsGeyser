package com.ratushny.wallsgeyser.extensions

import android.app.Activity
import com.ratushny.wallsgeyser.R
import com.ratushny.wallsgeyser.WallsListAdapter

fun WallsListAdapter.setWallsClickListener(activity: Activity) {
    setOnItemClickListener(object : WallsListAdapter.ClickListener {
        override fun onClick(pos: Int) {

            activity.showMessage(R.string.setting_wallpaper_message)

            activity.loadBitmap(wallsData[pos].fullHDURL) {
                activity.wallpaperManager.setBitmap(it)
                activity.moveTaskToBack(true)
            }
        }
    })
}