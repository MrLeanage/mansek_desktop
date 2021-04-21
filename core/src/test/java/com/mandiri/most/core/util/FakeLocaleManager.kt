package com.mandiri.most.core.util

import com.mandiri.most.core.common.utils.AppLanguage
import com.mandiri.most.core.common.utils.LocaleManager

class FakeLocaleManager : LocaleManager {
    override val appLanguage: AppLanguage
        get() = AppLanguage.EN
}