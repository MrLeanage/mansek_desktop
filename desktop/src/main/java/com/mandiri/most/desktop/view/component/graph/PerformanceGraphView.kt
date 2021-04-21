package com.mandiri.most.desktop.view.component.graph

import com.mandiri.most.desktop.common.StageManager
import javafx.fxml.FXML
import javafx.scene.chart.*
import javafx.scene.chart.XYChart.Series
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(KoinApiExtension::class)
class PerformanceGraphView(): KoinComponent {
    private val stageManager: StageManager by inject()

    @FXML
    private var performanceChart: BarChart<String, Number>? = null

    @FXML
    private val xAxisChart: CategoryAxis? = null

    @FXML
    private val yAxisChart: NumberAxis? = null

    fun initialize() {
        setChartData()
    }

    /**
     * load chart data
     */
    private fun setChartData() {

        val revenue: Series<String, Number> = Series<String, Number>()

        revenue.data.add(XYChart.Data(" Q1\n2019", 475.00f))
        revenue.data.add(XYChart.Data(" Q2\n2019", 550.00f))
        revenue.data.add(XYChart.Data(" Q3\n2019", 425.00f))
        revenue.data.add(XYChart.Data(" Q4\n2019", 475.00f))

        val earning: Series<String, Number> = Series<String, Number>()

        earning.data.add(XYChart.Data(" Q1\n2019", 275.00f))
        earning.data.add(XYChart.Data(" Q2\n2019", 310.00f))
        earning.data.add(XYChart.Data(" Q3\n2019", 190.00f))
        earning.data.add(XYChart.Data(" Q4\n2019", 210.00f))



        performanceChart?.data
        performanceChart?.data?.addAll(revenue)
        performanceChart?.data?.addAll(earning)

    }

    /**
     * close stage on close icon click
     */
    @FXML
    fun closeStage() {
        stageManager.closeStage()
    }
}