package com.ratushny.wallsgeyser

import com.ratushny.wallsgeyser.data.WallsDto
import com.ratushny.wallsgeyser.data.network.PixabayService
import com.ratushny.wallsgeyser.data.network.PixabayToListConverter
import com.ratushny.wallsgeyser.screens.Categories

interface WallsListRepository {
    suspend fun getWallsData(): List<WallsDto>
    suspend fun getFashionWallsData(): List<WallsDto>
    suspend fun getNatureWallsData(): List<WallsDto>
    suspend fun getBackgroundWallsData(): List<WallsDto>
    suspend fun getAnimalsWallsData(): List<WallsDto>
    suspend fun getComputerWallsData(): List<WallsDto>
}

class WallsListRepositoryImpl(
    private val pixabayToListConverter: PixabayToListConverter,
    private val service: PixabayService = PixabayService.create()
) :
    WallsListRepository {

    override suspend fun getWallsData(): List<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                ""
            )
        )
    }

    override suspend fun getFashionWallsData(): List<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.FASHION.getString
            )
        )
    }

    override suspend fun getNatureWallsData(): List<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.NATURE.getString
            )
        )
    }

    override suspend fun getBackgroundWallsData(): List<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.BACKGROUND.getString
            )
        )
    }

    override suspend fun getAnimalsWallsData(): List<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.ANIMALS.getString
            )
        )
    }

    override suspend fun getComputerWallsData(): List<WallsDto> {
        return pixabayToListConverter.convertWalls(
            service.getPixabayDataService(
                BuildConfig.PIXABAY_KEY,
                Categories.COMPUTER.getString
            )
        )
    }
}