package org.example.cal.exception

enum class CustomException(
    override val status: Int,
    override val code: String,
    override val message: String
) : CodeInterface {
    DIVIDE_BY_ZERO(400, "E001", "Can not divide by zero"),
    INVALID_OPERATOR(400, "E002", "Invalid operator")
}
