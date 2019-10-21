package com.ratushny.wallsgeyser

import com.ratushny.wallsgeyser.data.WallsDto
import com.ratushny.wallsgeyser.data.network.PixabayService
import com.ratushny.wallsgeyser.data.network.PixabayToListConverter

interface WallsListRepository {
    suspend fun getWallsData(): MutableList<WallsDto>
}

class WallsListRepositoryImpl(private val pixabayToListConverter: PixabayToListConverter) :
    WallsListRepository {

    override suspend fun getWallsData(): MutableList<WallsDto> {
        val service = PixabayService.create()
        val wallsResponseCall =
            service.getPixabayDataService("14009683-670121ec2afd0996e9106c0d0", "")
        return pixabayToListConverter.convertWalls(wallsResponseCall)
    }
}