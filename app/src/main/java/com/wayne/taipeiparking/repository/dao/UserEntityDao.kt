package com.wayne.taipeiparking.repository.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wayne.taipeiparking.repository.entity.UserEntity

@Dao
internal interface UserEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserEntity)

    @Query("SELECT * FROM USER")
    fun queryAsync(): LiveData<UserEntity?>

    @Query("SELECT * FROM USER")
    fun query(): UserEntity?

    @Query("DELETE FROM USER")
    fun deleteAll()
}