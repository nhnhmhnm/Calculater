package org.example.cal.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.startWith
import org.example.cal.dto.CalculatorRequest
import org.example.cal.exception.CustomException
import org.example.cal.exception.CustomExceptionWrapper
import java.math.BigDecimal

class CalculatorServiceTest : BehaviorSpec({
    val calculatorService  = CalculatorService()

    Given("정수 두 개와 연산자가 주어진 상태에서") {
        When("연산자가 +일 경우") {
            Then("두 수를 더한다.") {
                val request = CalculatorRequest(BigDecimal("3"), BigDecimal("2"), "+")
                calculatorService.calculate(request).result shouldBe BigDecimal("5")
            }
        }
        When("연산자가 -일 경우") {
            Then("두 수를 뺀다.") {
                val request = CalculatorRequest(BigDecimal("3"), BigDecimal("2"), "-")
                calculatorService.calculate(request).result shouldBe BigDecimal("1")
            }
        }
        When("연산자가 *일 경우") {
            Then("두 수를 곱한다.") {
                val request = CalculatorRequest(BigDecimal("3"), BigDecimal("2"), "*")
                calculatorService.calculate(request).result shouldBe BigDecimal("6")
            }
        }
        When("연산자가 /일 경우") {
            Then("두 수를 나눈다. (나누어 떨어질 경우)") {
                val request = CalculatorRequest(BigDecimal("3"), BigDecimal("2"), "/")
                calculatorService.calculate(request).result shouldBe BigDecimal("1.5000000000")
            }
            Then("두 수를 나눈다. (나누어 떨어지지 않을 경우 -> 반올림)") {
                val request = CalculatorRequest(BigDecimal("2"), BigDecimal("3"), "/")
                calculatorService.calculate(request).result shouldBe BigDecimal("0.6666666667")
            }
        }
    }

    Given("실수 두 개와 연산자가 주어진 상태에서") {
        When("연산자가 +일 경우") {
            Then("두 수를 더한다.") {
                val request = CalculatorRequest(BigDecimal("3.33"), BigDecimal("2.22"), "+")
                calculatorService.calculate(request).result shouldBe BigDecimal("5.55")
            }
        }
        When("연산자가 -일 경우") {
            Then("두 수를 뺀다.") {
                val request = CalculatorRequest(BigDecimal("3.33"), BigDecimal("2.22"), "-")
                calculatorService.calculate(request).result shouldBe BigDecimal("1.11")
            }
        }
        When("연산자가 *일 경우") {
            Then("두 수를 곱한다.") {
                val request = CalculatorRequest(BigDecimal("3.33"), BigDecimal("2.22"), "*")
                calculatorService.calculate(request).result shouldBe BigDecimal("7.3926")
            }
        }
        When("연산자가 /일 경우") {
            Then("두 수를 나눈다. (나누어 떨어질 경우)") {
                val request = CalculatorRequest(BigDecimal("3.33"), BigDecimal("2.22"), "/")
                calculatorService.calculate(request).result shouldBe BigDecimal("1.5000000000")
            }
            Then("두 수를 나눈다. (나누어 떨어지지 않을 경우 -> 반올림)") {
                val request = CalculatorRequest(BigDecimal("2.22"), BigDecimal("3.33"), "/")
                calculatorService.calculate(request).result shouldBe BigDecimal("0.6666666667")
            }
        }
    }

    Given("정수와 실수, 연산자가 주어진 상태에서") {
        When("연산자가 +일 경우") {
            Then("두 수를 더한다.") {
                val request = CalculatorRequest(BigDecimal("4"), BigDecimal("2.2"), "+")
                calculatorService.calculate(request).result shouldBe BigDecimal("6.2")
            }
        }
        When("연산자가 -일 경우") {
            Then("두 수를 뺀다.") {
                val request = CalculatorRequest(BigDecimal("4"), BigDecimal("2.2"), "-")
                calculatorService.calculate(request).result shouldBe BigDecimal("1.8")
            }
        }
        When("연산자가 *일 경우") {
            Then("두 수를 곱한다.") {
                val request = CalculatorRequest(BigDecimal("4"), BigDecimal("2.2"), "*")
                calculatorService.calculate(request).result shouldBe BigDecimal("8.8")
            }
        }
        When("연산자가 /일 경우") {
            Then("두 수를 나눈다. (나누어 떨어질 경우)") {
                val request = CalculatorRequest(BigDecimal("2.2"), BigDecimal("4"), "/")
//                calculatorService.calculate(request).result shouldBe BigDecimal("0.5500000000")
                val response = calculatorService.calculate(request)
                response.result shouldBe BigDecimal("0.5500000000")
            }
            Then("두 수를 나눈다. (나누어 떨어지지 않을 경우 -> 반올림)") {
                val request = CalculatorRequest(BigDecimal("4"), BigDecimal("2.2"), "/")
//                calculatorService.calculate(request).result shouldBe BigDecimal("1.8181818182")
                val response = calculatorService.calculate(request)
                response.result shouldBe BigDecimal("1.8181818182")
            }
        }
    }

    Given("예외 상황이 발생한 경우") {
        When("0으로 나눈 경우") {
            Then("DivideByZeroException이 발생해야 한다.") {
                val request = CalculatorRequest(BigDecimal("5"), BigDecimal("0"), "/")

                val exception = shouldThrow<CustomExceptionWrapper> {
                    calculatorService.calculate(request)
                }
                // message가 아닌 code로 비교
                exception.error.code shouldBe CustomException.DIVIDE_BY_ZERO.code
            }
        }
        When("0.0으로 나눈 경우") {
            Then("DivideByZeroException이 발생해야 한다.") {
                val request = CalculatorRequest(BigDecimal("5"), BigDecimal("0.0"), "/")

                val exception = shouldThrow<CustomExceptionWrapper> {
                    calculatorService.calculate(request)
                }
                exception.error.code shouldBe CustomException.DIVIDE_BY_ZERO.code
            }
        }
        When("지원하지 않는 연산자를 사용한 경우") {
            Then("InvalidOperatorException이 발생해야 한다.") {
                val request = CalculatorRequest(BigDecimal("5"), BigDecimal("5"), " ")

                val exception = shouldThrow<CustomExceptionWrapper> {
                    calculatorService.calculate(request)
                }
                // 정확한 값 비교
                exception.error.code shouldBe CustomException.INVALID_OPERATOR.code
            }
        }
    }
})
