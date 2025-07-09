package org.example.calculator.repository

import org.example.calculator.domain.Calculator
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class CalculatorRepository(
    val jdbcTemplate : JdbcTemplate
) {
    // RowMapper: ResultSet → Calculator 객체로 매핑
    private val mapper = RowMapper<Calculator> { rs: ResultSet, _ ->
        Calculator(
            id = rs.getLong("id"),
            user_id = rs.getLong("user_id"),
            operand1 = rs.getBigDecimal("operand1"),
            operator = rs.getString("operator"),
            operand2 = rs.getBigDecimal("operand2"),
            result = rs.getBigDecimal("result"),
            time = rs.getTimestamp("time").toLocalDateTime()
        )
    }

    // 계산 기록 저장
    fun save(calculator: Calculator): Calculator {
//        // id 값은 자동 증가로 생략 -> id 값을 모름 -> select로 조회해야 함
//        val sql = """
//            INSERT INTO calculations (user_id, operand1, operator, operand2, result, time)
//            VALUES (?, ?, ?, ?, ?, ?)
//        """.trimIndent()
//
//        jdbcTemplate.update(
//            sql,
//            calculator.user_id,
//            calculator.operand1,
//            calculator.operator,
//            calculator.operand2,
//            calculator.result,
//            calculator.time
//        }

        val simpleJdbcOrderInsert = SimpleJdbcInsert(jdbcTemplate)
            .withTableName("calculations")
            .usingGeneratedKeyColumns("id")

        // mapOf(key to value) : 읽기 전용 map -> 불변
        // mutableMapOf : 값 추가/수정 가능한 map
        val values = mapOf(
            "user_id" to calculator.user_id,
            "operand1" to calculator.operand1,
            "operator" to calculator.operator,
            "operand2" to calculator.operand2,
            "result" to calculator.result,
            "time" to calculator.time
        )
        val newId = simpleJdbcOrderInsert.executeAndReturnKey(values).toLong()

        log.info("Generated key insert: $newId")

        return calculator.copy(id = newId)
    }

    // 특정 사용자(user_id)의 계산 기록 조회 (시간순 내림차순)
    fun findByUser_Id(user_id: Long): List<Calculator> {
        val sql = """
            SELECT * FROM calculations
            WHERE user_id = ?
            ORDER BY time DESC
        """.trimIndent()

        return jdbcTemplate.query(sql, mapper, user_id)
    }
}
