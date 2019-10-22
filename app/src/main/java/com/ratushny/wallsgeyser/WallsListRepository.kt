package com.ratushny.wallsgeyser

import com.ratushny.wallsgeyser.data.WallsDto
import com.ratushny.wallsgeyser.data.network.PixabayService
import com.ratushny.wallsgeyser.data.network.PixabayToListConverter
import com.ratushny.wallsgeyser.screens.Categories

interface WallsListRepository {
    suspend fun getWallsData(categories: Categories, page: Int): List<WallsDto>
}

class WallsListRepositoryImpl(
    private val pixabayToListConverter: PixabayToListConverter,
    private val service: PixabayService = PixabayService.create()
) :
    WallsListRepository {

    override suspend fun getWallsData(categories: Categories, page: Int): List<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                categories.getString,
                page
            )
        )
    }
}