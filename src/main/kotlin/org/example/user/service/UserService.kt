package org.example.user.service

import org.example.exception.CustomException
import org.example.exception.CustomExceptionWrapper
import org.example.user.domain.User
import org.example.user.dto.CreateUserRequest
import org.example.user.dto.UpdateUserPasswordRequest
import org.example.user.dto.UserResponse
import org.example.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun createUser(request: CreateUserRequest): UserResponse {
        // ID 중복 체크
        if (userRepository.existsByUserID(request.userID)) {
            throw CustomExceptionWrapper(CustomException.DUPLICATE_USERID)
        }
        // PW 체크
        if (request.userPW.isBlank()) {
            throw CustomExceptionWrapper(CustomException.INVALID_PASSWORD)
        }
        val user = User(userID = request.userID, userPW = request.userPW)

        return userRepository.save(user).toResponse()
    }

    fun getUser(id: Long): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { CustomExceptionWrapper(CustomException.USER_NOT_FOUND) }

        return user.toResponse()
    }

    fun updateUserPassword(id: Long, request: UpdateUserPasswordRequest): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { CustomExceptionWrapper(CustomException.USER_NOT_FOUND) }

        if (user.userPW != request.currentPassword) {
            throw CustomExceptionWrapper(CustomException.INVALID_PASSWORD)
        }

        if (request.newPassword.isBlank()) {
            throw CustomExceptionWrapper(CustomException.INVALID_PASSWORD)
        }

        // copy : 기존 객체를 복제하면서 일부 프로퍼티 변경 가능 -> 새로운 객체 생성
        val updated = user.copy(userPW = request.newPassword)
        return userRepository.save(updated).toResponse()
    }

    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { CustomExceptionWrapper(CustomException.USER_NOT_FOUND) }

        userRepository.delete(user)
    }

    // entity 객체를 외부로 직접 반환하지 않고, dto로 변환해서 반환 -> 보안, 필요한 정보만 전달
    private fun User.toResponse() = UserResponse(id = id,  userID = userID, userPW = userPW)
}
