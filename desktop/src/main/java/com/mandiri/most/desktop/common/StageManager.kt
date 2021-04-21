package com.mandiri.most.desktop.common

import com.mandiri.most.desktop.App
import com.mandiri.most.desktop.view.popup.GeneralErrorPopupView
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.io.IOException
import java.lang.NullPointerException
import java.util.logging.Level
import java.util.logging.Logger

class StageManager(private val localeManager: LocaleManager) {

    var mainStage : Stage? = null

    /*
    fun startLoginScreen(stage: Stage?){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/login/login.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load(), 641.0, 375.0)
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )
        stage?.initStyle(StageStyle.UNDECORATED)
        stage?.scene = scene
        stage?.show()
        this.mainStage = stage
    }
     */

    fun startLoginScreen(){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/login/login.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )
        //mainStage?.initStyle(StageStyle.UNDECORATED)
        mainStage?.scene = scene
        mainStage?.show()
    }

    fun startSampleHome(stage: Stage?){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/sampleHome.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )
        stage?.initStyle(StageStyle.UNDECORATED)
        stage?.scene = scene
        stage?.show()
        this.mainStage = stage
    }

    fun closeStage(){
        mainStage?.close()
    }

    fun loadPINPopUp(){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/popup/validatePinPopup.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )

        mainStage?.scene = scene
        mainStage?.show()
    }

    fun loadPerformanceGraph(){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/component/graph/performanceGraph.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )

        mainStage?.scene = scene
        mainStage?.show()
    }

    fun loadCustomDateRange(){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/component/date/customDateRange.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )

        mainStage?.scene = scene
        mainStage?.show()
    }

    fun loadMessageTray(){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/component/messageTray/messageTray.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )

        mainStage?.scene = scene
        mainStage?.show()
    }

    fun loadMyStock(){
        val fxmlLoader = FXMLLoader(javaClass.getResource("/layout/component/myStock/myStockTable.fxml"),
            localeManager.getResourceBundle())
        val scene = Scene(fxmlLoader.load())
        scene.stylesheets.add(
            App::class.java.getResource("/css/light.css").toExternalForm()
        )

        mainStage?.scene = scene
        mainStage?.show()
    }

}