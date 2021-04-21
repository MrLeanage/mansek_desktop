package com.mandiri.most.desktop.view.component.date

import com.jfoenix.controls.JFXTextField
import com.mandiri.most.desktop.common.utils.Utility
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.DateCell
import javafx.scene.control.DatePicker
import javafx.scene.control.skin.DatePickerSkin
import javafx.scene.layout.BorderPane
import javafx.util.Callback
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.MonthDay
import java.util.*


@OptIn(KoinApiExtension::class)
class CustomDateRangeView : KoinComponent {

    private var clickCounter : Int = 0

    private var firstDate : String? = null

    private var startDate : String? = LocalDate.now().toString()

    private var endDate : String? = LocalDate.now().toString()

    var selectedDates : LocalDate? = LocalDate.now()

    @FXML
    private var fromTextField: JFXTextField? = null

    @FXML
    private var toTextField: JFXTextField? = null


    private var datePicker :DatePicker? = null;

    @FXML
    private var borderPane : BorderPane? = null

    fun initialize() {
        setData()
    }

    /**
     * setting the customized date picker to border pane
     */
    private fun setData(){
        datePicker = DatePicker(LocalDate.now())
        setDateRange()
        val datePickerSkin = DatePickerSkin(datePicker)
        val popupContent: Node = datePickerSkin.popupContent
        borderPane?.center = popupContent
    }

    /**
     * setting the Date from customized date picker text fields
     */
    @FXML
    private fun calendarDateSetter(){
        clickCounter ++
        val sdformat = SimpleDateFormat("yyyy-MM-dd")
        var firstLocalDate: Date? = null
        var secondLocalDate: Date? = null

        //Checking the number of times that clicked for dates in the calendar
        if(clickCounter < 2){
            firstDate = datePicker?.value.toString()
            fromTextField?.text = firstDate
            selectedDates = datePicker?.value
        }else {
            val secondDate = datePicker?.value.toString()

            try {
                firstLocalDate = sdformat.parse(firstDate)
                secondLocalDate = sdformat.parse(secondDate)
            } catch (e: ParseException) {
            }
            if (firstLocalDate!!.compareTo(secondLocalDate) > 0) {
                selectedDates = datePicker?.value
                fromTextField?.text = secondDate
                toTextField?.text = firstDate
                startDate= secondDate
                endDate = firstDate
            }
            if (firstLocalDate.compareTo(secondLocalDate) < 0) {
                selectedDates = datePicker?.value
                fromTextField?.text = firstDate
                toTextField?.text = secondDate
                startDate = firstDate
                endDate = secondDate
            }
            setData()
        }
    }

    /**
     * setting the customized date picker to border pane
     */
    private fun setDateRange() {
        val dayCellFactory: Callback<DatePicker, DateCell> =
            Callback {
                object : DateCell() {
                    override fun updateItem(item: LocalDate, empty: Boolean) {
                        super.updateItem(item, empty)
                        if(MonthDay.from(item) == MonthDay.of(LocalDate.now().monthValue, LocalDate.now().dayOfMonth)){
                            style = "-fx-background-color: #ffffff; -fx-text-fill: #000000"
                        }
                        if(startDate != endDate){
                            for (localDate in Utility.getListOfDates(startDate, endDate)) {
                                if (MonthDay.from(item) == MonthDay.of(localDate.monthValue,localDate.dayOfMonth)) {
                                    style = "-fx-background-color: #063F77; -fx-text-fill: #ffffff"
                                }
                            }
                        }
                    }
                }
            }
        datePicker?.dayCellFactory = dayCellFactory
    }
}