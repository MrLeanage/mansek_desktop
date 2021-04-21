package com.mandiri.most.core.auth.token

import com.mandiri.most.core.init.log
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.decodeBase64
import java.nio.charset.StandardCharsets


typealias JWTToken = String

@OptIn(ExperimentalSerializationApi::class)
internal inline fun <reified T: BaseToken>decodeTokenString(token: JWTToken): T? {
    var json = Json {
        ignoreUnknownKeys = true
    }
    val jwtSections = token.split(".")
    if(jwtSections.size != 3) {
        log.d("jwt token does not include 3 parts: $token")
        return null
    }
    val tokenData: String? = jwtSections[1].decodeBase64()?.string(StandardCharsets.UTF_8)

    return tokenData?.let{
        try {
            json.decodeFromString<T>(it)
        } catch (e: SerializationException) {
            log.e("deserialization exception for token $it")
            null
        } catch (e: Exception) {
            log.e("unexpected exception parsing token $it")
            null
        }
    }
}