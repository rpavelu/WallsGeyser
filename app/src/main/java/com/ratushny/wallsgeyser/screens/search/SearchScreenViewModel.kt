package com.ratushny.wallsgeyser.screens.search

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

class SearchScreenViewModel(
    private val wallsListRepository: WallsListRepository
) : ViewModel(),
    CoroutineScope {

    private val _wallsList = MutableLiveData<List<WallsDto>>()
    val wallsList: LiveData<List<WallsDto>>
        get() = _wallsList

    private val _exceptions = MutableLiveData<Exception>()
    val exceptions: LiveData<Exception>
        get() = _exceptions

    private val wallsPage = MutableLiveData<Int>().apply { value = 1 }

    val wallsCategory = MutableLiveData<Categories>()

    val searchWords = MutableLiveData<String>().apply { value = "" }

    val previousTotal = MutableLiveData<Int>().apply { value = 0 }
    val totalItemCount = MutableLiveData<Int>().apply { value = 0 }
    val visibleItemCount = MutableLiveData<Int>().apply { value = 0 }
    val firstVisibleItem = MutableLiveData<Int>().apply { value = 0 }
    val loading = MutableLiveData<Boolean>().apply { value = true }

    private val viewModelJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + viewModelJob

    override fun onCleared() {
        viewModelJob.cancel()
    }

    fun increasePage() {
        wallsPage.value = wallsPage.value?.plus(1)
    }

    fun getDataButton() {
        wallsPage.value = 1
        previousTotal.value = 0
        getData()
    }

    fun getData() {
        _exceptions.value = null
        val category = requireNotNull(wallsCategory.value)
        val words = requireNotNull(searchWords.value)
        val page = requireNotNull(wallsPage.value)
        launch {
            try {
                if (page == 1) {
                    _wallsList.value = wallsListRepository.getSearchWallsData(
                        category,
                        page,
                        words
                    )
                } else
                    _wallsList.value = _wallsList.value?.plus(
                        wallsListRepository.getSearchWallsData(
                            category,
                            page,
                            words
                        )
                    )
            } catch (e: Exception) {
                _exceptions.value = e
            }
        }
    }
}