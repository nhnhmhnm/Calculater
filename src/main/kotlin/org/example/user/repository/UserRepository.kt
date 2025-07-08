package org.example.user.repository

import org.example.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun existsByUserID(userID: String): Boolean
}
