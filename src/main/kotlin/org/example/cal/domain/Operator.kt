package org.example.cal.domain

import com.fasterxml.jackson.annotation.JsonCreator

enum class Operator(val symbol: String) {
    PLUS("+"),
    MINUS("-"),
    MULTIPLY("*"),
    DIVIDE("/");

    companion object {
        @JvmStatic // companion object의 요소를 static으로 사용 가능
        @JsonCreator // json -> enum/객체로 변경
        fun toEnum(symbol: String): Operator =
            values().find { it.symbol == symbol }
                ?: throw IllegalArgumentException("지원하지 않는 연산자입니다: $symbol")
    }
}
