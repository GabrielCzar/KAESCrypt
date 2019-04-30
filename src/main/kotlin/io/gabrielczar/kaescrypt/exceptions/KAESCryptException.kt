package io.gabrielczar.kaescrypt.exceptions

sealed class KAESCryptException {
    class DecryptException(ex: Exception) : Exception(ex)
}