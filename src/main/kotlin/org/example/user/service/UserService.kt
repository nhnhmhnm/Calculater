package org.example.user.service

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
        val user = User(userID = request.userID, userPW = request.userPW)

        return userRepository.save(user).toResponse()
    }

    fun getUser(id: Long): UserResponse =
        userRepository.findById(id).orElseThrow().toResponse()

    fun updateUserPassword(id: Long, request: UpdateUserPasswordRequest): UserResponse {
        val user = userRepository.findById(id).orElseThrow()
        val updated = user.copy(userPW = request.newPassword)

        return userRepository.save(updated).toResponse()
    }

    fun deleteUser(id: Long) {
        userRepository.deleteById(id)
    }

    private fun User.toResponse() = UserResponse(id = id,  userID = userID, userPW = userPW)
}
