package com.ratushny.wallsgeyser.screens.background

import android.util.Log
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

class BackgroundScreenViewModel(private val wallsListRepository: WallsListRepository) : ViewModel(),
    CoroutineScope {

    private val _wallsList = MutableLiveData<List<WallsDto>>()
    val wallsList: LiveData<List<WallsDto>>
        get() = _wallsList

    private val viewModelJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + viewModelJob

    override fun onCleared() {
        viewModelJob.cancel()
    }

    fun getData() {
        launch {
            _wallsList.value = wallsListRepository.getBackgroundWallsData()
        }
    }
}