package com.mandiri.most.core.user.data.source.remote

import com.mandiri.most.core.user.domain.User

fun UserResponseData.asUser() = User(userId, name, password)