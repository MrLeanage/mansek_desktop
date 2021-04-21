package com.mandiri.most.desktop.view.component.graph

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.chart.BarChart
import javafx.scene.chart.CategoryAxis
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import javafx.scene.chart.XYChart.Series
import org.koin.core.component.KoinApiExtension
import java.net.URL
import java.util.*

@OptIn(KoinApiExtension::class)
class Barchart : Initializable {

    override fun initialize(location: URL, resources: ResourceBundle) {
        setChartData()
    }

    @FXML
    private var performanceChart: BarChart<String, Number>? = null

    @FXML
    private val xAxisChart: CategoryAxis? = null

    @FXML
    private val yAxisChart: NumberAxis? = null
    private fun setChartData() {
        val revenue: Series<String, Number> = Series<String, Number>()

        revenue.data.add(XYChart.Data(" Q1\n2019", 475.00f))
        revenue.data.add(XYChart.Data(" Q2\n2019", 525.00f))
        revenue.data.add(XYChart.Data(" Q3\n2019", 425.00f))
        revenue.data.add(XYChart.Data(" Q4\n2019", 475.00f))

        val earning: Series<String, Number> = Series<String, Number>()

        earning.data.add(XYChart.Data(" Q1\n2019", 275.00f))
        earning.data.add(XYChart.Data(" Q2\n2019", 310.00f))
        earning.data.add(XYChart.Data(" Q3\n2019", 190.00f))
        earning.data.add(XYChart.Data(" Q4\n2019", 210.00f))

        performanceChart!!.verticalGridLinesVisible = false
        performanceChart?.isVerticalZeroLineVisible = false // This property isn't working on graph
        performanceChart?.data
        performanceChart?.data?.addAll(revenue)
        performanceChart?.data?.addAll(earning)
    }
}