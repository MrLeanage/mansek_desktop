package com.mandiri.most.core.auth.api.model

import com.mandiri.most.core.auth.usecase.UserCredential

fun UserCredential.toUserCredentialRequest() = UserCredentialRequest(email, password)