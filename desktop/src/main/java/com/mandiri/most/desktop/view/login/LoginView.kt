package com.mandiri.most.desktop.view.login

import com.jfoenix.controls.JFXCheckBox
import com.jfoenix.controls.JFXPasswordField
import com.jfoenix.controls.JFXTextField
import com.mandiri.most.desktop.common.LocaleManager
import com.mandiri.most.desktop.common.StageManager
import com.mandiri.most.desktop.common.utils.Utility
import com.mandiri.most.desktop.util.validation.InputValidation
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.input.KeyEvent
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

@OptIn(KoinApiExtension::class)
class LoginView : KoinComponent {

    private val viewModel: LoginViewModel by inject()

    private val localeManager: LocaleManager by inject()

    private val stageManager: StageManager by inject()

    @FXML
    var btnLogin: Button? = null

    @FXML
    var emailTextField: JFXTextField? = null

    @FXML
    var passwordField: JFXPasswordField? = null

    @FXML
    var rememberCheckBox: JFXCheckBox? = null

    @FXML
    var passwordTextField: JFXTextField ? = null

    fun initialize() {
        //btnLogin?.textProperty()?.bind(viewModel.message)
        passwordTextField!!.isVisible = false
    }

    /**
     * calling login function from LoginViewModel
     */
    @FXML
    fun login() {
        //viewModel.login("meitri.delfiza+dummy02@icehousecorp.com","Ironman1")
        viewModel.login(emailTextField!!.text, passwordField!!.text, stageManager)
        localeManager.setLocal(Locale("in", "in"))
        //stageManager.refreshScreen()
    }
    /**
     * setting password from paswordField to passwordTextField on keyRelease
     */
    @FXML
    fun setPasswordToTextBox() {
        passwordTextField?.text = passwordField!!.text
    }
    /**
     * setting password from passwordTextField to paswordField on keyRelease
     */
    @FXML
    fun setPasswordToPasswordField() {
        passwordField?.text = passwordTextField!!.text
    }

    /**
     * check visibility of password field,
     * If field is not visible then, hide passwordField, show passwordTextfield
     * else hide passwordtextField, show passwordField
     */
    @FXML
    fun showPassword() {
        if(!passwordTextField?.isVisible()!!){
            passwordField?.isVisible = false
            passwordTextField?.isVisible = true
        }else{
            passwordTextField?.isVisible = false
            passwordField?.isVisible = true
        }
    }

    /**
     * close stage on close icon click
     */
    @FXML
    fun closeStage() {
        stageManager.closeStage()
    }

    /**
     * checking number of characters entered, pass to modelView
     *
     */
    @FXML
    fun checkCharacterLimit(keyEvent: KeyEvent?){

        if(viewModel.checkEmailCharacterLimit(emailTextField!!.text)){
            emailTextField?.isEditable = Utility.disableKeyConsume(keyEvent)
        }
        if(viewModel.checkPasswordCharacterLimit(passwordField!!.text)){
            passwordField?.isEditable = Utility.disableKeyConsume(keyEvent)
        }
        if(viewModel.checkPasswordCharacterLimit(passwordTextField!!.text)){
            passwordTextField?.isEditable = Utility.disableKeyConsume(keyEvent)
        }
    }
}