package org.example.calculator.dto

import java.math.BigDecimal

data class CalculatorRequest (
    val operand1: BigDecimal,
    val operator: String,
    val operand2: BigDecimal
)
