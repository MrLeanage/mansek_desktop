package com.mandiri.most.core.user.domain.usecase

import com.mandiri.most.core.common.domain.SingleUseCase
import com.mandiri.most.core.user.domain.LoginData
import com.mandiri.most.core.user.domain.User
import com.mandiri.most.core.user.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Single

class Login(private val userRepository: UserRepository) : SingleUseCase<Login.Params, User>() {

    override fun build(params: Params): Single<User> = params.run {
        return userRepository.login(LoginData(userId, password))
    }

    data class Params(
        val userId: String,
        val password: String
    )
}