package io.gabrielczar.kaescrypt

import io.gabrielczar.kaescrypt.exceptions.KAESCryptException
import io.gabrielczar.kaescrypt.utils.*
import io.gabrielczar.kaescrypt.vo.EncryptResult
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec

internal const val AES = "AES"
internal const val CYPHER = "AES/CBC/PKCS5Padding"
internal const val PBKDF2 = "PBKDF2WithHmacSHA256"

fun String.encrypt(secretKey: String, salt: String = generateRandomKey(), ivParameter: String = generateRandomKey()): EncryptResult {
    val ivParam = fixIvParameter(ivParameter)
    val sKeyEncoded = encoder.encodeToString(secretKey.toByteArray())
    val key = sKeyEncoded.toSecretKeySpec(salt)

    val input = this.toByteArray()
    val cipher = Cipher.getInstance(CYPHER)
    cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(ivParam))
    val cipherText = ByteArray(cipher.getOutputSize(input.size))
    var ctLength = cipher.update(input, 0, input.size, cipherText, 0)
    val doFinal = cipher.doFinal(cipherText, ctLength)
    ctLength += doFinal
    val encryptResult = encoder.encodeToString(cipherText)
    return EncryptResult(encryptResult, salt, ivParameter)
}

@Throws(KAESCryptException.DecryptException::class)
fun String.decrypt(secretKey: String, salt: String, ivParameter : String): String {
    try {
        val ivParam = fixIvParameter(ivParameter)
        val sKeyEncoded = encoder.encodeToString(secretKey.toByteArray())
        val key = sKeyEncoded.toSecretKeySpec(salt)

        val cipherText = decoder.decode(this)
        val ctLength = cipherText.size
        val cipher = Cipher.getInstance(CYPHER)
        cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(ivParam))
        val plainText = ByteArray(cipher.getOutputSize(ctLength))
        var ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0)
        ptLength += cipher.doFinal(plainText, ptLength)
        return String(plainText, 0, ptLength)
    } catch (ex: Exception) {
        throw KAESCryptException.DecryptException(ex)
    }
}



