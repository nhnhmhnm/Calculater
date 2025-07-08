package org.example.calculator.domain

import java.math.BigDecimal
import java.time.LocalDateTime

data class Calculator(
    var id: Long? = 0,
    val user_id: Long,
    val operand1: BigDecimal,
    val operator: String,
    val operand2: BigDecimal,
    val result: BigDecimal,
    val time: LocalDateTime
)
