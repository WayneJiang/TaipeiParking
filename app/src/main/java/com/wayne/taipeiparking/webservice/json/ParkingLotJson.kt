package com.wayne.taipeiparking.webservice.json

import com.squareup.moshi.Json

data class ParkingLotJson(
    @Json(name = "data")
    val parkingLot: ParkingLot = ParkingLot()
)

data class ParkingLot(
    @Json(name = "park")
    val park: List<Park> = emptyList()
)

data class Park(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "name")
    val name: String = "",
    @Json(name = "address")
    val address: String = "",
    @Json(name = "totalcar")
    val total: Int = -9999
)
