package org.example.calculator.service

import org.example.calculator.domain.Calculator
import org.example.calculator.domain.Operator
import org.example.calculator.dto.CalculatorLogResponse
import org.example.calculator.dto.CalculatorRequest
import org.example.calculator.dto.CalculatorResponse
import org.example.calculator.repository.CalculatorRepository
import org.example.exception.CustomException
import org.example.exception.CustomExceptionWrapper
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDateTime

@Service
class CalculatorService(
    private val calculatorRepository: CalculatorRepository
) {
    fun calculate(user_id: Long, request: CalculatorRequest): CalculatorResponse {
        // 계산
        val (operand1, operatorStr, operand2) = request
        val operatorEnum = Operator.toEnum(operatorStr)

        val result = when (operatorEnum) {
            Operator.PLUS -> operand1.add(operand2)
            Operator.MINUS -> operand1.subtract(operand2)
            Operator.MULTIPLY -> operand1.multiply(operand2)
            Operator.DIVIDE -> {
                if (operand2.compareTo(BigDecimal.ZERO) == 0) throw CustomExceptionWrapper(CustomException.DIVIDE_BY_ZERO)
                operand1.divide(operand2, 10, RoundingMode.HALF_UP)
            }
        }

        // 저장
        val calculatorLog = Calculator(
            user_id = user_id,
            operand1 = operand1.stripTrailingZeros(),
            operator = operatorStr,
            operand2 = operand2.stripTrailingZeros(),
            result = result.stripTrailingZeros(),
            time = LocalDateTime.now()
        )
        calculatorRepository.save(calculatorLog)

        return CalculatorResponse(result)
    }

    fun getCalculatorLog(user_id: Long): List<CalculatorLogResponse> {
        val records = calculatorRepository.findByUser_Id(user_id)

        // map : list의 각 요소를 변환해서 새 list로 만든다.
        return records.map {
            CalculatorLogResponse(
                operand1 = it.operand1,
                operator = it.operator,
                operand2 = it.operand2,
                result = it.result,
                time = it.time
            )
        }
    }
}
