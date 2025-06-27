package org.example.cal.dto

import java.math.BigDecimal

data class CalculatorRequest (
    val operand1: BigDecimal,
    val operand2: BigDecimal,
    val operator: String
)
