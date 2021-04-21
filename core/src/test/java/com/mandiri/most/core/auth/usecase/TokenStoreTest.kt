package com.mandiri.most.core.auth.usecase

import com.mandiri.most.core.auth.api.model.AuthenticationType
import com.mandiri.most.core.auth.token.*
import com.mandiri.most.core.init.NetworkConstants
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TokenStoreTest {

    private lateinit var tokenStore: TokenStore

    @Before
    fun setup() {
        this.tokenStore = DefaultTokenStore(SimpleMemoryKeyValueStore())
    }

    @Test
    fun `test a1token insertion and retrieval`() {
        val (a1Token, _) = makeToken(TokenScope.A1, 60, 0)
        tokenStore.setToken(TokenScope.A1, a1Token)
        val token = tokenStore.getTokenString(TokenScope.A1)
        Assert.assertEquals(a1Token, token)
        val token2 = tokenStore.getTokenStringByXAuth(NetworkConstants._X_AUTHSCOPE_VAL_A1TOKEN)
        Assert.assertEquals(a1Token, token2)
        tokenStore.deleteAllTokens()
        val tokenNone = tokenStore.getTokenString(TokenScope.A1)
        Assert.assertNull(tokenNone)
    }

    @Test
    fun `test a2token insertion and retrieval`() {
        val (a2Token, _) = makeToken(TokenScope.Market, 60, 0)
        tokenStore.setToken(TokenScope.Market, a2Token)
        val token = tokenStore.getTokenString(TokenScope.Market)
        Assert.assertEquals(a2Token, token)
        val token2 = tokenStore.getTokenStringByXAuth(NetworkConstants._X_AUTHSCOPE_VAL_SVC_MARKET)
        Assert.assertEquals(a2Token, token2)
        tokenStore.deleteAllTokens()
        val tokenNone = tokenStore.getTokenString(TokenScope.Market)
        Assert.assertNull(tokenNone)
    }

    @Test
    fun `test pin save and retriveval`() {
        val pin = "123456"
        tokenStore.savePin(pin)
        Assert.assertEquals(pin, tokenStore.getPin())
        tokenStore.deleteAllTokens()
        Assert.assertNull(tokenStore.getPin())
    }

    @Test
    fun `test loginInfo`() {
        // No token --> None
        Assert.assertEquals(tokenStore.loginStatus(), AuthenticationType.None)

        // Valid guest token
        val (token1, _) = makeToken(TokenScope.A1, 60, 0, guest = true)
        tokenStore.setToken(TokenScope.A1, token1)
        Assert.assertEquals(tokenStore.loginStatus(), AuthenticationType.Guest)

        // Valid user token
        val (token2, _) = makeToken(TokenScope.A1, 60, 0, guest = false)
        tokenStore.setToken(TokenScope.A1, token2)
        Assert.assertEquals(tokenStore.loginStatus(), AuthenticationType.User)

        // Expired token
        val (token3, raw3) = makeToken(TokenScope.A1, -60, 60, guest = false)
        Assert.assertTrue(raw3.isExpired())
        tokenStore.setToken(TokenScope.A1, token3)
        Assert.assertEquals(tokenStore.getTokenString(TokenScope.A1), token3)
        Assert.assertEquals(tokenStore.loginStatus(), AuthenticationType.None)
        //side effect, expired token is automatically deleted
        Assert.assertNull(tokenStore.getTokenString(TokenScope.A1))

    }
}