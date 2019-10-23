package com.ratushny.wallsgeyser.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ratushny.wallsgeyser.WallsListRepository
import com.ratushny.wallsgeyser.data.WallsDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ListScreenViewModel(
    private val wallsListRepository: WallsListRepository,
    private val categories: Categories
) : ViewModel(),
    CoroutineScope {

    private val _wallsList = MutableLiveData<List<WallsDto>>().apply { value = emptyList() }
    val wallsList: LiveData<List<WallsDto>>
        get() = _wallsList

    private val _exceptions = MutableLiveData<Exception>()
    val exceptions: LiveData<Exception>
        get() = _exceptions

    private val wallsPage = MutableLiveData<Int>().apply { value = 1 }

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

    fun getData() {
        _exceptions.value = null
        val page = requireNotNull(wallsPage.value)
        launch {
            try {
                _wallsList.value =
                    _wallsList.value?.plus(wallsListRepository.getWallsData(categories, page))
            } catch (e: Exception) {
                _exceptions.value = e
            }
        }
    }
}