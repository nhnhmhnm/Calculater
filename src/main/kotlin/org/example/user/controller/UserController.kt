package org.example.user.controller

import org.example.user.dto.CreateUserRequest
import org.example.user.dto.UpdateUserPasswordRequest
import org.example.user.dto.UserResponse
import org.example.user.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/user")
class UserController(private val userService: UserService) {
    @PostMapping
    fun createUser(@RequestBody request: CreateUserRequest): UserResponse {
        return userService.createUser(request)
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Long): UserResponse {
        return userService.getUser(id)
    }

    @PutMapping("/{id}")
    fun updatePassword(@PathVariable id: Long, @RequestBody request: UpdateUserPasswordRequest): UserResponse {
        return userService.updateUserPassword(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
}
