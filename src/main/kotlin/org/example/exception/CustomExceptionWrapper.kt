package org.example.exception

// enum은 예외로 throw할 수 없는 객체 -> throw CustomException.USER_NOT_FOUND 불가능
// enum을 예외로 감싸 throw 가능하게 만듦
class CustomExceptionWrapper(
    val error: CustomException,
    override val message: String = error.message // CustomException.message
) : RuntimeException(message)
