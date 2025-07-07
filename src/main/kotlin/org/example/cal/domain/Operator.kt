package org.example.cal.domain

import com.fasterxml.jackson.annotation.JsonCreator
import org.example.cal.exception.CustomException
import org.example.cal.exception.CustomExceptionWrapper

enum class Operator(val symbol: String) {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    // companion object의 요소를 static으로 사용 -> Operator.toEnum(...)으로 호출
    companion object {
        @JvmStatic // Kotlin -> Java로 사용할 때 static으로 인식되게 (Java와 연동되는 환경에서 씀)
        @JsonCreator // json -> enum/객체로 변경
        fun toEnum(symbol: String) :
            Operator = entries.find { it.symbol == symbol } ?: throw CustomExceptionWrapper(CustomException.INVALID_OPERATOR)
    }
}
