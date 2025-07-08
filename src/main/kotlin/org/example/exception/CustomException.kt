package org.example.exception

enum class CustomException(
    override val status: Int,
    override val code: String,
    override val message: String
) : CodeInterface {
    // calculator exception
    DIVIDE_BY_ZERO(400, "C001", "Can not divide by zero"),
    INVALID_OPERATOR(400, "C002", "Invalid operator"),

    // user exception
    USER_NOT_FOUND(404, "U001", "User not found"),
    DUPLICATE_USERID(400, "U002", "This ID is duplicated."),
    INVALID_PASSWORD(400, "U003", "Invalid password"),
}
