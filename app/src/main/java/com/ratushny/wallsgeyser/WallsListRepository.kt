package com.ratushny.wallsgeyser

import com.ratushny.wallsgeyser.data.WallsDto
import com.ratushny.wallsgeyser.data.network.PixabayService
import com.ratushny.wallsgeyser.data.network.PixabayToListConverter

interface WallsListRepository {
    suspend fun getWallsData(): MutableList<WallsDto>
    suspend fun getFashionWallsData(): MutableList<WallsDto>
    suspend fun getNatureWallsData(): MutableList<WallsDto>
    suspend fun getBackgroundWallsData(): MutableList<WallsDto>
    suspend fun getAnimalsWallsData(): MutableList<WallsDto>
    suspend fun getComputerWallsData(): MutableList<WallsDto>
}

class WallsListRepositoryImpl(private val pixabayToListConverter: PixabayToListConverter) :
    WallsListRepository {

    override suspend fun getWallsData(): MutableList<WallsDto> {
        val service = PixabayService.create()
        val wallsResponseCall =
            service.getPixabayDataService("14009683-670121ec2afd0996e9106c0d0", "")
        return pixabayToListConverter.convertWalls(wallsResponseCall)
    }

    override suspend fun getFashionWallsData(): MutableList<WallsDto> {
        val service = PixabayService.create()
        val wallsResponseCall =
            service.getPixabayDataService("14009683-670121ec2afd0996e9106c0d0", "fashion")
        return pixabayToListConverter.convertWalls(wallsResponseCall)
    }

    override suspend fun getNatureWallsData(): MutableList<WallsDto> {
        val service = PixabayService.create()
        val wallsResponseCall =
            service.getPixabayDataService("14009683-670121ec2afd0996e9106c0d0", "nature")
        return pixabayToListConverter.convertWalls(wallsResponseCall)
    }

    override suspend fun getBackgroundWallsData(): MutableList<WallsDto> {
        val service = PixabayService.create()
        val wallsResponseCall =
            service.getPixabayDataService("14009683-670121ec2afd0996e9106c0d0", "background")
        return pixabayToListConverter.convertWalls(wallsResponseCall)
    }

    override suspend fun getAnimalsWallsData(): MutableList<WallsDto> {
        val service = PixabayService.create()
        val wallsResponseCall =
            service.getPixabayDataService("14009683-670121ec2afd0996e9106c0d0", "animals")
        return pixabayToListConverter.convertWalls(wallsResponseCall)
    }

    override suspend fun getComputerWallsData(): MutableList<WallsDto> {
        val service = PixabayService.create()
        val wallsResponseCall =
            service.getPixabayDataService("14009683-670121ec2afd0996e9106c0d0", "computer")
        return pixabayToListConverter.convertWalls(wallsResponseCall)
    }
}