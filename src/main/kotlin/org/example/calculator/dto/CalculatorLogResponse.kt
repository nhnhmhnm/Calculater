package org.example.calculator.dto

import java.math.BigDecimal
import java.time.LocalDateTime

data class CalculatorLogResponse(
    val operand1: BigDecimal,
    val operator: String,
    val operand2: BigDecimal,
    val result: BigDecimal,
    val time: LocalDateTime
)
