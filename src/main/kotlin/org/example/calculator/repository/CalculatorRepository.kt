package org.example.calculator.repository

import org.example.calculator.domain.Calculator
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class CalculatorRepository(
    private val namedParameterJdbcTemplate : NamedParameterJdbcTemplate
) {
    // RowMapper: ResultSet → Calculator 객체로 매핑
    private val rowMapper = RowMapper<Calculator> { rs: ResultSet, _ ->
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
    fun save(calculator: Calculator): Long {
        val sql = """
            INSERT INTO calculations (user_id, operand1, operator, operand2, result, time)
            VALUES (:user_id, :operand1, :operator, :operand2, :result, :time)
        """.trimIndent()

        val paramMap = MapSqlParameterSource()
            .addValue("user_id", calculator.user_id)
            .addValue("operand1", calculator.operand1)
            .addValue("operator", calculator.operator)
            .addValue("operand2", calculator.operand2)
            .addValue("result", calculator.result)
            .addValue("time", calculator.time)

        val keyHolder = GeneratedKeyHolder()
        namedParameterJdbcTemplate.update(sql, paramMap, keyHolder, arrayOf("id"))
        val newId = keyHolder.key!!.toLong()

        return newId
    }

    // 특정 사용자(user_id)의 계산 기록 조회 (시간순 내림차순)
    fun findByUser_Id(user_id: Long): List<Calculator> {
        val sql = """
            SELECT * FROM calculations
            WHERE user_id = :user_id
            ORDER BY time DESC
        """.trimIndent()

        val params = mapOf("user_id" to user_id)
        return namedParameterJdbcTemplate.query(sql, params, rowMapper)
    }
}
