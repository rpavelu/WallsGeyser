package com.ratushny.wallsgeyser.screens.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratushny.wallsgeyser.WallsListRepository
import com.ratushny.wallsgeyser.data.WallsDto
import com.ratushny.wallsgeyser.screens.Categories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import android.widget.EditText
import androidx.databinding.InverseBindingAdapter
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.InverseMethod


class SearchScreenViewModel(
    private val wallsListRepository: WallsListRepository
) : ViewModel(),
    CoroutineScope {

    private val _wallsList = MutableLiveData<List<WallsDto>>()
    val wallsList: LiveData<List<WallsDto>>
        get() = _wallsList

    val wallsPage = MutableLiveData<Int>().apply { value = 1 }

    val wallsCategory = MutableLiveData<Categories>()

    val searchWords = MutableLiveData<String>().apply { value = "" }

    private val viewModelJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + viewModelJob

    override fun onCleared() {
        viewModelJob.cancel()
    }

    fun getData(page: Int) {
        launch {
            val category = requireNotNull(wallsCategory.value)
            val words = requireNotNull(searchWords.value)
            if (page == 1) {
                _wallsList.value = wallsListRepository.getSearchWallsData(
                    category,
                    page,
                    words
                )
                Log.i("SearchScreenViewModel", "Page: $page")
            } else
                _wallsList.value = _wallsList.value?.plus(
                    wallsListRepository.getSearchWallsData(
                        category,
                        page,
                        words
                    )
                )
        }
    }
}