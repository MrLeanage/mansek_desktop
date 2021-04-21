package com.mandiri.most.core.auth.token

import com.mandiri.most.core.auth.api.model.AuthenticationType
import com.mandiri.most.core.auth.management.SecureKeyValueStore
import com.mandiri.most.core.init.NetworkConstants
import com.mandiri.most.core.init.log

internal class DefaultTokenStore(private val store: SecureKeyValueStore): TokenStore {

    private var memoryStore: MutableMap<TokenScope, TokenRepresentation> = mutableMapOf()

    @Synchronized
    override fun initialize() {
        retrieveToken(TokenScope.A1)
    }

    @Synchronized
    override fun setToken(scope: TokenScope, jwtToken: JWTToken): Boolean {
        val token = when(scope) {
            TokenScope.A1 -> decodeTokenString<A1Token>(jwtToken)
            TokenScope.Pin -> decodeTokenString<PinToken>(jwtToken)
            else -> decodeTokenString<A2Token>(jwtToken)
        }
        return when(token) {
            null -> {
                log.e("token store got invalid token: $token")
                false
            }
            else -> {
                storeToken(scope, TokenRepresentation(token, jwtToken))
                true
            }
        }
    }

    @Synchronized
    override fun getTokenStringByXAuth(value: String): String? {
        val scope = mapXAuthValueToScope(value)
        return when(scope) {
            null -> null
            else -> retrieveToken(scope)?.serialized
        }
    }

    @Synchronized
    override fun loginStatus(): AuthenticationType {
        val representation = retrieveToken(TokenScope.A1)

        return when(representation) {
            null -> AuthenticationType.None
            else -> {
                val token = representation.token as? A1Token ?: return AuthenticationType.None

                if(token.isExpired()) {
                    deleteAllTokens()
                    return AuthenticationType.None
                }
                if(representation.token.guest) AuthenticationType.Guest else AuthenticationType.User
            }
        }
    }

    @Synchronized
    override fun getTokenString(scope: TokenScope): String? {
        return retrieveToken(scope)?.serialized
    }

    @Synchronized
    override fun getTokenObject(scope: TokenScope): BaseToken? {
        return retrieveToken(scope)?.token
    }

    private fun retrieveToken(scope: TokenScope): TokenRepresentation? {
        val token = memoryStore[scope]
        if(token == null && scope == TokenScope.A1) {
            val storedToken = store.load(scopeToKey(TokenScope.A1))
            storedToken?.let {
                val tokenString = it
                val decoded = decodeTokenString<A1Token>(tokenString)
                decoded?.let {
                    var rep = TokenRepresentation(decoded, tokenString)
                    memoryStore[TokenScope.A1] = rep
                    return rep
                }
            }
        }
        return token
    }

    private fun storeToken(scope: TokenScope, representation: TokenRepresentation) {
        log.d("storing token for scope $scope")
        memoryStore[scope] = representation
        if(scope == TokenScope.A1) {
            store.save(scopeToKey(scope), representation.serialized)
        }
    }

    private fun deleteToken(scope: TokenScope) {
        memoryStore.remove(scope)
        when(scope) {
            TokenScope.A1 -> store.delete(scopeToKey(scope))
            TokenScope.Pin -> store.delete(PIN_KEY)
            else -> {}
        }
    }

    @Synchronized
    override fun deleteAllTokens() {
        memoryStore = mutableMapOf()
        store.delete(scopeToKey(TokenScope.A1))
        store.delete(PIN_KEY)
        log.i("tokenStore delete all tokens done")
    }

    @Synchronized
    override fun getTokensTimeInfo(): List<TokenTimeInfo> {
        return memoryStore.map {
            val token = it.value.token
            TokenTimeInfo(
                scope = it.key,
                issuance = token.issuanceInstant(),
                expiration = token.expirationInstant(),
                isExpired = token.isExpired(),
                secondsSinceIssue = token.secondsSinceIssued(),
                secondsUntilExpiry = token.secondsLeft()
            )
        }
    }

    @Synchronized
    override fun savePin(pin: String) {
        store.save(PIN_KEY, pin)
    }

    @Synchronized
    override fun getPin(): String? {
        return store.load(PIN_KEY)
    }
}

private data class TokenRepresentation(val token: BaseToken, val serialized: String)

private fun scopeToKey(scope: TokenScope): String {
    return "MOSTTOKEN" + scope.value
}

private const val PIN_KEY = "MOSTPIN"

private fun mapXAuthValueToScope(value: String): TokenScope? {
    return when(value) {
        NetworkConstants._X_AUTHSCOPE_VAL_A1TOKEN -> TokenScope.A1
        NetworkConstants._X_AUTHSCOPE_VAL_PIN -> TokenScope.Pin
        NetworkConstants._X_AUTHSCOPE_VAL_SVC_USER -> TokenScope.User
        NetworkConstants._X_AUTHSCOPE_VAL_SVC_PORTFOLIO -> TokenScope.Portfolio
        NetworkConstants._X_AUTHSCOPE_VAL_SVC_ORDER -> TokenScope.Order
        NetworkConstants._X_AUTHSCOPE_VAL_SVC_MARKET -> TokenScope.Market
        else -> null
    }

}