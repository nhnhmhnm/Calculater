package org.example.cal.service

import org.example.cal.dto.CalculatorRequest
import org.example.cal.dto.CalculatorResponse
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class CalculatorService {
    fun calculate(request: CalculatorRequest) : CalculatorResponse {
//        val operand1 = request.operand1
//        val operand2 = request.operand2
//        val operator = request.operator
        val (operand1, operand2, operator) = request

        return when (operator) {
            "+" -> CalculatorResponse(operand1 + operand2)
            "-" -> CalculatorResponse(operand1 - operand2)
            "*" -> CalculatorResponse(operand1 * operand2)
            "/" -> {
                require(operand2.compareTo(BigDecimal.ZERO) != 0) {"0으로 나눌 수 없습니다."}
                CalculatorResponse(operand1 / operand2)
            }
            else -> throw IllegalArgumentException("Invalid operator")
        }
    }
}