package com.mandiri.most.core.init

import com.mandiri.most.core.auth.management.SecureKeyValueStore
import com.mandiri.most.core.auth.token.DefaultTokenStore
import com.mandiri.most.core.common.utils.Singleton
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module

object CoreInitializer {

    fun initialize(config: CoreConfiguration): Module {
        log = config.logger ?: NullLogger()
        globalTokenStore.initOnce(config.secureStore)
        globalTokenStore.instance?.also {
            val networkModule = makeNetworkModule(config, it)
            loadKoinModules(listOf(networkModule, makeAPIModule(), makeRepositoryModule()))
        }
        log.i("core initialized")
        return makeUseCasesModule()
    }
}

internal var globalTokenStore =
    Singleton<DefaultTokenStore, SecureKeyValueStore>{ store -> DefaultTokenStore(store) }

internal var log: CoreLogger = NullLogger()
