package com.example.fileuploadservice.service

import com.example.fileuploadservice.model.user.UserDao
import com.example.fileuploadservice.model.user.UserDTO
import com.example.fileuploadservice.model.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service


@Service
class JwtUserDetailsService : UserDetailsService {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var bcryptEncoder: PasswordEncoder

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("User not found with username: $username")
        return User(
            user.username, user.password,
            ArrayList()
        )
    }

    fun save(user: UserDTO): UserDao {
        val newUser = UserDao()
        newUser.username = user.username
        newUser.password = bcryptEncoder.encode(user.password)
        return userRepository.save(newUser)
    }
}