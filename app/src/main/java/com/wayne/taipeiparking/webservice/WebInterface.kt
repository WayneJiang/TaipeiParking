package com.wayne.taipeiparking.webservice

import com.wayne.taipeiparking.webservice.json.LoginResponse
import com.wayne.taipeiparking.webservice.json.UserUpdatedResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.*

interface WebInterface {
    @FormUrlEncoded
    @POST("api/login")
    fun postLoginAsync(
        @Header("X-Parse-Application-Id") applicationId: String,
        @FieldMap param: Map<String, String>
    ): Deferred<LoginResponse>

    @FormUrlEncoded
    @PUT("api/users/{objectId}")
    fun putUpdateUserAsync(
        @Header("X-Parse-Application-Id") applicationId: String,
        @Header("X-Parse-Session-Token") sessionToken: String,
        @Path("objectId") objectId: String,
        @FieldMap param: Map<String, String>
    ): Deferred<UserUpdatedResponse>
}