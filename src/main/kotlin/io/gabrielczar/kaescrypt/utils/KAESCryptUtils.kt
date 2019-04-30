package io.gabrielczar.kaescrypt.utils

import io.gabrielczar.kaescrypt.AES
import io.gabrielczar.kaescrypt.PBKDF2
import io.gabrielczar.kaescrypt.decrypt
import io.gabrielczar.kaescrypt.exceptions.KAESCryptException
import io.gabrielczar.kaescrypt.vo.EncryptResult
import java.security.SecureRandom
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

internal val encoder : Base64.Encoder = Base64.getEncoder()
internal val decoder : Base64.Decoder = Base64.getDecoder()

internal fun fixIvParameter(byteArray: String): ByteArray = byteArray.toByteArray().copyOf(16)

internal fun String.toSecretKeySpec(salt: String) = SecretKeyFactory
    .getInstance(PBKDF2)
    .generateSecret(PBEKeySpec(this.toCharArray(), salt.toByteArray(), 65536, 256))
    .let { SecretKeySpec(it.encoded, AES) }

fun generateRandomKey(size : Int = 16): String = encoder.encodeToString(ByteArray(size).apply {
    SecureRandom().nextBytes(this)
})

@Throws(KAESCryptException.DecryptException::class)
fun String.decrypt(secretKey: String, encryptResult: EncryptResult) =
    this.decrypt(secretKey, encryptResult.salt, encryptResult.ivParameter)

fun String.decryptOrNull(secretKey: String, encryptResult: EncryptResult): String? = try {
    this.decrypt(secretKey, encryptResult)
} catch (ex: KAESCryptException.DecryptException) {
    null
}

fun String.decryptOrNull(secretKey: String, salt: String, ivParameter : String): String? = try {
    this.decrypt(secretKey, salt, ivParameter)
} catch (ex: KAESCryptException.DecryptException) {
    null
}