package com.mandiri.most.desktop.view.component.myStock


import com.mandiri.most.desktop.base.Stock
import com.mandiri.most.desktop.base.TableComponent
import com.mandiri.most.desktop.util.validation.DataValidation
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.TextAlignment
import javafx.util.Callback
import javafx.util.Duration
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent


@OptIn(KoinApiExtension::class)
class MyStockView() : KoinComponent {

    @FXML
    private var stockTable: TableView<Stock>? = null

    @FXML
    private var symbolColumn: TableColumn<Stock, TableComponent>? = null

    @FXML
    private var lotShareColumn: TableColumn<Stock, TableComponent>? = null

    @FXML
    private var availableColumn: TableColumn<Stock, String>? = null

    @FXML
    private var priceColumn: TableColumn<Stock, TableComponent>? = null

    @FXML
    private var gainLossColumn: TableColumn<Stock, TableComponent>? = null

    @FXML
    private var marketColumn: TableColumn<Stock, TableComponent>? = null

    @FXML
    private var cutValueColumn: TableColumn<Stock, String>? = null

    @FXML
    private val loaderAnchorPane: AnchorPane? = null

    @FXML
    private val loaderLabel: Label? = null

    fun initialize() {
        loadTableData()
    }

    /**
     * settting new callbacks and setting data to table
     */
    fun loadTableData() {

//        val animationFX  = FadeOutRight(loaderLabel)
//        animationFX.setCycleCount(5).setDelay(Duration.valueOf("400ms")).play()
//        animationFX.setOnFinished { event: ActionEvent? ->
//            loaderAnchorPane?.setVisible(false)
//        }

        loaderAnchorPane?.setVisible(false)
        val productsData: ObservableList<Stock> = FXCollections.observableArrayList()

        productsData.add(
            Stock(
                "ADRO",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "ANTM",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "-212",
                "-2.50%",
                7300000,
                4000000,
                0.25
            )
        )
        productsData.add(
            Stock(
                "KAEF",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "BBRI",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.28
            )
        )
        productsData.add(
            Stock(
                "BBCA",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "BRAE",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "ABCD",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "GEFD",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.40
            )
        )
        productsData.add(
            Stock(
                "HIGJ",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "LMNO",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "PQRS",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "TUVW",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.64
            )
        )
        productsData.add(
            Stock(
                "XYZA",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "BCDE",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "FGHI",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )
        productsData.add(
            Stock(
                "JKLM",
                "Adri Energy Tbk",
                50,
                5000,
                30,
                1470,
                8000.00,
                "+660",
                "82.50%",
                7300000,
                4000000,
                0.24
            )
        )

        columnTitleTableComponentClassWrapper(symbolColumn, "CENTER_LEFT")
        columnTitleTableComponentClassWrapper(lotShareColumn, "CENTER_RIGHT")
        columnTitleStringClassWrapper(availableColumn, "CENTER_RIGHT")
        columnTitleTableComponentClassWrapper(priceColumn, "CENTER_RIGHT")
        columnTitleTableComponentClassWrapper(gainLossColumn, "CENTER_RIGHT")
        columnTitleTableComponentClassWrapper(marketColumn, "CENTER_RIGHT")
        columnTitleStringClassWrapper(cutValueColumn, "CENTER_RIGHT")

        symbolColumn?.cellValueFactory = PropertyValueFactory<Stock, TableComponent>("mSymbol")
        lotShareColumn?.setCellValueFactory(PropertyValueFactory<Stock, TableComponent>("mLotShare"))
        availableColumn?.setCellValueFactory(PropertyValueFactory<Stock, String>("mAvailableLastPrice"))
        priceColumn?.setCellValueFactory(PropertyValueFactory<Stock, TableComponent>("mAveragePrice"))
        gainLossColumn?.setCellValueFactory(PropertyValueFactory<Stock, TableComponent>("mGainLost"))
        marketColumn?.setCellValueFactory(PropertyValueFactory<Stock, TableComponent>("mMarketStockValue"))
        cutValueColumn?.setCellValueFactory(PropertyValueFactory<Stock, String>("mHaircutValue"))


        symbolColumn?.setCellFactory(doubleCellCallbackFactory("CENTER_LEFT", "#21272c", "#738495"))
        lotShareColumn?.setCellFactory(doubleCellCallbackFactory("CENTER_RIGHT", "#21272c", "#738495"))
        availableColumn?.setCellFactory(singleCellCallbackFactoryStrng("CENTER_RIGHT", "#21272c"))
        priceColumn?.setCellFactory(doubleCellCallbackFactory("CENTER_RIGHT", "#151a20", "#738495"))
        gainLossColumn?.setCellFactory(doubleCellCallbackFactory("CENTER_RIGHT", "#2ca779", "#2ca779"))
        marketColumn?.setCellFactory(doubleCellCallbackFactory("CENTER_RIGHT", "#21272c", "#738495"))
        cutValueColumn?.setCellFactory(singleCellCallbackFactoryStrng("CENTER_RIGHT", "#151a20"))
        stockTable?.items = null
        stockTable?.setItems(productsData)

    }

    /**
     * setting column CallbackFactory to Dual data Stream(Component)
     * @param position to adjust VBox content Position
     * @param headColor to set Text color of head Stream
     * @param msgColor to set Text color of message Stream
     * @return Callback of Table Column
     */
    fun doubleCellCallbackFactory(
        position: String,
        headColor: String,
        msgColor: String
    ): Callback<TableColumn<Stock?, TableComponent?>, TableCell<Stock?, TableComponent?>> {
        val parentCellFactory: Callback<TableColumn<Stock?, TableComponent?>, TableCell<Stock?, TableComponent?>> =
            Callback<TableColumn<Stock?, TableComponent?>, TableCell<Stock?, TableComponent?>> {
                object : TableCell<Stock?, TableComponent?>() {
                    override fun updateItem(item: TableComponent?, empty: Boolean) {
                        super.updateItem(item, empty)

                        if (empty) {
                            graphic = null
                        } else {
                            val vbox = VBox()
                            vbox.alignment = Pos.valueOf(position)
                            val headTextList: List<String>? = item?.headComponent?.split("\n")
                            val msgTextList: List<String>? = item?.msgComponent?.split("\n")

                            headTextList?.indices?.forEach { i ->
                                val headTextLabel = Label(headTextList[i])
                                val msgTextLabel = Label(msgTextList?.get(i))

                                if (DataValidation.containMinusSign(item.headComponent))
                                    headTextLabel.style = "-fx-text-fill: #f14d4e;-fx-font-weight: bold;"
                                else
                                    headTextLabel.style = "-fx-text-fill:" + headColor + ";-fx-font-weight: bold;"
                                if (DataValidation.containMinusSign(item.msgComponent))
                                    msgTextLabel.style = "-fx-text-fill: #f14d4e;-fx-font-weight: bold;"
                                else
                                    msgTextLabel.style = "-fx-text-fill:" + msgColor + ";-fx-font-weight: bold;"
                                vbox.children.addAll(headTextLabel, msgTextLabel)
                            }
                            graphic = vbox
                        }
                    }
                }
            }
        return parentCellFactory
    }

    /**
     * setting column CallbackFactory to single data Stream
     * @param position to adjust VBox content Position
     * @param headColor to set Text color of head Stream
     * @return Callback of Table Column
     */
    fun singleCellCallbackFactoryStrng(
        position: String,
        headColor: String
    ): Callback<TableColumn<Stock?, String?>, TableCell<Stock?, String?>> {
        val parentCellFactory: Callback<TableColumn<Stock?, String?>, TableCell<Stock?, String?>> =
            Callback<TableColumn<Stock?, String?>, TableCell<Stock?, String?>> {
                object : TableCell<Stock?, String?>() {
                    override fun updateItem(item: String?, empty: Boolean) {
                        super.updateItem(item, empty)
                        if (empty) {
                            graphic = null
                        } else {
                            val vbox = VBox()
                            vbox.alignment = Pos.valueOf(position)
                            val headTextList: List<String>? = item?.split("\n")
                            headTextList?.indices?.forEach { i ->
                                val headTextLabel = Label(headTextList[i])
                                val msgTextLabel = Label()

                                if (DataValidation.containMinusSign(item))
                                    headTextLabel.style = "-fx-text-fill: #f14d4e;-fx-font-weight: bold;"
                                else
                                    headTextLabel.style = "-fx-text-fill:" + headColor + ";-fx-font-weight: bold;"
                                vbox.children.addAll(headTextLabel, msgTextLabel)
                            }
                            graphic = vbox
                        }
                    }
                }
            }
        return parentCellFactory
    }

    /**
     * Wrapper for table column
     * @param column getting column to add wrpper heading label
     * @param position to set text alignment
     */
    private fun columnTitleStringClassWrapper(column: TableColumn<Stock, String>?, position: String) {
        val label = Label(column?.text)
        val stack = StackPane()

        label.style = "-fx-padding: 8px;"
        label.isWrapText = true
        label.alignment = Pos.valueOf(position)
        label.textAlignment = TextAlignment.CENTER

        stack.children.add(label)
        stack.prefWidthProperty().bind(column?.widthProperty()?.subtract(5))
        label.prefWidthProperty().bind(stack.prefWidthProperty())

        column?.text = null
        column?.graphic = stack
    }

    /**
     * Wrapper for table column
     * @param column getting column to add wrpper heading label
     * @param position to set text alignment
     */
    private fun columnTitleTableComponentClassWrapper(column: TableColumn<Stock, TableComponent>?, position: String) {
        val label = Label(column?.text)
        val stack = StackPane()

        label.style = "-fx-padding: 8px;"
        label.isWrapText = true
        label.alignment = Pos.valueOf(position)
        label.textAlignment = TextAlignment.CENTER

        stack.children.add(label)
        stack.prefWidthProperty().bind(column?.widthProperty()?.subtract(5))
        label.prefWidthProperty().bind(stack.prefWidthProperty())

        column?.text = null
        column?.graphic = stack
    }
}