package com.ratushny.wallsgeyser.screens.search

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.ratushny.wallsgeyser.databinding.SearchScreenFragmentBinding
import com.ratushny.wallsgeyser.screens.Categories
import com.ratushny.wallsgeyser.screens.ListScreenViewModel
import kotlinx.android.synthetic.main.search_screen_fragment.*

class SearchScreenFragment : Fragment() {

    private lateinit var binding: SearchScreenFragmentBinding
    private lateinit var viewModel: SearchScreenViewModel

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
            R.layout.search_screen_fragment,
            container,
            false
        )

        // Spinner
        binding.categoriesSpinner.adapter = ArrayAdapter<Categories>(context!!,R.layout.item_spinner, Categories.values())
        binding.categoriesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

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
                super.onScrolled(recyclerView, dx, dy)

                visibleItemCount = recyclerView.childCount
                totalItemCount = mLayoutManager.itemCount
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition()

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                        Log.i("SearchScreenFragment","TotalItemCount: $totalItemCount")
                        Log.i("SearchScreenFragment","PreviousTotal: $previousTotal")
                    }
                }

                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                    page++
                    viewModel.getData(page)
                    loading = true
                    Log.i("SearchScreenFragment","Page: $page")
                    Log.i("SearchScreenFragment","firstVisibleItem: $firstVisibleItem")
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
                        override fun onLoadCleared(placeholder: Drawable?) = Unit


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