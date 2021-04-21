package com.mandiri.most.desktop

import com.mandiri.most.desktop.common.MansekApplication
import com.mandiri.most.desktop.common.StageManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class App : KoinComponent, MansekApplication() {

    private val stageManager: StageManager by inject()

    override fun startApp(stage: Stage?) {

        stageManager.startSampleHome(stage)
    }

//    private fun getFXMLScene(): Scene {
//        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/login.fxml"), resourceBundle)
//        val scene = Scene(fxmlLoader.load(), 1266.0, 768.0)
//        scene.stylesheets.add(
//            App::class.java.getResource("/css/light.css").toExternalForm()
//        )
//        return scene
//    }
}

fun main(args: Array<String>) {
    Application.launch(App::class.java)
}
