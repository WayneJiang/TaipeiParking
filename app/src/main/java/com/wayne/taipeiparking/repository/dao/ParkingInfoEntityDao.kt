package com.wayne.taipeiparking.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wayne.taipeiparking.repository.entity.ParkingInfoEntity

@Dao
internal interface ParkingInfoEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(parkingInfoEntity: ParkingInfoEntity)

    @Query("UPDATE PARKING_INFO SET AVAILABLE = :available, CHARGING = :charging, STANDBY = :standby WHERE ID = :id")
    fun update(id: String, available: Int, charging: Int, standby: Int)

    @Query("SELECT * FROM PARKING_INFO ORDER BY ID")
    fun queryAllAsync(): LiveData<List<ParkingInfoEntity>>

    @Query("DELETE FROM PARKING_INFO")
    fun deleteAll()
}