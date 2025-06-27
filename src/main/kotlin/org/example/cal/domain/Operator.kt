package org.example.cal.domain

import com.fasterxml.jackson.annotation.JsonCreator
import org.example.cal.exception.InvalidOperatorException

enum class Operator(val symbol: String) {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    companion object {
        @JvmStatic // companion object의 요소를 static으로 사용 가능
        @JsonCreator // json -> enum/객체로 변경
        fun toEnum(symbol: String) :
            Operator = entries.find { it.symbol == symbol } ?: throw InvalidOperatorException(symbol)
    }
}
