package org.example.cal.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.example.cal.dto.CalculatorRequest
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
                val request = CalculatorRequest(BigDecimal("4"), BigDecimal("2.2"), "/")
                calculatorService.calculate(request).result shouldBe BigDecimal("1.8181818182")
            }
            Then("두 수를 나눈다. (나누어 떨어지지 않을 경우 -> 반올림)") {
                val request = CalculatorRequest(BigDecimal("2.2"), BigDecimal("4"), "/")
                calculatorService.calculate(request).result shouldBe BigDecimal("0.5500000000")
            }
        }
    }

//    Given("예외 상황이 발생한 경우") {
//        When("0으로 나눈 경우") {
//
//        }
//        When("지원하지 않는 연산자를 사용한 경우") {
//
//        }
//        When("올바르지 않은 숫자를 입력한 경우 ") {
//
//        }
//    }
})
