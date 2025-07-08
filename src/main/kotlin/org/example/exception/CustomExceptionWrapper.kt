package org.example.exception

class CustomExceptionWrapper(
    val error: CustomException,
    override val message: String = error.message // CustomException.message
) : RuntimeException(message)
