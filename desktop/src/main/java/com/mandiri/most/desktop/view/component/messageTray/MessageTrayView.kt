package com.mandiri.most.desktop.view.component.messageTray

import com.jfoenix.controls.JFXListView
import com.mandiri.most.desktop.base.JFXMessageCell
import com.mandiri.most.desktop.base.MessageCell
import com.mandiri.most.desktop.base.ListCellData
import javafx.fxml.FXML
import javafx.scene.control.ListView
import javafx.util.Callback
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent


@OptIn(KoinApiExtension::class)
class MessageTrayView(): KoinComponent {

    @FXML
    private var messageTrayList: ListView<String>? = null

    @FXML
    private var jfxListView: JFXListView<String>? = null

    fun initialize() {
        setMessageTray()
    }

    /**
     * setting callback methods on ListViews
     */
    @FXML
    private fun setMessageTray() {
        //Fedding data to Customized JFoenix ListView
        jfxListView?.cellFactory = Callback { param: ListView<String>? -> JFXMessageCell() }
        jfxListView?.items?.addAll(ListCellData.paneIDList())

        //Feeding Data to customized built in ListView
        messageTrayList?.cellFactory = Callback { param : ListView<String>? -> MessageCell() }
        messageTrayList?.items?.addAll(ListCellData.paneIDList())
    }
}
