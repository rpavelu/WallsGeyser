package com.ratushny.wallsgeyser.screens

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ratushny.wallsgeyser.R
import com.ratushny.wallsgeyser.WallsListAdapter
import com.ratushny.wallsgeyser.WallsListRepositoryImpl
import com.ratushny.wallsgeyser.data.network.PixabayToListConverterImpl
import com.ratushny.wallsgeyser.databinding.MainScreenFragmentBinding

abstract class ListScreenFragment(private val categories: Categories) : Fragment() {

    private lateinit var binding: MainScreenFragmentBinding
    private lateinit var viewModel: ListScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.main_screen_fragment,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ListScreenViewModel(WallsListRepositoryImpl(PixabayToListConverterImpl()), categories).also {
                    it.getData()
                } as T
            }
        }).get(ListScreenViewModel::class.java)

        binding.mainScreenViewModel = viewModel
        binding.lifecycleOwner = this

        // Implementing adapter for RecyclerView
        val adapter = WallsListAdapter()
        binding.mainScreenRecyclerview.adapter = adapter

        // Adding grid layout
        binding.mainScreenRecyclerview.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.phone_grid))

        viewModel.wallsList.observe(viewLifecycleOwner, Observer {
            adapter.addWalls(it)
        })

        // Setting on click listener to adapter
        adapter.setOnItemClickListener(object : WallsListAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {

                Toast.makeText(context, "Setting wallpaper", Toast.LENGTH_SHORT).show()

                val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)

                // Loading high-res wallpaper
                Glide.with(context!!)
                    .asBitmap()
                    .load(viewModel.wallsList.value!![pos].largeImageURL)
                    .into(object : CustomTarget<Bitmap>() {
                        override fun onLoadCleared(placeholder: Drawable?) {
                        }

                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            // Setting wallpaper as bitmap
                            wallpaperManager.setBitmap(resource)
                        }

                    })
            }
        })
        return binding.root
    }
}