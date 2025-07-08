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

        val updated = user.copy(userPW = request.newPassword)
        return userRepository.save(updated).toResponse()
    }

    fun deleteUser(id: Long) {
        val user = userRepository.findById(id)
            .orElseThrow { CustomExceptionWrapper(CustomException.USER_NOT_FOUND) }

        userRepository.delete(user)
    }

    private fun User.toResponse() = UserResponse(id = id,  userID = userID, userPW = userPW)
}
