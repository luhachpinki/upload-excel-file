package com.example.uploadexcelfile.controller;


import com.example.uploadexcelfile.model.UploadedFile;
import com.example.uploadexcelfile.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/files")
@AllArgsConstructor
public class FileStatusController {

    private final FileService fileService;

    @RequestMapping(method = RequestMethod.GET, path = "/status/{fileId}")
    public ResponseEntity<?> checkUploadedFileStatus(@PathVariable("fileId") int uploadedFileId) {
        try {
            return ResponseEntity.ok(fileService.checkUploadedFileStatus(uploadedFileId));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{fileId}")
    public ResponseEntity<?> getUploadedFileById(@PathVariable("fileId") long uploadedFileId) {
        try {
            return ResponseEntity.ok(fileService.getUploadedFileDetailsById(uploadedFileId));
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().body(exception.getMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/list")
    public ResponseEntity<List<UploadedFile>> getAllUploadedFiles() {
        return ResponseEntity.ok(fileService.listAllUploadedFiles());
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/delete/{fileId}")
    public void deleteFileById(@PathVariable("fileId") int uploadedFileId) {
        fileService.deleteUploadedFile(uploadedFileId);
    }

}