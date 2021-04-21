package com.mandiri.most.desktop.util.validation

import com.jfoenix.controls.JFXTimePicker
import com.mandiri.most.desktop.common.StageManager
import javafx.scene.control.*
import javafx.scene.image.ImageView
import javafx.scene.input.KeyEvent
import javafx.util.Callback
import java.time.LocalDate
import java.util.regex.Pattern

object InputValidation {

    //Checking not empty for Text Fields with same name
    fun textValueNotEmpty(textValue: String?): Boolean {

        //returning text fields empty as default value
        return textValue != null && textValue.isNotEmpty()
    }

    //email validation
    val VALID_EMAIL = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)
    fun isValidEmail(emailStr: String?): Boolean {
        var returnVal = false
        val matcher = VALID_EMAIL.matcher(emailStr)
        if (matcher.find()) {
            returnVal = true
        }
        return returnVal
    }

    //checking for maximum length
    fun isValidMaximumLength(data: String, maxLength: String): Boolean = data.length <= maxLength.length

}