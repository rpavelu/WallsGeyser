package com.ratushny.wallsgeyser.screens.fashion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.ratushny.wallsgeyser.R
import com.ratushny.wallsgeyser.WallsListAdapter
import com.ratushny.wallsgeyser.WallsListRepositoryImpl
import com.ratushny.wallsgeyser.data.network.PixabayToListConverterImpl
import com.ratushny.wallsgeyser.databinding.FashionScreenFragmentBinding
import com.ratushny.wallsgeyser.databinding.MainScreenFragmentBinding
import com.ratushny.wallsgeyser.screens.MainScreenViewModel

class FashionScreenFragment : Fragment() {

    private lateinit var binding: FashionScreenFragmentBinding
    private lateinit var viewModel: FashionScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fashion_screen_fragment,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FashionScreenViewModel(WallsListRepositoryImpl(PixabayToListConverterImpl())).also {
                    it.getData()
                } as T
            }
        }).get(FashionScreenViewModel::class.java)

        binding.fashionScreenViewModel = viewModel
        binding.lifecycleOwner = this

        // Implementing adapter for RecyclerView
        val adapter = WallsListAdapter()
        binding.fashionScreenRecyclerview.adapter = adapter

        binding.fashionScreenRecyclerview.layoutManager = GridLayoutManager(context, 2)

        viewModel.wallsList.observe(viewLifecycleOwner, Observer {
            adapter.addWalls(viewModel.wallsList.value!!)
        })

        return binding.root
    }
}