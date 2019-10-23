package com.ratushny.wallsgeyser.screens.search

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.ratushny.wallsgeyser.databinding.SearchScreenFragmentBinding
import com.ratushny.wallsgeyser.screens.Categories

class SearchScreenFragment : Fragment() {

    private lateinit var binding: SearchScreenFragmentBinding
    private lateinit var viewModel: SearchScreenViewModel

    private val visibleThreshold = 4

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = SearchScreenFragmentBinding.inflate(
            inflater,
            container,
            false
        )

        // Spinner
        binding.categoriesSpinner.adapter =
            ArrayAdapter<Categories>(requireContext(), R.layout.item_spinner, Categories.values())
        binding.categoriesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onNothingSelected(p0: AdapterView<*>?) = Unit

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    viewModel.wallsCategory.value = Categories.values()[p2]
                }
            }

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SearchScreenViewModel(
                    WallsListRepositoryImpl(PixabayToListConverterImpl())
                ) as T
            }
        }).get(SearchScreenViewModel::class.java)

        binding.searchScreenViewModel = viewModel
        binding.lifecycleOwner = this

        val adapter = WallsListAdapter()
        binding.searchScreenRecyclerview.adapter = adapter

        val mLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.phone_grid))
        binding.searchScreenRecyclerview.layoutManager = mLayoutManager

        // Pagination
        binding.searchScreenRecyclerview.addOnScrollListener(object :
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
                binding.errorButtonSearchScreen.visibility = View.VISIBLE
                binding.searchScreenRecyclerview.visibility = View.GONE
                binding.errorTextSearchScreen.visibility = View.VISIBLE
                binding.errorTextSearchScreen.text = it.message
            } else {
                binding.errorButtonSearchScreen.visibility = View.GONE
                binding.searchScreenRecyclerview.visibility = View.VISIBLE
                binding.errorTextSearchScreen.visibility = View.GONE
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
                        override fun onLoadCleared(placeholder: Drawable?) = Unit

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