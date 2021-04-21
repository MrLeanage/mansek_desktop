package com.mandiri.most.desktop.view.component.messageTray

import com.mandiri.most.desktop.base.ListCellData
import javafx.event.Event
import javafx.fxml.FXML
import javafx.scene.control.Label

import javafx.scene.shape.Circle
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent

@OptIn(KoinApiExtension::class)
class ListModelView(): KoinComponent {


    @FXML
    private val removeIcon: Circle? = null

    @FXML
    private val titleLabel: Label? = null

    @FXML
    private val messageLabel: Label? = null


    fun initialize() {
        setMessage()
    }

    private fun setMessage(){
        titleLabel?.text = ListCellData.currentmMessage?.messageHeading
        messageLabel?.text = ListCellData.currentmMessage?.messageInfo
    }

    /**
     * Setting drag event status to ListCellData
     * @param event getting event performed on the function
     */
    @FXML
    private fun getDrag(event: Event){
        if(event.eventType.toString() == "DRAG_DETECTED")
            ListCellData.dragStatus = true
    }
}