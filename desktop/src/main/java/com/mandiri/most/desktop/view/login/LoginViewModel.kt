package com.mandiri.most.desktop.view.login

import com.mandiri.most.core.auth.usecase.*
import com.mandiri.most.desktop.common.LocaleManager
import com.mandiri.most.desktop.common.PopupManager
import com.mandiri.most.desktop.common.StageManager
import com.mandiri.most.desktop.util.validation.InputValidation
import com.mandiri.most.desktop.view.popup.GeneralErrorPopupView
import javafx.beans.property.ReadOnlyStringWrapper
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import java.io.IOException
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger

class LoginViewModel(
    private val observableAuthStatusUseCase: ObservableAuthStatusUseCase,
    private val localeManager: LocaleManager,

) {

    val emailTextLimit : Int = 20
    val passwordTextLimit : Int = 30
    val message: ReadOnlyStringWrapper = ReadOnlyStringWrapper(localeManager.
    getResourceBundle().getString("hello_world"))

    init {
        observableAuthStatusUseCase.execute(Unit).subscribe {
        }
    }

    //todo this is demo
    /**
     * if data validate success, binding data to API
     * if success, close stage
     * failed, show popup
     */
    fun login(email:String, password:String, stageManager : StageManager) {
        val userCredential = UserCredential(email,password)

        if(inputValidation(email, password)){
            val observable = observableAuthStatusUseCase.send(AuthAction.LoginAsUser(userCredential))
            observable?.subscribe(
                {
                    println(observable.toString())
                    stageManager.closeStage()

                },
                {
                    println(observable.toString())
                    PopupManager.loadGeneralErrorPopupStage("Authentication Failed", "Message in for API Fail")
                }
            )
        }
    }

    /**
     * validating inputs from User Interface
     */
    private fun inputValidation(emailText: String?, passwordText: String?): Boolean {
        return InputValidation.textValueNotEmpty(emailText) && InputValidation.textValueNotEmpty(passwordText) && InputValidation.isValidEmail(emailText)
    }
    /**
     * return true if character count greater than given limit
     */
    fun checkEmailCharacterLimit(stringValue : String?): Boolean{
        return stringValue!!.length >= emailTextLimit
    }

    /**
     * return true if character count greater than given limit
     */
    fun checkPasswordCharacterLimit(stringValue : String?): Boolean{
        return stringValue!!.length > passwordTextLimit
    }
}