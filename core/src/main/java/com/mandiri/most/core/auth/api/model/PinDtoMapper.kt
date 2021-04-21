package com.mandiri.most.core.auth.api.model

import com.mandiri.most.core.common.extension.date.DateFormat
import com.mandiri.most.core.common.extension.date.toDate

fun ForgotPinResponse.asRequestExpiration() =
    RequestExpiration(expiresAt.toDate(DateFormat.TIME_STAMP_FORMAT))