package com.example.fileuploadservice

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "file")
class FileStorageProperties {
    lateinit var uploadDir: String
}