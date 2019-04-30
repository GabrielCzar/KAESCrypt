package io.gabrielczar.kaescrypt.vo

data class EncryptResult (val encryptedMessage :String, val salt: String, val ivParameter: String)