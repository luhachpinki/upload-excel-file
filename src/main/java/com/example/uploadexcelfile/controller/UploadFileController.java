package com.example.uploadexcelfile.controller;


import com.example.uploadexcelfile.exception.FileUploadException;
import com.example.uploadexcelfile.constants.Constants;
import com.example.uploadexcelfile.model.UploadedFile;
import com.example.uploadexcelfile.service.FileService;
import com.example.uploadexcelfile.service.UploadFileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.uploadexcelfile.enums.FileStatus.ERROR_OCCURRED;

@RestController
@RequestMapping("/file")
@AllArgsConstructor
public class UploadFileController {
    @Autowired
    private UploadFileService uploadFileService;
    private FileService fileService;

    @RequestMapping(method = RequestMethod.POST, path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<UploadedFile> uploadExcelFile(@RequestPart("file") MultipartFile file)  {
        try {
            if (file != null) {
                return ResponseEntity.ok(uploadFileService.readExcelFile(file.getInputStream(), file.getOriginalFilename()));
            } else {
                throw new FileUploadException(HttpStatus.NOT_FOUND.value(), "File not found.");
            }
        } catch (Exception exception) {
            if (file != null) {
                fileService.save(uploadFileService.prepareFileDetails(file.getOriginalFilename(), ERROR_OCCURRED));
            }
            throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), String.format(Constants.FILE_UPLOAD_FAILED, exception.getCause()));
        }
    }
}