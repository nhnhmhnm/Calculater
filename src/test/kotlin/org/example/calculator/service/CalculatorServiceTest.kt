package org.example.calculator.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.example.calculator.domain.Calculator
import org.example.calculator.dto.CalculatorRequest
import org.example.calculator.repository.CalculatorRepository
import org.example.exception.CustomException
import org.example.exception.CustomExceptionWrapper
import java.math.BigDecimal
import java.time.LocalDateTime

class CalculatorServiceTest : BehaviorSpec({
    val calculatorRepository = mockk<CalculatorRepository>(relaxed = true)
    val calculatorService = CalculatorService(calculatorRepository)

    val saveSlot = slot<Calculator>()

    val user_id = 1L

    Given("정수 두 개와 연산자가 주어진 상태에서") {
        When("연산자가 +일 경우") {
            val request = CalculatorRequest(BigDecimal("3"), "+", BigDecimal("2"))

            Then("두 수를 더한다.") {
                val response = calculatorService.calculate(user_id, request)
                
                response.result.compareTo(BigDecimal("5")) shouldBe 0
            }
        }
        When("연산자가 -일 경우") {
            val request = CalculatorRequest(BigDecimal("3"), "-", BigDecimal("2"))

            Then("두 수를 뺀다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("1")) shouldBe 0
            }
        }
        When("연산자가 *일 경우") {
            val request = CalculatorRequest(BigDecimal("3"), "*", BigDecimal("2"))

            Then("두 수를 곱한다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("6")) shouldBe 0
            }
        }
        When("연산자가 /일 경우") {
            val request1 = CalculatorRequest(BigDecimal("3"), "/", BigDecimal("2"))

            Then("두 수를 나눈다. (나누어 떨어질 경우)") {
                val response = calculatorService.calculate(user_id, request1)

                response.result.compareTo(BigDecimal("1.5")) shouldBe 0
            }
            val request2 = CalculatorRequest(BigDecimal("2"), "/", BigDecimal("3"))

            Then("두 수를 나눈다. (나누어 떨어지지 않을 경우 -> 반올림)") {
                val response = calculatorService.calculate(user_id, request2)

                response.result.compareTo(BigDecimal("0.6666666667")) shouldBe 0
            }
        }
    }

    Given("실수 두 개와 연산자가 주어진 상태에서") {
        When("연산자가 +일 경우") {
            val request = CalculatorRequest(BigDecimal("3.33"), "+", BigDecimal("2.22"))

            Then("두 수를 더한다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("5.55")) shouldBe 0
            }
        }
        When("연산자가 -일 경우") {
            val request = CalculatorRequest(BigDecimal("3.33"), "-", BigDecimal("2.22"))

            Then("두 수를 뺀다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("1.11")) shouldBe 0
            }
        }
        When("연산자가 *일 경우") {
            val request = CalculatorRequest(BigDecimal("3.33"), "*", BigDecimal("2.22"))

            Then("두 수를 곱한다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("7.3926")) shouldBe 0
            }
        }
        When("연산자가 /일 경우") {
            val request1 = CalculatorRequest(BigDecimal("3.33"), "/", BigDecimal("2.22"))

            Then("두 수를 나눈다. (나누어 떨어질 경우)") {
                val response = calculatorService.calculate(user_id, request1)

                response.result.compareTo(BigDecimal("1.5")) shouldBe 0
            }
            val request2 = CalculatorRequest(BigDecimal("2.22"), "/", BigDecimal("3.33"))

            Then("두 수를 나눈다. (나누어 떨어지지 않을 경우 -> 반올림)") {
                val response = calculatorService.calculate(user_id, request2)

                response.result.compareTo(BigDecimal("0.6666666667")) shouldBe 0
            }
        }
    }

    Given("정수와 실수, 연산자가 주어진 상태에서") {
        When("연산자가 +일 경우") {
            val request = CalculatorRequest(BigDecimal("4"), "+", BigDecimal("2.2"))

            Then("두 수를 더한다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("6.2")) shouldBe 0
            }
        }
        When("연산자가 -일 경우") {
            val request = CalculatorRequest(BigDecimal("4"), "-", BigDecimal("2.2"))

            Then("두 수를 뺀다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("1.8")) shouldBe 0
            }
        }
        When("연산자가 *일 경우") {
            val request = CalculatorRequest(BigDecimal("4"), "*", BigDecimal("2.2"))

            Then("두 수를 곱한다.") {
                val response = calculatorService.calculate(user_id, request)

                response.result.compareTo(BigDecimal("8.8")) shouldBe 0
            }
        }
        When("연산자가 /일 경우") {
            val request1 = CalculatorRequest(BigDecimal("2.2"), "/", BigDecimal("4"))

            Then("두 수를 나눈다. (나누어 떨어질 경우)") {
                val response = calculatorService.calculate(user_id, request1)

                response.result.compareTo(BigDecimal("0.55")) shouldBe 0
            }
            val request2 = CalculatorRequest(BigDecimal("4"), "/", BigDecimal("2.2"))

            Then("두 수를 나눈다. (나누어 떨어지지 않을 경우 -> 반올림)") {
                val response = calculatorService.calculate(user_id, request2)

                response.result.compareTo(BigDecimal("1.8181818182")) shouldBe 0
            }
        }
    }

    Given("계산 요청이 주어진 경우") {
        When("0으로 나눈 경우") {
            val request = CalculatorRequest(BigDecimal("5"), "/", BigDecimal("0"))

            Then("DivideByZeroException이 발생해야 한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    calculatorService.calculate(user_id, request)
                }
                // message가 아닌 code로 비교
                exception.error.code shouldBe CustomException.DIVIDE_BY_ZERO.code
            }
        }
        When("0.0으로 나눈 경우") {
            val request = CalculatorRequest(BigDecimal("5"), "/", BigDecimal("0.0"))

            Then("DivideByZeroException이 발생해야 한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    calculatorService.calculate(user_id, request)
                }
                exception.error.code shouldBe CustomException.DIVIDE_BY_ZERO.code
            }
        }
        When("지원하지 않는 연산자를 사용한 경우") {
            val request = CalculatorRequest(BigDecimal("5"), " ", BigDecimal("5"))

            Then("InvalidOperatorException이 발생해야 한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    calculatorService.calculate(user_id, request)
                }
                exception.error.code shouldBe CustomException.INVALID_OPERATOR.code
            }
        }
        When("정상적으로 작동한 경우") {
            val request = CalculatorRequest(BigDecimal("5"), "+", BigDecimal("5"))

            val calculator = Calculator(
                id = 1L,
                user_id = user_id,
                operand1 = BigDecimal("5"),
                operand2 = BigDecimal("5"),
                operator = "+",
                result = BigDecimal("10"),
                time = LocalDateTime.now()
            )
            every { calculatorRepository.save(capture(saveSlot)) } returns calculator

            val response = calculatorService.calculate(user_id, request)

            Then("결과가 10이어야 한다.") {
                response.result shouldBe BigDecimal("10")
            }
            Then("결과가 저장돼야 한다.") {
                saveSlot.captured.user_id shouldBe user_id
                saveSlot.captured.operand1 shouldBe BigDecimal("5")
                saveSlot.captured.operand2 shouldBe BigDecimal("5")
                saveSlot.captured.operator shouldBe "+"
                saveSlot.captured.result shouldBe BigDecimal("10")
            }
        }
    }

})
