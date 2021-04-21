package com.mandiri.most.desktop.view

import com.mandiri.most.desktop.common.StageManager
import javafx.fxml.FXML
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
class SampleHomeView : KoinComponent{
    private val stageManager: StageManager by inject()

    fun initialize(){
    }

    @FXML
    fun closeStage() {
        stageManager.closeStage()
    }

    @FXML
    fun loadLogin() {
        stageManager.startLoginScreen()
    }

    @FXML
    fun loadPerformanceGraph() {
        stageManager.loadPerformanceGraph()
    }

    @FXML
    fun loadValidationPIN() {
        stageManager.loadPINPopUp()
    }

    @FXML
    fun loadCustomDateRange() {
        stageManager.loadCustomDateRange()
    }

    @FXML
    fun loadMessageTray() {
        stageManager.loadMessageTray()
    }

    @FXML
    fun loadMyStock() {
        stageManager.loadMyStock()
    }
}