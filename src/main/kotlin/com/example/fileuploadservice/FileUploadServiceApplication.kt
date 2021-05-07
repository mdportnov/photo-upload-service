package com.example.fileuploadservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class FileUploadServiceApplication

fun main(args: Array<String>) {
    runApplication<FileUploadServiceApplication>(*args)
}
