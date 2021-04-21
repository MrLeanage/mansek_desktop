package com.mandiri.most.core.user.domain.repository

import com.mandiri.most.core.user.domain.LoginData
import com.mandiri.most.core.user.domain.User
import io.reactivex.rxjava3.core.Single

interface UserRepository {

    fun login(loginData: LoginData): Single<User>
}