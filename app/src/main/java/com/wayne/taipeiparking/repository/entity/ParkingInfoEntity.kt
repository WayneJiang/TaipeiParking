package com.wayne.taipeiparking.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "PARKING_INFO")
data class ParkingInfoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ID")
    var id: String = "",
    @ColumnInfo(name = "NAME")
    var name: String = "",
    @ColumnInfo(name = "ADDRESS")
    var address: String = "",
    @ColumnInfo(name = "TOTAL")
    var total: Int = -9999,
    @ColumnInfo(name = "AVAILABLE")
    var available: Int = -9999,
    @ColumnInfo(name = "CHARGING")
    var charing: Int = -9999,
    @ColumnInfo(name = "STANDBY")
    var standby: Int = -9999
)