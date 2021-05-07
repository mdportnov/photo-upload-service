package com.example.fileuploadservice.model

class FileResponse(
    var filename: String,
    var url: String,
    var fileType: String? = null,
    var size: Long? = null
) {

}