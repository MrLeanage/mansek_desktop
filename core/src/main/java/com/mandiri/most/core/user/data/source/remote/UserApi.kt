package com.mandiri.most.core.user.data.source.remote

import io.reactivex.rxjava3.core.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("user/login")
    fun login(@Body request: LoginRequest): Single<UserResponse>
}