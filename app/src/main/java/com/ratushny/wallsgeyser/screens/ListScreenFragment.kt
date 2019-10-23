package com.ratushny.wallsgeyser.screens

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.ratushny.wallsgeyser.databinding.ListScreenFragmentBinding

abstract class ListScreenFragment(private val categories: Categories) : Fragment() {

    private lateinit var binding: ListScreenFragmentBinding
    private lateinit var viewModel: ListScreenViewModel

    private val visibleThreshold = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = ListScreenFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ListScreenViewModel(
                    WallsListRepositoryImpl(PixabayToListConverterImpl()),
                    categories
                ).also {
                    it.getData()
                } as T
            }
        }).get(ListScreenViewModel::class.java)

        binding.listScreenViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = WallsListAdapter()
        binding.listScreenRecyclerview.adapter = adapter

        val mLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.phone_grid))
        binding.listScreenRecyclerview.layoutManager = mLayoutManager

        // Pagination
        binding.listScreenRecyclerview.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                viewModel.visibleItemCount.value = recyclerView.childCount
                viewModel.totalItemCount.value = mLayoutManager.itemCount
                viewModel.firstVisibleItem.value = mLayoutManager.findFirstVisibleItemPosition()

                val previousTotal = requireNotNull(viewModel.previousTotal.value)
                val totalItemCount = requireNotNull(viewModel.totalItemCount.value)
                val visibleItemCount = requireNotNull(viewModel.visibleItemCount.value)
                val firstVisibleItem = requireNotNull(viewModel.firstVisibleItem.value)
                val loading = requireNotNull(viewModel.loading.value)

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        viewModel.loading.value = false
                        viewModel.previousTotal.value = viewModel.totalItemCount.value
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    viewModel.increasePage()
                    viewModel.getData()
                    viewModel.loading.value = true
                }
            }
        })

        viewModel.exceptions.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                binding.errorButton.visibility = View.VISIBLE
                binding.listScreenRecyclerview.visibility = View.GONE
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = it.message
            } else {
                binding.errorButton.visibility = View.GONE
                binding.listScreenRecyclerview.visibility = View.VISIBLE
                binding.errorText.visibility = View.GONE
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
                            wallpaperManager.setBitmap(resource)
                            activity?.moveTaskToBack(true)
                        }

                    })
            }
        })
        return binding.root
    }
}