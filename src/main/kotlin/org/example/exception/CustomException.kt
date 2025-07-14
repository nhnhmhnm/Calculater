package org.example.exception

import org.springframework.http.HttpStatus

enum class CustomException(
    val status: HttpStatus,
    val code: String,
    val message: String
)  {
    // calculator exception
    DIVIDE_BY_ZERO(HttpStatus.BAD_REQUEST, "C001", "Can not divide by zero"),
    INVALID_OPERATOR(HttpStatus.BAD_REQUEST, "C002", "Invalid operator"),

    // user exception
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "User not found"),
    DUPLICATE_USERID(HttpStatus.BAD_REQUEST, "U002", "This ID is duplicated."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "U003", "Invalid password"),
}
