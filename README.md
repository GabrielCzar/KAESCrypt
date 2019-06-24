# KAESCrypt [![Build Status](https://travis-ci.com/GabrielCzar/KAESCrypt.svg?branch=master)](https://travis-ci.com/GabrielCzar/KAESCrypt) [![codecov](https://codecov.io/gh/GabrielCzar/KAESCrypt/branch/master/graph/badge.svg)](https://codecov.io/gh/GabrielCzar/KAESCrypt) [![](https://jitpack.io/v/gabrielczar/kaescrypt.svg)](https://jitpack.io/#gabrielczar/kaescrypt)
String extension to encrypting & decrypting strings using AES (Advanced Encryption Standard).

* AES-256 
* CBC mode
* PKCS5 Padding

### Usage

##### Create a key

```kotlin
val message = "This is a message"

val secretKey = generateRandomKey()

// or use your own key
val secretKey = "eMVVnPH8Vft+nVqCUJkjCTSA"
```

##### Encrypt

```kotlin
val encryptResult = message.encrypt(secretKey)
val (encryptedMessage, salt, ivParameter) = encryptResult

// or use your own salt and ivParameter
val salt = "Cm6aY5/WqZCv4jjEXmF"
val ivParameter = "sghPeycC5XGQBCpXLbg"
val encryptResult = message.encrypt(secretKey, salt, ivParameter)
```

##### Decrypt

```kotlin
val decryptedMessage = encryptedMessage.decrypt(secretKey, encryptResult)

// or parameterized
val (encryptedMessage, salt, ivParameter) = encryptResult
val decryptedMessage = encryptedMessage.decrypt(secretKey, salt, ivParameter)

// or wrapper any exception with a null result
val decryptedMessage = encryptedMessage.decryptOrNull(secretKey, encryptResult)
```

### License

`KAESCrypt` is available under the MIT license. See the [LICENSE](/LICENSE) file for more info.