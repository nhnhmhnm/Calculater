package org.example.cal.service

import org.example.cal.domain.Operator
import org.example.cal.dto.CalculatorRequest
import org.example.cal.dto.CalculatorResponse
import org.example.cal.exception.DivideByZeroException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CalculatorService {
    fun calculate(request: CalculatorRequest) : CalculatorResponse {
        val (operand1, operand2, operatorStr) = request
        val operatorEnum = Operator.toEnum(operatorStr)

        return when (operatorEnum) {
            Operator.PLUS -> CalculatorResponse(operand1.add(operand2))
            Operator.MINUS-> CalculatorResponse(operand1.subtract(operand2))
            Operator.MULTIPLY -> CalculatorResponse(operand1.multiply(operand2))
            Operator.DIVIDE-> {
                if (operand2 == BigDecimal.ZERO) throw DivideByZeroException()
                CalculatorResponse(operand1.divide(operand2, 10, RoundingMode.HALF_UP))
            }
        }
    }
}
