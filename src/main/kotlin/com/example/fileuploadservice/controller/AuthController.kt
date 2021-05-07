package com.example.fileuploadservice.controller

import com.example.fileuploadservice.exception.UserAlreadyCreatedException
import com.example.fileuploadservice.model.user.UserDTO
import com.example.fileuploadservice.security.jwt.JwtRequest
import com.example.fileuploadservice.security.jwt.JwtResponse
import com.example.fileuploadservice.security.jwt.JwtTokenUtil
import com.example.fileuploadservice.service.FileStorageService
import com.example.fileuploadservice.service.JwtUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin(origins = ["*"], allowedHeaders = ["*"])
class AuthController {
    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @Autowired
    lateinit var userDetailsService: JwtUserDetailsService

    @Autowired
    lateinit var fileStorageService: FileStorageService

    @PostMapping(value = ["/register"])
    @Throws(java.lang.Exception::class)
    fun saveUser(@RequestBody user: UserDTO): ResponseEntity<*>? {
        if (isUsernameAlreadyInUse(user.username))
            throw UserAlreadyCreatedException("User already exists")
        fileStorageService.createNewUserDir(user.username)
        return ResponseEntity.ok(userDetailsService.save(user))
    }

    @PostMapping("/authenticate")
    @Throws(Exception::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtRequest): ResponseEntity<*> {
        authenticate(authenticationRequest.username, authenticationRequest.password)
        val userDetails: UserDetails = userDetailsService
            .loadUserByUsername(authenticationRequest.username)
        val token = jwtTokenUtil.generateToken(userDetails)
        return ResponseEntity.ok(JwtResponse(token))
    }

    fun isUsernameAlreadyInUse(username: String): Boolean {
        var userInDb = true
        if (userDetailsService.userRepository.findByUsername(username) == null) userInDb = false
        return userInDb
    }

    private fun authenticate(username: String, password: String) {
        try {
            authenticationManager.authenticate(UsernamePasswordAuthenticationToken(username, password))
        } catch (e: DisabledException) {
            throw Exception("USER_DISABLED", e)
        } catch (e: BadCredentialsException) {
            throw Exception("INVALID_CREDENTIALS", e)
        }
    }
}