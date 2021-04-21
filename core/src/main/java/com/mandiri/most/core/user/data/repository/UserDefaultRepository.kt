package com.mandiri.most.core.user.data.repository

import com.mandiri.most.core.common.domain.exception.AuthorizationFailedException
import com.mandiri.most.core.user.data.source.remote.LoginRequest
import com.mandiri.most.core.user.data.source.remote.UserApi
import com.mandiri.most.core.user.data.source.remote.asUser
import com.mandiri.most.core.user.domain.LoginData
import com.mandiri.most.core.user.domain.User
import com.mandiri.most.core.user.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Single

class UserDefaultRepository(
    private val userApi: UserApi
) : UserRepository {

    override fun login(loginData: LoginData): Single<User> {
        return userApi.login(LoginRequest(loginData.id, loginData.password)).flatMap {
            val data = it.data
            if (data != null) {
                Single.just(data.asUser())
            } else {
                Single.error(AuthorizationFailedException())
            }
        }
    }
}