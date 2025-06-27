package org.example.cal.service

import org.example.cal.domain.Operator
import org.example.cal.dto.CalculatorRequest
import org.example.cal.dto.CalculatorResponse
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CalculatorService {
    fun calculate(request: CalculatorRequest) : CalculatorResponse {
//        val operand1 = request.operand1
//        val operand2 = request.operand2
//        val operator = request.operator
        val (operand1, operand2, operatorStr) = request
        val operator = Operator.toEnum(operatorStr)

        return when (operator) {
            Operator.PLUS -> CalculatorResponse(operand1.add(operand2))
            Operator.MINUS-> CalculatorResponse(operand1.subtract(operand2))
            Operator.MULTIPLY -> CalculatorResponse(operand1.multiply(operand2))
            Operator.DIVIDE-> {
                require(operand2.compareTo(BigDecimal.ZERO) != 0) {"0으로 나눌 수 없습니다."}
                CalculatorResponse(operand1.divide(operand2, 5, RoundingMode.HALF_UP))
            }
        }
    }
}
