package com.mandiri.most.desktop.view.popup

import com.mandiri.most.desktop.common.PopupManager
import com.mandiri.most.desktop.common.StageManager
import javafx.fxml.FXML
import javafx.scene.control.Label
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
class GeneralErrorPopupView() : KoinComponent {
    private val stageManager: StageManager by inject()

    @FXML
    var  titleLabel : Label? = null

    @FXML
    var descriptionLabel : Label? = null

    fun initialize(title : String?,description : String?){
        titleLabel?.text = title
        descriptionLabel?.text = description
    }

    /**
     * close popup stage
     */
    @FXML
    fun closeStage() {
        PopupManager.closePopup()
    }

}