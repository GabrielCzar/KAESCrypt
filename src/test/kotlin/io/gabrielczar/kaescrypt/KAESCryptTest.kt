package io.gabrielczar.kaescrypt

import io.gabrielczar.kaescrypt.exceptions.KAESCryptException
import io.gabrielczar.kaescrypt.utils.decryptOrNull
import io.gabrielczar.kaescrypt.vo.EncryptResult
import org.junit.Assert
import org.junit.Test

class KAESCryptTest {
    private val keyOne = "ENCRYPT_KEY"
    private val keyTwo = "ENCRYPT_KEY_2"

    private val salt = "SALT_FOR_TEST"
    private val ivParameter = "IV_PARAM_FOR_TEST"

    private val message = "ENCRYPT_MESSAGE"

    @Test
    fun `should be encrypt string`() {
        val encryptResult = message.encrypt(secretKey = keyOne)
        val encryptResultTwo = message.encrypt(secretKey = keyTwo)

        `assert result is different of message`(encryptResult)
        `assert result is different of message`(encryptResultTwo)
        `assert that result are different`(encryptResult, encryptResultTwo)
    }

    @Test
    fun `should be decrypt message`() {
        val result = message.encrypt(secretKey = keyOne)

        `assert result is different of message`(result)

        val encryptMessage = result.encryptedMessage
        val decryptMessage = encryptMessage.decrypt(secretKey = keyOne, salt = result.salt, ivParameter = result.ivParameter)

        `assert that is the same message`(decryptMessage)
    }

    @Test
    fun `should be decrypt message correctly`() {
        val result = message.encrypt(secretKey = keyOne, salt = salt, ivParameter = ivParameter)

        `assert result is different of message`(result)

        val encryptMessage = result.encryptedMessage
        val decryptMessage = encryptMessage.decryptOrNull(secretKey = keyOne, salt = result.salt, ivParameter = result.ivParameter)

        `assert that is the same message`(decryptMessage)
    }

    @Test
    fun `should be decrypt message using encrypt result`() {
        val result = message.encrypt(secretKey = keyOne, salt = salt, ivParameter = ivParameter)

        `assert result is different of message`(result)

        val encryptMessage = result.encryptedMessage
        val decryptMessage = encryptMessage.decryptOrNull(secretKey = keyOne, encryptResult = result)

        `assert that is the same message`(decryptMessage)
    }

    @Test(expected = KAESCryptException.DecryptException::class)
    fun `should be fail to decrypt with invalid salt`() {
        message.encrypt(secretKey = keyOne).also {
            `assert result is different of message`(it)
            it
                .encryptedMessage
                .decrypt(secretKey = keyOne, salt = INVALID_CONTENT, ivParameter = it.ivParameter)
        }
    }

    @Test
    fun `should be result null with invalid salt`() {
        message.encrypt(secretKey = keyOne).also {
            `assert result is different of message`(it)

            Assert.assertNull(
                it
                    .encryptedMessage
                    .decryptOrNull(secretKey = keyOne, salt = INVALID_CONTENT, ivParameter = it.ivParameter)
            )
        }
    }

    @Test
    fun `should be result null with invalid key`() {
        message.encrypt(secretKey = keyOne).also {
            `assert result is different of message`(it)

            Assert.assertNull(
                it
                    .encryptedMessage
                    .decryptOrNull(keyTwo, it)
            )
        }
    }

    @Test(expected = KAESCryptException.DecryptException::class)
    fun `should be fail to decrypt with invalid ivParameter`() {
        message.encrypt(secretKey = keyOne).also {
            `assert result is different of message`(it)
            it
                .encryptedMessage
                .decrypt(secretKey = keyOne, salt = it.salt, ivParameter = INVALID_CONTENT)
        }
    }

    @Test
    fun `should be result null with invalid ivParameter`() {
        message.encrypt(secretKey = keyOne).also {
            `assert result is different of message`(it)

            Assert.assertNull(
                it
                    .encryptedMessage
                    .decryptOrNull(secretKey = keyOne, salt = it.salt, ivParameter = INVALID_CONTENT)
            )
        }
    }

    @Test
    fun `should be result are different with same key and same salt`() {
        val encryptResult = message.encrypt(secretKey = keyOne, salt = salt)
        val encryptResultClone = message.encrypt(secretKey = keyOne, salt = salt)

        Assert.assertEquals(encryptResult.salt, encryptResultClone.salt)
        Assert.assertNotEquals(encryptResult.ivParameter, encryptResultClone.ivParameter)
        Assert.assertNotEquals(encryptResult.encryptedMessage, encryptResultClone.encryptedMessage)
    }

    @Test
    fun `should be result are different with same salt and same ivParameter`() {
        val encryptResult = message.encrypt(secretKey = keyOne, salt = salt, ivParameter = ivParameter)
        val encryptResultClone = message.encrypt(secretKey = keyTwo, salt = salt, ivParameter = ivParameter)

        Assert.assertEquals(encryptResult.salt, encryptResultClone.salt)
        Assert.assertEquals(encryptResult.ivParameter, encryptResultClone.ivParameter)
        Assert.assertNotEquals(encryptResult.encryptedMessage, encryptResultClone.encryptedMessage)
    }

    @Test
    fun `should be result are the same with same key, same salt and same ivParameter`() {
        val encryptResult = message.encrypt(secretKey = keyOne, salt = salt, ivParameter = ivParameter)
        val encryptResultClone = message.encrypt(secretKey = keyOne, salt = salt, ivParameter = ivParameter)

        Assert.assertEquals(encryptResult.salt, encryptResultClone.salt)
        Assert.assertEquals(encryptResult.ivParameter, encryptResultClone.ivParameter)
        Assert.assertEquals(encryptResult.encryptedMessage, encryptResultClone.encryptedMessage)
    }

    private fun `assert that result are different`(result: EncryptResult, resultTwo: EncryptResult) {
        Assert.assertNotEquals(result.encryptedMessage, resultTwo.encryptedMessage)
        Assert.assertNotEquals(result.ivParameter, resultTwo.ivParameter)
        Assert.assertNotEquals(result.salt, resultTwo.salt)
    }

    private fun `assert that is the same message`(decryptMessage: String?) {
        Assert.assertNotNull(decryptMessage)
        Assert.assertEquals(message, decryptMessage)
    }

    private fun `assert result is different of message`(result: EncryptResult) {
        Assert.assertNotEquals(message, result.encryptedMessage)
        Assert.assertNotNull(result.encryptedMessage)
        Assert.assertNotNull(result.ivParameter)
        Assert.assertNotNull(result.salt)
    }

}

private const val INVALID_CONTENT = "INVALID_CONTENT"