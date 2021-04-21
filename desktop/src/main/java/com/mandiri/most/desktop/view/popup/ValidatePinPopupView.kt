package com.mandiri.most.desktop.view.popup

import com.jfoenix.controls.JFXTextField
import com.mandiri.most.desktop.common.PopupManager
import com.mandiri.most.desktop.common.StageManager
import javafx.fxml.FXML
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@OptIn(KoinApiExtension::class)
class ValidatePinPopupView(): KoinComponent {
    private val stageManager: StageManager by inject()

    @FXML
    private val firstTextField: JFXTextField? = null

    @FXML
    private val secondTextField: JFXTextField? = null

    @FXML
    private val thirdTextField: JFXTextField? = null

    @FXML
    private val fourthTextField: JFXTextField? = null

    @FXML
    private val fifthTextField: JFXTextField? = null

    @FXML
    private val sixthTextField: JFXTextField? = null

    /**
     * close popup stage
     */
    @FXML
    fun closeStage() {
        stageManager.closeStage()
    }
}