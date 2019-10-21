package com.ratushny.wallsgeyser.screens.nature

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

class NatureScreenViewModel(private val wallsListRepository: WallsListRepository) : ViewModel(),
    CoroutineScope {

    private val _wallsList = MutableLiveData<MutableList<WallsDto>>()
    val wallsList: LiveData<MutableList<WallsDto>>
        get() = _wallsList

    private val viewModelJob = SupervisorJob()
    override val coroutineContext: CoroutineContext = Dispatchers.Main + viewModelJob

    override fun onCleared() {
        viewModelJob.cancel()
    }

    fun getData() {
        launch {
            _wallsList.value = wallsListRepository.getNatureWallsData()
            Log.i("MainScreenViewModel", "NatureWallsList: ${_wallsList.value}")
        }
    }
}