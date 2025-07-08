package org.example.user.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.exception.CustomException
import org.example.exception.CustomExceptionWrapper
import org.example.user.domain.User
import org.example.user.dto.CreateUserRequest
import org.example.user.dto.UpdateUserPasswordRequest
import org.example.user.repository.UserRepository
import java.util.*

class UserServiceTest : BehaviorSpec({
    val userRepository = mockk<UserRepository>(relaxed = true)
    val userService = UserService(userRepository)

    Given("사용자 생성 요청이 주어졌을 때") {
        val request = CreateUserRequest("nam", "0000")

        When("중복된 userID가 존재하는 경우") {
            every { userRepository.existsByUserID("nam") } returns true

            Then("DUPLICATE_USERID 예외가 발생한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.createUser(request)
                }
                exception.error shouldBe CustomException.DUPLICATE_USERID
            }
        }
        When("중복된 userID가 존재하지 않는 경우 & 잘못된 userPW가 주어진 경우") {
            val request = CreateUserRequest("nam", "")
            every { userRepository.existsByUserID("nam") } returns false

            Then("INVALID_PASSWORD 예외가 발생한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.createUser(request)
                }
                exception.error shouldBe CustomException.INVALID_PASSWORD
            }
        }
        When("중복된 userID가 존재하지 않는 경우 & 올바른 userPW가 주어진 경우") {
            every { userRepository.existsByUserID("nam") } returns false
            every { userRepository.save(any()) } returns User(1L, "nam", "0000")

            Then("사용자를 생성한다.") {
                val result = userService.createUser(request)
                result.userID shouldBe "nam"
            }
        }
    }

    Given("사용자 조회 요청이 주어졌을 때") {
        val user = User(1L, "nam", "0000")

        When("user_id가 틀린 경우") {
            every { userRepository.findById(9999L) } returns Optional.empty()

            Then("USER_NOT_FOUND 예외가 발생한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.getUser(9999L)
                }
                exception.error shouldBe CustomException.USER_NOT_FOUND
            }
        }
        When("user_id가 올바른 경우") {
            every { userRepository.findById(1L) } returns Optional.of(user)

            Then("사용자 정보를 반환한다.") {
                val result = userService.getUser(1L)
                result.userID shouldBe "nam"
            }
        }
    }

    Given("사용자 비밀번호 변경 요청이 주어졌을 때") {
        val user = User(1L, "nam", "0000")

        When("user_id가 틀린 경우") {
            every { userRepository.findById(9999L) } returns Optional.of(user)

            Then("USER_NOT_FOUND 예외가 발생한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.getUser(9999L)
                }
                exception.error shouldBe CustomException.USER_NOT_FOUND
            }
        }
        When("user_id가 올바른 경우 & 현재 비밀번호가 틀린 경우") {
            every { userRepository.findById(1L) } returns Optional.of(user)

            Then("INVALID_PASSWORD 예외가 발생한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.updateUserPassword(1L, UpdateUserPasswordRequest("wrong", "1111"))
                }
                exception.error shouldBe CustomException.INVALID_PASSWORD
            }
        }
        When("user_id가 올바른 경우 & 새 비밀번호가 올바르지 않은 경우") {
            every { userRepository.findById(1L) } returns Optional.of(user)

            Then("INVALID_PASSWORD 예외가 발생한다") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.updateUserPassword(1L, UpdateUserPasswordRequest("0000", ""))
                }
                exception.error shouldBe CustomException.INVALID_PASSWORD
            }
        }
        When("user_id, 현재 비밀번호, 새 비밀번호가 모두 올바르면") {
            every { userRepository.findById(1L) } returns Optional.of(user)
            every { userRepository.save(any()) } returns user.copy(userPW = "0000")

            Then("비밀번호가 변경된다.") {
                val result = userService.updateUserPassword(1L, UpdateUserPasswordRequest("0000", "9999"))
                result.userID shouldBe "nam"
            }
        }
    }

    Given("사용자 삭제 요청이 주어졌을 때") {
        val user = User(1L, "nam", "9999")

        When("존재하지 않는 사용자인 경우") {
            every { userRepository.findById(9999L) } returns Optional.empty()

            Then("USER_NOT_FOUND 예외가 발생한다.") {
                val exception = shouldThrow<CustomExceptionWrapper> {
                    userService.deleteUser(9999L)
                }
                exception.error shouldBe CustomException.USER_NOT_FOUND
            }
        }
        When("존재하는 사용자인 경우") {
            every { userRepository.findById(1L) } returns Optional.of(user)
            every { userRepository.delete(user) } returns Unit

            Then("사용자가 삭제된다") {
                userService.deleteUser(1L)
                verify { userRepository.delete(user) }
            }
        }
    }
})
