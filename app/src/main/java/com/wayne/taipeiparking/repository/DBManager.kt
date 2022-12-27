package com.wayne.taipeiparking.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wayne.taipeiparking.repository.dao.ParkingInfoEntityDao
import com.wayne.taipeiparking.repository.dao.UserEntityDao
import com.wayne.taipeiparking.repository.entity.ParkingInfoEntity
import com.wayne.taipeiparking.repository.entity.UserEntity

@Database(
    entities = [UserEntity::class, ParkingInfoEntity::class],
    version = 1,
    exportSchema = true
)
internal abstract class DBManager : RoomDatabase() {
    abstract fun getUserEntityDao(): UserEntityDao

    abstract fun getParkingInfoEntityDao(): ParkingInfoEntityDao
}