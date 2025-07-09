package org.example.user.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.example.exception.CustomException
import org.example.exception.CustomExceptionWrapper
import org.example.user.domain.User
import org.example.user.dto.CreateUserRequest
import org.example.user.dto.UpdateUserPasswordRequest
import org.example.user.repository.UserRepository
import java.util.*

// every { ... } returns/throws ... : 메소드가 호출되면 어떤 값을 리턴할 지
// 실제 DB에 접근하지 않고, 테스트 용으로 지정한 결과를 반환하도록

// verify { ... } : 정상적인 경우 실제로 메소드가 호출 됐는지 확인 ex) 저장, 삭제, 외부 api 호출 여부, 특정 조건에만 호출되는 메소드
class UserServiceTest : BehaviorSpec({
    val userRepository = mockk<UserRepository>(relaxed = true)
    val userService = UserService(userRepository)

    Given("사용자 생성 요청이 주어졌을 때") {
        val validRequest = CreateUserRequest("nam", "0000")

        When("중복된 userID가 존재하는 경우") {
            Then("DUPLICATE_USERID 예외가 발생한다.") {
                every { userRepository.existsByUserID("nam") } returns true

                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.createUser(validRequest)
                }
                exception.error shouldBe CustomException.DUPLICATE_USERID
            }
        }
        When("중복된 userID가 존재하지 않지만 잘못된 userPW가 주어진 경우") {
            val badPwRequest = CreateUserRequest("nam", "")

            Then("INVALID_PASSWORD 예외가 발생한다.") {
                every { userRepository.existsByUserID("nam") } returns false

                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.createUser(badPwRequest)
                }
                exception.error shouldBe CustomException.INVALID_PASSWORD
            }
        }
        When("중복된 userID가 존재하지 않고 올바른 userPW가 주어진 경우") {
            val slot = slot<User>()

            Then("사용자를 생성하고 저장한다.") {
                every { userRepository.existsByUserID("nam") } returns false
                every { userRepository.save(capture(slot)) } returns User(1L, "nam", "0000")

                val result = userService.createUser(validRequest)

                result.userID shouldBe "nam"

                // save가 호출 됐는지
                verify(exactly = 1) { userRepository.save(any()) }

                // 잘 저장됐는지
                slot.captured.userID shouldBe "nam"
                slot.captured.userPW shouldBe "0000"
            }
        }
    }

    Given("사용자 조회 요청이 주어졌을 때") {
        val user = User(1L, "nam", "0000")

        When("user_id가 존재하지 않는 경우") {
            Then("USER_NOT_FOUND 예외가 발생한다.") {
                every { userRepository.findById(9999L) } returns Optional.empty()

                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.getUser(9999L)
                }
                exception.error shouldBe CustomException.USER_NOT_FOUND
            }
        }
        When("user_id가 존재하는 경우") {
            Then("사용자 정보를 반환한다.") {
                every { userRepository.findById(1L) } returns Optional.of(user)

                val result = userService.getUser(1L)
                result.userID shouldBe "nam"
            }
        }
    }

    Given("사용자 비밀번호 변경 요청이 주어졌을 때") {
        val user = User(1L, "nam", "0000")

        When("user_id가 존재하지 않는 경우") {
            Then("USER_NOT_FOUND 예외가 발생한다.") {
                every { userRepository.findById(9999L) } returns Optional.empty()

                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.updateUserPassword(9999L, UpdateUserPasswordRequest("0000", "1111"))
                }
                exception.error shouldBe CustomException.USER_NOT_FOUND
            }
        }
        When("현재 비밀번호가 틀린 경우") {
            Then("INVALID_PASSWORD 예외가 발생한다.") {
                every { userRepository.findById(1L) } returns Optional.of(user)

                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.updateUserPassword(1L, UpdateUserPasswordRequest("wrong", "1111"))
                }
                exception.error shouldBe CustomException.INVALID_PASSWORD
            }
        }

        When("새 비밀번호가 올바르지 않은 경우") {
            Then("INVALID_PASSWORD 예외가 발생한다.") {
                every { userRepository.findById(1L) } returns Optional.of(user)

                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.updateUserPassword(1L, UpdateUserPasswordRequest("0000", ""))
                }
                exception.error shouldBe CustomException.INVALID_PASSWORD
            }
        }
        When("user_id, 현재 비밀번호, 새 비밀번호가 모두 올바르면") {
            Then("비밀번호가 변경된다.") {
                every { userRepository.findById(1L) } returns Optional.of(user)
                every { userRepository.save(any()) } returns user.copy(userPW = "9999")

                val result = userService.updateUserPassword(1L, UpdateUserPasswordRequest("0000", "9999"))
                result.userID shouldBe "nam"
            }
        }
    }

    Given("사용자 삭제 요청이 주어졌을 때") {
        val user = User(1L, "nam", "9999")

        When("user_id가 존재하지 않는 경우") {
            Then("USER_NOT_FOUND 예외가 발생한다.") {
                every { userRepository.findById(9999L) } returns Optional.empty()

                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.deleteUser(9999L)
                }
                exception.error shouldBe CustomException.USER_NOT_FOUND
            }
        }

        When("user_id가 존재하는 경우") {
            Then("사용자가 삭제된다.") {
                every { userRepository.findById(1L) } returns Optional.of(user)

                userService.deleteUser(1L)

                verify { userRepository.delete(user) }
            }
        }
    }
})
