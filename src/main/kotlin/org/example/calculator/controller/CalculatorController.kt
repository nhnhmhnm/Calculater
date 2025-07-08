package org.example.calculator.controller

import org.example.calculator.dto.CalculatorLogResponse
import org.example.calculator.dto.CalculatorRequest
import org.example.calculator.dto.CalculatorResponse
import org.example.calculator.service.CalculatorService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/calculator")
class CalculatorController (
    private val calculatorService: CalculatorService
) {
//    @Autowired
//    private lateinit var calculatorService: CalculatorService

    @PostMapping("/{user_id}")
    fun calculate(@PathVariable user_id: Long, @RequestBody request: CalculatorRequest): CalculatorResponse {
        return calculatorService.calculate(user_id, request)
    }

    @GetMapping("/{user_id}")
    fun getCalculatorLog(@PathVariable user_id: Long): List<CalculatorLogResponse> {
        return calculatorService.getCalculatorLog(user_id)
    }
}
