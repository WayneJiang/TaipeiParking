package com.wayne.taipeiparking.webservice.json

import com.squareup.moshi.Json

data class ParkingStatusJson(
    @Json(name = "data")
    val parkingStatus: ParkingStatus = ParkingStatus()
)

data class ParkingStatus(
    @Json(name = "park")
    val statusList: List<Status> = emptyList()
)

data class Status(
    @Json(name = "id")
    val id: String = "",
    @Json(name = "availablecar")
    val available: Int = -9999,
    @Json(name = "ChargeStation")
    val chargeStation: ChargeStation = ChargeStation()
)

data class ChargeStation(
    @Json(name = "scoketStatusList")
    val socketList: List<Socket> = emptyList()
)

data class Socket(
    @Json(name = "spot_status")
    val status: String = ""
)
