package com.wayne.taipeiparking.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.wayne.taipeiparking.repository.entity.ParkingInfoEntity
import com.wayne.taipeiparking.repository.entity.UserEntity
import kotlinx.coroutines.*
import java.io.File
import kotlin.coroutines.CoroutineContext

object Repository {
    private lateinit var dbManager: DBManager

    private val mCoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO
    }

    private lateinit var mJob: Job

    fun setUpRepository(context: Context) {
        dbManager = Room.databaseBuilder(context, DBManager::class.java, "DB").build()
    }

    internal fun UserEntity.insert() = dbManager.getUserEntityDao().insert(this)

    internal fun queryUserEntity() = dbManager.getUserEntityDao().query()

    internal fun ParkingInfoEntity.insert() = dbManager.getParkingInfoEntityDao().insert(this)

    fun updateParkingInfoEntity(id: String, available: Int, charging: Int, idle: Int) =
        dbManager.getParkingInfoEntityDao().update(id, available, charging, idle)

    fun queryAllParkingInfoEntityAsync() = dbManager.getParkingInfoEntityDao().queryAllAsync()

    fun queryUserEntityAsync() = dbManager.getUserEntityDao().queryAsync()

    fun clearAllTablesAndFile(context: Context, callback: (Boolean) -> Unit) {
        mJob = mCoroutineScope.launch(
            CoroutineExceptionHandler { _, throwable ->
                Log.d("Wayne", Log.getStackTraceString(throwable))

                mCoroutineScope.launch {
                    withContext(Dispatchers.Main) {
                        callback.invoke(false)
                    }
                }

                mJob.cancel()
            }
        ) {
            File(context.filesDir, "TCMSV_alldesc.json").delete()
            File(context.filesDir, "TCMSV_allavailable.json").delete()

            dbManager.clearAllTables()

            mCoroutineScope.launch {
                withContext(Dispatchers.Main) {
                    callback.invoke(true)
                }
            }
        }
    }
}