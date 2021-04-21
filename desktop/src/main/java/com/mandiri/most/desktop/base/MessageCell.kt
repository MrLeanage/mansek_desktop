package com.mandiri.most.desktop.base

import javafx.event.EventHandler
import javafx.scene.SnapshotParameters
import javafx.scene.control.ListCell
import javafx.scene.image.WritableImage
import javafx.scene.input.*
import javafx.scene.layout.AnchorPane
import java.util.*


class MessageCell() : ListCell<String?>() {
    private var anchorPane = AnchorPane()

    override fun updateItem(item: String?, empty: Boolean) {
        super.updateItem(item, empty)
        if (empty || item == null) {
            graphic = null
        } else {
            anchorPane = ListCellData.paneList()[listView.items.indexOf(item)]
            graphic = anchorPane
        }
    }

    init {
        val thisCell: ListCell<*> = this

        //setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        //(Pos.CENTER);
        onDragDetected = EventHandler { event: MouseEvent ->
            if (item == null) {
                return@EventHandler
            }
            val items = listView.items
            val dragboard = startDragAndDrop(TransferMode.MOVE)
            val content = ClipboardContent()
            content.putString(item)
            val snapShot : WritableImage = ListCellData.paneList()[items.indexOf(item)].snapshot(SnapshotParameters(), null)
            dragboard.dragView = snapShot
            dragboard.setContent(content)
            event.consume()
        }
        onDragOver = EventHandler { event: DragEvent ->
            if (event.gestureSource !== thisCell &&
                event.dragboard.hasString()
            ) {
                event.acceptTransferModes(TransferMode.MOVE)
                
            }
            event.consume()
        }
        onDragEntered = EventHandler { event: DragEvent ->
            if (event.gestureSource !== thisCell &&
                event.dragboard.hasString()
            ) {
                opacity = 0.3
            }
        }
        onDragExited = EventHandler { event: DragEvent ->
            if (event.gestureSource !== thisCell &&
                event.dragboard.hasString()
            ) {
                opacity = 1.0
            }
        }
        onDragDropped = EventHandler { event: DragEvent ->
            if (item == null) {
                return@EventHandler
            }
            val db = event.dragboard
            var success = false
            if (db.hasString()) {
                val items = listView.items
                val draggedIdx = items.indexOf(db.string)
                val thisIdx = items.indexOf(item)
                val temp = ListCellData.paneList()[draggedIdx]
                ListCellData.paneList()[draggedIdx] = ListCellData.paneList()[thisIdx]
                ListCellData.paneList()[thisIdx] = temp
                items[draggedIdx] = item
                items[thisIdx] = db.string
                val itemscopy: List<String?> = ArrayList(listView.items)
                listView.items.setAll(itemscopy)
                success = true
            }
            event.isDropCompleted = success
            event.consume()
        }
        onDragDone = EventHandler { obj: DragEvent -> obj.consume() }
    }
}