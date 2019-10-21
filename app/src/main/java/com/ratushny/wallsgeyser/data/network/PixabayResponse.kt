package com.ratushny.wallsgeyser.data.network

import com.ratushny.wallsgeyser.data.WallsDto

data class PixabayResponse(
    val hits: List<WallsDto>
)