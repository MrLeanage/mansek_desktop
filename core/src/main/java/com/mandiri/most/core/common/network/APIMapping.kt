package com.mandiri.most.core.common.network

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class MostAPIException : IOException() {
    class SystemError(val statusCode: Int, val responseMessage: String?) : MostAPIException()
    class InvalidCredentials(val responseMessage: String?) : MostAPIException()
    object DataNotFound : MostAPIException()
    object Timeout : MostAPIException()
    object ForcedLogout : MostAPIException()
    object NoConnection : MostAPIException()
    class Unknown(val responseMessage: String?) : MostAPIException()
    class Unexpected(val responseMessage: String?) :
        MostAPIException() // Indicates a bug in error conversion

    object BadToken : MostAPIException()
    object Unauthorized : MostAPIException()
    object WrongPassword : MostAPIException()
    object InvalidBody : MostAPIException()
    object BadInput : MostAPIException()
    object WrongOtp : MostAPIException()
    object OtpNotFound : MostAPIException()
}

fun Throwable.toMostAPIError(): MostAPIException {
    return when (this) {
        is MostAPIException -> this
        is APIException -> this.toMostAPIError()
        is SocketTimeoutException -> MostAPIException.Timeout
        is ConnectException, is UnknownHostException -> MostAPIException.NoConnection
        else -> MostAPIException.Unexpected("Unexpected throwable type, not APIException, got: $this")
    }
}

fun APIException.toMostAPIError(): MostAPIException {
    if (this.error.status >= 500) {
        return MostAPIException.SystemError(this.error.status, this.error.message)
    }
    when (this.error.code) {
        ErrorCodes.Unauthorized.value -> return MostAPIException.Unauthorized
        ErrorCodes.DataNotFound.value -> return MostAPIException.DataNotFound
        ErrorCodes.ForcedLogout.value -> return MostAPIException.ForcedLogout
        ErrorCodes.InvalidPin.value -> return MostAPIException.InvalidCredentials("Incorrect PIN")
        ErrorCodes.WrongPassword.value -> return MostAPIException.WrongPassword
        ErrorCodes.InvalidBody.value -> return MostAPIException.InvalidBody
        ErrorCodes.BadInput.value -> return MostAPIException.BadInput
        ErrorCodes.WrongOtp.value -> return MostAPIException.WrongOtp
        ErrorCodes.OtpNotFound.value -> return MostAPIException.OtpNotFound
    }

    return MostAPIException.Unknown("No mapping found. You may need to add one...")
}