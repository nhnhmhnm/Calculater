package org.example.user.repository

import org.example.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    // findById() : JpaRepository<T 엔티티, ID 기본키 타입>에 이미 정의된 기본 메서드

    // findByUserID() : userID가 존재하면 User, 없으면 null/Optimal.empty() 리턴
    // SELECT * FROM users WHERE user_id = ?

    // userID가 존재하면 true, 없으면 false -> 존재 여부만 확인
    // SELECT 1 FROM users WHERE user_id = ?
    fun existsByUserID(userID: String): Boolean
}
