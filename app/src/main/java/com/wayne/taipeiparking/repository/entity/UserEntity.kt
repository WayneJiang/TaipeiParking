package com.wayne.taipeiparking.repository.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USER")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "OBJECT_ID")
    var objectId: String = "",
    @ColumnInfo(name = "NAME")
    var name: String = "",
    @ColumnInfo(name = "PHONE")
    var phone: String = "",
    @ColumnInfo(name = "CREATED_AT")
    var createdAt: String = "",
    @ColumnInfo(name = "UPDATED_AT")
    var updatedAt: String = "",
    @ColumnInfo(name = "SESSION_TOKEN")
    var sessionToken: String = "",
    @ColumnInfo(name = "TIMEZONE")
    var timezone: Int = -9999,
    @ColumnInfo(name = "EMAIL")
    var email: String = ""
)