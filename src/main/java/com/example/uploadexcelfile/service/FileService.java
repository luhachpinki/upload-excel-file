package com.example.uploadexcelfile.service;


import com.example.uploadexcelfile.exception.FileUploadException;
import com.example.uploadexcelfile.dto.UploadedFileDetailsDTO;
import com.example.uploadexcelfile.model.FileHeaders;
import com.example.uploadexcelfile.model.FileRecords;
import com.example.uploadexcelfile.model.UploadedFile;
import com.example.uploadexcelfile.repository.FileHeaderRepository;
import com.example.uploadexcelfile.repository.FileRecordsRepository;
import com.example.uploadexcelfile.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileService extends BaseService {

    private FileRepository fileRepository;
    private FileHeaderRepository fileHeaderRepository;
    private FileRecordsRepository fileRecordsRepository;
    private FileRecordsService fileRecordsService;
    private FileHeaderService fileHeaderService;

    @Transactional
    public UploadedFile save(final UploadedFile uploadedFile) {
        return fileRepository.save(uploadedFile);
    }

    @Transactional(readOnly = true)
    public List<UploadedFile> listAllUploadedFiles() {
        return (List<UploadedFile>) fileRepository.findAll();
    }

    @Transactional
    public void deleteUploadedFile(long uploadedFileId) {
        UploadedFile uploadedFile = getByFileId(uploadedFileId);
        List<FileHeaders> fileHeadersList = fileHeaderRepository.findByFileId(uploadedFileId);
        List<FileHeaders> updatedHeaderList = fileHeadersList
                .stream()
                .map(fileHeader -> fileHeader
                        .toBuilder().deleted(Boolean.TRUE)
                        .updatedDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());
        List<FileRecords> fileRecordsList = fileRecordsRepository.findByFileId(uploadedFileId);
        List<FileRecords> updatedRecordsList = fileRecordsList
                .stream()
                .map(fileHeader -> fileHeader
                        .toBuilder().deleted(Boolean.TRUE)
                        .updatedDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        fileHeaderService.saveAll(updatedHeaderList);
        fileRecordsService.saveAll(updatedRecordsList);
        save(uploadedFile.toBuilder().deleted(Boolean.TRUE).updatedDate(LocalDateTime.now()).build());
    }

    @Transactional(readOnly = true)
    public UploadedFile getByFileId(Long id) throws FileUploadException {
        return fileRepository
                .findById(id)
                .orElseThrow(() -> new FileUploadException(HttpStatus.NOT_FOUND.value(), "File details not found."));
    }

    @Transactional(readOnly = true)
    public UploadedFileDetailsDTO getUploadedFileDetailsById(Long fileId) throws FileUploadException {
        UploadedFile dbUploadedFile = getByFileId(fileId);
        saveUserDetails(dbUploadedFile);
        final List<FileHeaders> fileHeadersList = fileHeaderRepository.findByFileId(fileId);
        final List<FileRecords> fileRecordsList = fileRecordsRepository.findByFileId(fileId);
        return new UploadedFileDetailsDTO().toBuilder().fileName(dbUploadedFile.getFileName()).totalRecords(dbUploadedFile.getTotalRecords())
                .totalHeaders(dbUploadedFile.getTotalHeaders())
                .status(dbUploadedFile.getStatus())
                .deleted(dbUploadedFile.getDeleted())
                .uploadBy(dbUploadedFile.getUploadBy()).totalUploaded(dbUploadedFile.getTotalUploaded()).
                lastReviewedBy(dbUploadedFile.getLastReviewedBy()).lastReviewedTime(dbUploadedFile.getLastReviewedTime()).
                fileHeadersList(fileHeadersList).fileRecordsList(fileRecordsList).
                build();
    }

    private void saveUserDetails(UploadedFile dbUploadedFile) {
        UploadedFile uploadedFile = dbUploadedFile
                .toBuilder()
                .lastReviewedBy(Math.toIntExact(getAuthenticatedUser().getId()))
                .lastReviewedTime(LocalDateTime.now())
                .build();
        fileRepository.save(uploadedFile);
    }

    public UploadedFile checkUploadedFileStatus(long fileId) {
        return fileRepository
                .findById(fileId)
                .orElseThrow(() -> new FileUploadException(HttpStatus.NOT_FOUND.value(),
                        "File details not found."));
    }
}



