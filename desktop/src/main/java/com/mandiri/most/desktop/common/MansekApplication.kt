package com.mandiri.most.desktop.common

import com.mandiri.most.core.init.CoreConfiguration
import com.mandiri.most.core.init.CoreInitializer
import com.mandiri.most.desktop.DesktopConfig
import com.mandiri.most.desktop.common.di.viewModelModules
import com.mandiri.most.desktop.common.infra.DesktopPreferences
import com.mandiri.most.desktop.common.infra.DesktopSecureKeyValueStore
import com.mandiri.most.desktop.common.infra.Log4j2CoreLoggerAdapter
import javafx.application.Application
import javafx.stage.Stage
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

abstract class MansekApplication : Application() {

    abstract fun startApp(stage: Stage?)

    override fun start(primaryStage: Stage?) {
        initKoin()
        startApp(primaryStage)
    }

    private fun initKoin() {
        val preferenceModule = DesktopPreferences()
        startKoin {
            modules(viewModelModules, module {
                single { preferenceModule }
            }, module {
                single {
                    LocaleManager(get())
                }
            }, module {
                single {
                    StageManager(get())
                }
            }
            )
        }
        val config = CoreConfiguration(
            true,
            DesktopConfig.BASE_URL,
            DesktopSecureKeyValueStore(preferenceModule),
            null,
            DesktopLocaleManager(),
            Log4j2CoreLoggerAdapter()
        )
        val exportedModules = CoreInitializer.initialize(config)
        loadKoinModules(exportedModules)
    }
}