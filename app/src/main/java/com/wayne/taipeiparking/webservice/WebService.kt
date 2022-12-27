package com.wayne.taipeiparking.webservice

import android.content.Context
import android.net.Uri
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.wayne.taipeiparking.repository.Repository
import com.wayne.taipeiparking.repository.Repository.insert
import com.wayne.taipeiparking.repository.entity.ParkingInfoEntity
import com.wayne.taipeiparking.repository.entity.UserEntity
import com.wayne.taipeiparking.webservice.json.ParkingLotJson
import com.wayne.taipeiparking.webservice.json.ParkingStatusJson
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.buffer
import okio.sink
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

object WebService {
    private val mOkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            retryOnConnectionFailure(true)
            connectTimeout(300, TimeUnit.SECONDS)
            writeTimeout(300, TimeUnit.SECONDS)
            readTimeout(300, TimeUnit.SECONDS)
            addInterceptor(HttpLoggingInterceptor(HttpLogger()).apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }.build()
    }

    private class HttpLogger : HttpLoggingInterceptor.Logger {
        override fun log(message: String) {
            val maxLength = 2000
            var start = 0
            var end = maxLength
            val logLength = message.length
            for (index in 0 until 100) {
                if (logLength > end) {
                    Log.d("Wayne", Uri.decode(message.substring(start, end)))
                    start = end
                    end += maxLength
                } else {
                    Log.d("Wayne", Uri.decode(message.substring(start, logLength)))
                    break
                }
            }
        }
    }

    private val mWebInterface by lazy {
        Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        ).addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://noodoe-app-development.web.app/")
            .client(mOkHttpClient)
            .build()
            .create(WebInterface::class.java)
    }

    private val mOpenDataInterface by lazy {
        Retrofit.Builder().addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl("https://tcgbusfs.blob.core.windows.net/blobtcmsv/")
            .client(mOkHttpClient)
            .build()
            .create(OpenDataInterface::class.java)
    }

    private val mCoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO
    }

    private lateinit var mJob: Job

    private const val APPLICATION_ID = "vqYuKPOkLQLYHhk4QTGsGKFwATT4mBIGREI2m8eD"

    fun requestLogin(email: String, password: String, callback: (Boolean) -> Unit) {
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
            val params = mutableMapOf<String, String>()
            params["username"] = email
            params["password"] = password

            val loginResponse = mWebInterface.postLoginAsync(APPLICATION_ID, params).await()

            loginResponse.apply {
                UserEntity(
                    objectId = objectId,
                    name = name,
                    phone = phone,
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                    sessionToken = sessionToken,
                    timezone = timezone,
                    email = email
                ).insert()
            }

            withContext(Dispatchers.Main) {
                callback.invoke(true)
            }
        }
    }

    fun requestDownloadParkingJSON(context: Context, callback: (Boolean) -> Unit) {
        mCoroutineScope.launch(
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
            File(context.filesDir, "TCMSV_alldesc.json").sink().buffer().use {
                it.writeAll(
                    mOpenDataInterface.downloadFileAsync("TCMSV_alldesc.json").await().source()
                )
            }

            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(ParkingLotJson::class.java)
                .fromJson(
                    File(context.filesDir, "TCMSV_alldesc.json")
                        .bufferedReader().use {
                            it.readText()
                        }
                )?.apply {
                    parkingLot.park.forEach {
                        ParkingInfoEntity(
                            id = it.id,
                            name = it.name,
                            address = it.address,
                            total = it.total
                        ).insert()
                    }
                }

            File(context.filesDir, "TCMSV_allavailable.json").sink().buffer().use {
                it.writeAll(
                    mOpenDataInterface.downloadFileAsync("TCMSV_allavailable.json").await().source()
                )
            }

            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
                .adapter(ParkingStatusJson::class.java).fromJson(
                    File(context.filesDir, "TCMSV_allavailable.json")
                        .bufferedReader().use {
                            it.readText()
                        }
                )?.apply {
                    parkingStatus.statusList.forEach { status ->
                        status.apply {
                            Repository.updateParkingInfoEntity(
                                id,
                                available,
                                chargeStation.socketList.filter {
                                    it.status == "充電中"
                                }.size,
                                chargeStation.socketList.filter {
                                    it.status == "待機中"
                                }.size
                            )
                        }
                    }
                }

            withContext(Dispatchers.Main) {
                callback.invoke(true)
            }
        }
    }

    fun requestUserUpdate(timezone: Int, callback: (Boolean) -> Unit) {
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
            val userEntity = Repository.queryUserEntity()

            if (userEntity != null) {
                val params = mutableMapOf<String, String>()
                params["timezone"] = "$timezone"

                val userUpdatedResponse =
                    mWebInterface.putUpdateUserAsync(
                        APPLICATION_ID,
                        userEntity.sessionToken,
                        userEntity.objectId,
                        params
                    ).await()

                if (userUpdatedResponse.updatedAt.isNotEmpty()) {
                    userEntity.apply {
                        this.timezone = timezone
                    }.insert()

                    withContext(Dispatchers.Main) {
                        callback.invoke(true)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        callback.invoke(true)
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    callback.invoke(false)
                }
            }
        }
    }
}