package com.example.fileuploadservice.exception

class FileStorageException : RuntimeException {
    constructor(msg: String) : super(msg)
    constructor(msg: String, cause: Throwable) : super(msg, cause)
}