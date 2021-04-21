package com.mandiri.most.desktop.base

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXMLLoader
import javafx.scene.layout.AnchorPane

object ListCellData {
    var dragStatus = false
    var currentmMessage: Message? = null

    /**
     * setting id for each cell on listView
     * @return paneID as list of generated IDs
     */
    fun paneIDList() : ObservableList<String>{
        val paneId = FXCollections.observableArrayList<String>()
        for(i in 1..10){
            paneId.add("message ID: "+i)
        }
        return paneId
    }

    /**
     * Generating list of messages using listModel component for ListView
     * @return observableList as list of generated messages for ListView
     */
    fun paneList(): ObservableList<AnchorPane>{
        val observableList = FXCollections.observableArrayList<AnchorPane>()
        for(i in 1..10) {
            currentmMessage = Message("Message Head "+i , "Heaasge Body :"+i)
            val listModel =
                FXMLLoader.load(javaClass.getResource("/layout/component/messageTray/listModel.fxml")) as AnchorPane
            observableList.add(listModel)

        }
        return observableList
    }

}