package com.mandiri.most.desktop.common.di

import com.mandiri.most.desktop.view.login.LoginViewModel
import org.koin.dsl.module

var viewModelModules = module{
    factory {
        LoginViewModel(get(), get())
    }
}