package com.mandiri.most.desktop.common

import com.mandiri.most.desktop.view.popup.GeneralErrorPopupView

import com.mandiri.most.desktop.view.popup.ValidatePinPopupView

import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

object PopupManager {

    val stage = Stage()

    /**
     * popup stage for General Error popup
     */

    fun popupStage(title : String?, description : String?) {
    }
    fun loadGeneralErrorPopupStage(title : String?, description : String?){

        val loader = FXMLLoader()

        loader.location = javaClass.getResource("/layout/popup/generalErrorPopup.fxml")
        try {
            loader.load<Any>()
        } catch (ex: IOException) {
            Logger.getLogger(GeneralErrorPopupView::class.java.name).log(Level.SEVERE, null, ex)
        }

        val generalErrorPopupView: GeneralErrorPopupView = loader.getController()
        generalErrorPopupView.initialize(title, description)
        val rootParent = loader.getRoot<Parent>()

        stage.scene = Scene(rootParent)
        stage.initStyle(StageStyle.UNDECORATED)
        stage.isResizable = false
        stage.showAndWait()
    }

    /**
     * popup stage for Validate Pin popup
     */
    fun loadValidatePinPopupStage(){
        val loader = FXMLLoader()

        loader.location = javaClass.getResource("/layout/popup/validatePinPopup.fxml")
        try {
            loader.load<Any>()
        } catch (ex: IOException) {
            Logger.getLogger(ValidatePinPopupView::class.java.name).log(Level.SEVERE, null, ex)
        }

        val validatePinPopupView: ValidatePinPopupView = loader.getController()
        val rootParent = loader.getRoot<Parent>()

        stage.scene = Scene(rootParent)
        stage.initStyle(StageStyle.UNDECORATED)
        stage.isResizable = false
        stage.showAndWait()
    }

    /**
     * closing popup Stage
     */
    fun closePopup(){
        println("Called")
        stage.close()
    }
}