package com.mandiri.most.desktop.util.validation

object DataValidation {
    fun containMinusSign(text : String?) : Boolean{
        return text?.startsWith("-") == true
    }
}