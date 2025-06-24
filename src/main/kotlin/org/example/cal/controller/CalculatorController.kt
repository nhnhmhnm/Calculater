package org.example.cal.controller

import org.example.cal.dto.CalculatorRequest
import org.example.cal.dto.CalculatorResponse
import org.example.cal.service.CalculatorService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class CalculatorController (private val calculatorService: CalculatorService){
//    @Autowired
//    private lateinit var calculatorService: CalculatorService

    @PostMapping("/cal")
    fun calculate(@RequestBody request: CalculatorRequest): CalculatorResponse {

        return calculatorService.calculate(request)
    }
}