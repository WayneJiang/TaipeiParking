package com.wayne.taipeiparking.webservice.json

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "name")
    val name: String = "",
    @Json(name = "phone")
    val phone: String = "",
    @Json(name = "createdAt")
    val createdAt: String = "",
    @Json(name = "updatedAt")
    val updatedAt: String = "",
    @Json(name = "objectId")
    val objectId: String = "",
    @Json(name = "sessionToken")
    val sessionToken: String = "",
    @Json(name = "timezone")
    val timezone: Int = -9999
)
