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
import androidx.recyclerview.widget.RecyclerView
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

    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var previousTotal = 0
    private val visibleThreshold = 10
    private var firstVisibleItem = 0
    private var page = 1
    private var loading = true

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
                return ListScreenViewModel(
                    WallsListRepositoryImpl(PixabayToListConverterImpl()),
                    categories
                ).also {
                    it.getData(page)
                } as T
            }
        }).get(ListScreenViewModel::class.java)

        binding.mainScreenViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = WallsListAdapter()
        binding.mainScreenRecyclerview.adapter = adapter

        val mLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.phone_grid))
        binding.mainScreenRecyclerview.layoutManager = mLayoutManager

        // Pagination
        binding.mainScreenRecyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = recyclerView.childCount
                totalItemCount = mLayoutManager.itemCount
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    page++
                    viewModel.getData(page)
                    loading = true
                }
            }
        })

        viewModel.wallsList.observe(viewLifecycleOwner, Observer {
            adapter.addWalls(it)
        })

        adapter.setOnItemClickListener(object : WallsListAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {

                Toast.makeText(context, "Setting wallpaper", Toast.LENGTH_SHORT).show()

                val wallpaperManager: WallpaperManager = WallpaperManager.getInstance(context)

                val currentWalls = requireNotNull(viewModel.wallsList.value)
                Glide.with(requireContext())
                    .asBitmap()
                    .load(currentWalls[pos].largeImageURL)
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