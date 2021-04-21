package com.mandiri.most.core.user.data.repository

import com.mandiri.most.core.common.domain.exception.AuthorizationFailedException
import com.mandiri.most.core.user.domain.LoginData
import com.mandiri.most.core.user.domain.User
import com.mandiri.most.core.user.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Single

class DummyUserRepository : UserRepository {

    private val dummyUser = User("jack", "Jack Ryan", "password")

    override fun login(loginData: LoginData): Single<User> {
        return if (loginData.id == dummyUser.id && loginData.password == dummyUser.password) {
            Single.just(dummyUser)
        } else {
            Single.error(AuthorizationFailedException())
        }
    }
}