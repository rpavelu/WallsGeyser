package com.ratushny.wallsgeyser.data.network

import com.ratushny.wallsgeyser.data.WallsDto

interface PixabayToListConverter {
    fun convertWalls(response: PixabayResponse): List<WallsDto>
}

class PixabayToListConverterImpl : PixabayToListConverter {
    override fun convertWalls(response: PixabayResponse): List<WallsDto> =
        response.hits.map { wallsDto ->
            WallsDto(wallsDto.previewURL, wallsDto.largeImageURL)
        }
}