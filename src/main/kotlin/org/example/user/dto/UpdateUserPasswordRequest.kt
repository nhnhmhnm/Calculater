package org.example.user.dto

data class UpdateUserPasswordRequest (
    val currentPassword: String,
    val newPassword: String
)
