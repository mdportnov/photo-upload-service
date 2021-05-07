package com.example.fileuploadservice.controller

import io.jsonwebtoken.Jwts
import javax.servlet.http.HttpServletRequest

const val USER_AUTH_SESSION = "Authorization"

fun getUsernameFromRequest(request: HttpServletRequest, secret: String): String? {
    val token = request.getHeader(USER_AUTH_SESSION)
    val user: String?
    return if (token != null) {
        user = try {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .body
                .subject
        } catch (e: Exception) {
            throw e
        }
        user
    } else null
}

fun getUsernameFromToken(token: String?, secret: String): String {
    var user = ""
    if (token == null) return ""
    return run {
        user = try {
            Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .body
                .subject
        } catch (e: Exception) {
            throw e
        }
        user
    }
}

