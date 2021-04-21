package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.model.TokenResponse
import com.mandiri.most.core.auth.token.TokenScope
import com.mandiri.most.core.common.network.APIError
import com.mandiri.most.core.common.network.ErrorCodes
import com.mandiri.most.core.init.log
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

class TestTokenDispatcher(
    val a1LifeSec: Long,
    val a2LifeSec: Long,
    val a1RefreshAsGuest: Boolean = true,
    val forcedLogoutOnRefresh: Boolean = false,
    val forcedLogoutOnAuthorize: Boolean = false
): Dispatcher() {

    override fun dispatch(request: RecordedRequest): MockResponse {
        log.d("token dispatcher: $request")
        return when(request.path) {
            "/user/login" -> makeTokenResponse(TokenScope.A1, makeToken(TokenScope.A1, a1LifeSec, 0, guest = false).str)
            "/user/login/guest" -> makeTokenResponse(TokenScope.A1, makeToken(TokenScope.A1, a1LifeSec, 0, guest = true).str)
            "/user/token/refresh" -> when(forcedLogoutOnRefresh) {
                false -> makeTokenResponse(TokenScope.A1, makeToken(TokenScope.A1, a1LifeSec, 0, guest = a1RefreshAsGuest).str)
                true -> makeTokenRefreshForcedLogoutResponse()
            }
            "/user/token/authorize" -> when(forcedLogoutOnAuthorize) {
                false -> makeTokenResponseAsList(TokenScope.Market, makeToken(TokenScope.Market, a2LifeSec, 0).str)
                true ->  makeTokenRefreshForcedLogoutResponse()
            }
            "/user/token/pin" -> makeTokenResponse(TokenScope.Pin, makeToken(TokenScope.Pin, 300, 0).str)
            else -> MockResponse().setResponseCode(404)
        }
    }

    private fun makeTokenResponse(scope: TokenScope, tokenString: String): MockResponse {

        val responseBody = TokenResponse(
            scheme = "Bearer",
            token = tokenString,
            scope = scope.value,
            expiresAt = "2019-08-24T14:15:22Z"
        )
        val bodyString = Json.encodeToString(responseBody)
        return MockResponse().setResponseCode(200).setBody(bodyString)
    }

    private fun makeTokenResponseAsList(scope: TokenScope, tokenString: String): MockResponse {

        val responseBody = listOf(TokenResponse(
            scheme = "Bearer",
            token = tokenString,
            scope = scope.value,
            expiresAt = "2019-08-24T14:15:22Z"
            )
        )
        val bodyString = Json.encodeToString(responseBody)
        return MockResponse().setResponseCode(200).setBody(bodyString)
    }

    private fun makeTokenRefreshForcedLogoutResponse(): MockResponse {
        log.d("test token refresher making forced logout response")
        val responseBody = APIError(code = ErrorCodes.ForcedLogout.value, "forced logout")
        val bodyString = Json.encodeToString(responseBody)
        return MockResponse().setResponseCode(401).setBody(bodyString)
    }
}