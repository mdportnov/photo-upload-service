package com.example.fileuploadservice.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class UserAlreadyCreatedException : RuntimeException {
    constructor(msg: String) : super(msg)
    constructor(msg: String, cause: Throwable) : super(msg, cause)
}