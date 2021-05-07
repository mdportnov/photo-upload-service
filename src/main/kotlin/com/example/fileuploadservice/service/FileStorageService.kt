package com.example.fileuploadservice.service

import com.example.fileuploadservice.FileStorageProperties
import com.example.fileuploadservice.exception.FileNotFoundException
import com.example.fileuploadservice.exception.FileStorageException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.stream.Stream

@Service
class FileStorageService(fileStorageProperties: FileStorageProperties) {
    lateinit var fileStorageLocation: Path
    final var uploadDirection: String = fileStorageProperties.uploadDir
    var logger: Logger = LoggerFactory.getLogger(FileStorageService::class.java)

    init {
        fileStorageLocation = Paths.get(uploadDirection).toAbsolutePath()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw FileStorageException("Could not create dir to upload")
        }
    }

    fun storeFile(path: String, file: MultipartFile): String {
        val filename = path + "/" + StringUtils.cleanPath(file.originalFilename!!)

        try {
            val targetLocation = this.fileStorageLocation.resolve(filename)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            return filename
        } catch (ex: IOException) {
            throw FileStorageException("Could not store file $filename. Try again")
        }
    }

    fun createNewUserDir(username: String) {
        val path = Paths.get("$uploadDirection/$username").toAbsolutePath()
        Files.createDirectories(path)
        logger.info("Root directory for user $username was created: $path")
    }

    fun loadFileAsResource(fileName: String): Resource {
        try {
            val filePath = this.fileStorageLocation.resolve(fileName).normalize()
            val resource = UrlResource(filePath.toUri())
            if (resource.exists())
                return resource
            else
                throw FileNotFoundException("File not found")
        } catch (ex: MalformedURLException) {
            throw FileNotFoundException("File not found")
        }
    }


    fun loadAll(username: String): Stream<Path> {
        val root = Paths.get("$uploadDirection/$username")

        return try {
            Files.walk(root, 1)
                .filter { path: Path -> path != root }
                .map(root::relativize)
        } catch (e: IOException) {
            throw RuntimeException("Could not load the files!")
        }
    }

}