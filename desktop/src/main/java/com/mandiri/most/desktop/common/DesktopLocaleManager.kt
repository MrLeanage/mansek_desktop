package com.mandiri.most.desktop.common

import com.mandiri.most.core.common.utils.AppLanguage
import com.mandiri.most.core.common.utils.LocaleManager

class DesktopLocaleManager : LocaleManager{
    override val appLanguage: AppLanguage
        get() = AppLanguage.EN
}