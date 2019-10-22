package com.ratushny.wallsgeyser.screens.nature

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
import com.ratushny.wallsgeyser.R
import com.ratushny.wallsgeyser.WallsListAdapter
import com.ratushny.wallsgeyser.WallsListRepositoryImpl
import com.ratushny.wallsgeyser.data.network.PixabayToListConverterImpl
import com.ratushny.wallsgeyser.databinding.NatureScreenFragmentBinding

class NatureScreenFragment : Fragment() {

    private lateinit var binding: NatureScreenFragmentBinding
    private lateinit var viewModel: NatureScreenViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.nature_screen_fragment,
            container,
            false
        )

        viewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return NatureScreenViewModel(WallsListRepositoryImpl(PixabayToListConverterImpl())).also {
                    it.getData()
                } as T
            }
        }).get(NatureScreenViewModel::class.java)

        binding.natureScreenViewModel = viewModel
        binding.lifecycleOwner = this

        // Implementing adapter for RecyclerView
        val adapter = WallsListAdapter()
        binding.natureScreenRecyclerview.adapter = adapter

        binding.natureScreenRecyclerview.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.phone_grid))

        viewModel.wallsList.observe(viewLifecycleOwner, Observer {
            adapter.addWalls(it)
        })

        // Setting on click listener to adapter
        adapter.setOnItemClickListener(object : WallsListAdapter.ClickListener {
            override fun onClick(pos: Int, aView: View) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}