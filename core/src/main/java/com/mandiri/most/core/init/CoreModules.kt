package com.mandiri.most.core.init

import com.mandiri.most.core.auth.api.AuthRepository
import com.mandiri.most.core.auth.api.DefaultAuthRepository
import com.mandiri.most.core.auth.api.source.network.AuthAPI
import com.mandiri.most.core.auth.usecase.*
import com.mandiri.most.core.user.data.repository.DummyUserRepository
import com.mandiri.most.core.user.data.source.remote.UserApi
import com.mandiri.most.core.user.domain.repository.UserRepository
import com.mandiri.most.core.user.domain.usecase.Login
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit


internal fun makeAPIModule(): Module {

    return module {
        single { get<Retrofit>().create(AuthAPI::class.java) }
        single { get<Retrofit>().create(UserApi::class.java) } // TODO Example, remove me later
    }
}

internal fun makeRepositoryModule(): Module {

    return module {
        factory<AuthRepository> { DefaultAuthRepository(get()) }
        factory<UserRepository> { DummyUserRepository() } // TODO Example, remove me later
    }
}

internal fun makeUseCasesModule(): Module {

    return module {
        single { ObservableAuthStatusUseCase(get(), get()) }
        factory { ValidatePinUseCase(get()) }
        factory { ForgotPasswordUseCase(get()) }
        factory { ValidatePasswordResetUseCase(get()) }
        factory { ResetPasswordUseCase(get()) }
        factory { GetStoredPinUseCase() }
        factory { Login(get()) } // TODO Example, remove me later
        factory { ResetPinUseCase(get()) }
        factory { ValidatePinResetUseCase(get()) }
        factory { ForgotPinUseCase(get()) }
    }
}