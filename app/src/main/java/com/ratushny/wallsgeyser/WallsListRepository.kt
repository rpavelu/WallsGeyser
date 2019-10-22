package com.ratushny.wallsgeyser

import com.ratushny.wallsgeyser.data.WallsDto
import com.ratushny.wallsgeyser.data.network.PixabayService
import com.ratushny.wallsgeyser.data.network.PixabayToListConverter
import com.ratushny.wallsgeyser.screens.Categories

interface WallsListRepository {
    suspend fun getWallsData(): MutableList<WallsDto>
    suspend fun getFashionWallsData(): MutableList<WallsDto>
    suspend fun getNatureWallsData(): MutableList<WallsDto>
    suspend fun getBackgroundWallsData(): MutableList<WallsDto>
    suspend fun getAnimalsWallsData(): MutableList<WallsDto>
    suspend fun getComputerWallsData(): MutableList<WallsDto>
}

class WallsListRepositoryImpl(
    private val pixabayToListConverter: PixabayToListConverter,
    private val service: PixabayService = PixabayService.create()
) :
    WallsListRepository {

    override suspend fun getWallsData(): MutableList<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                ""
            )
        )
    }

    override suspend fun getFashionWallsData(): MutableList<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.FASHION.str
            )
        )
    }

    override suspend fun getNatureWallsData(): MutableList<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.NATURE.str
            )
        )
    }

    override suspend fun getBackgroundWallsData(): MutableList<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.BACKGROUND.str
            )
        )
    }

    override suspend fun getAnimalsWallsData(): MutableList<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.ANIMALS.str
            )
        )
    }

    override suspend fun getComputerWallsData(): MutableList<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.COMPUTER.str
            )
        )
    }
}