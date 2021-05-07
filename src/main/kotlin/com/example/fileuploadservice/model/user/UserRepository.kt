package com.example.fileuploadservice.model.user

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : CrudRepository<UserDao, Int> {
    fun findByUsername(username: String): UserDao?
}