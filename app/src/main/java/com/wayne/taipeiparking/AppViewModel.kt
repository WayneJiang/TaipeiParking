package com.wayne.taipeiparking

import androidx.lifecycle.ViewModel
import com.wayne.taipeiparking.repository.Repository

open class AppViewModel : ViewModel() {
    fun queryAllParkingInfo() = Repository.queryAllParkingInfoEntityAsync()

    fun queryUser() = Repository.queryUserEntityAsync()
}