package com.example.uploadexcelfile.service;

import com.example.uploadexcelfile.exception.FileUploadException;
import com.example.uploadexcelfile.constants.Constants;
import com.example.uploadexcelfile.dto.FileInfoDTO;
import com.example.uploadexcelfile.enums.FileStatus;
import com.example.uploadexcelfile.model.FileHeaders;
import com.example.uploadexcelfile.model.FileRecords;
import com.example.uploadexcelfile.model.UploadedFile;
import com.example.uploadexcelfile.repository.FileRecordsRepository;
import com.example.uploadexcelfile.utils.FileHelper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.example.uploadexcelfile.enums.FileStatus.*;

@Service
@AllArgsConstructor
@Slf4j
public class UploadFileService extends BaseService {

    private FileService fileService;
    private FileRecordsRepository fileRecordsService;
    private FileHeaderService fileHeaderService;

    @Transactional
    public UploadedFile readExcelFile(InputStream inputStream, String fileName) {
        if (!FilenameUtils.isExtension(fileName, new String[]{"xls", "xlsx"})) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), Constants.FILE_FORMAT_NOT_ACCEPTED);
        }

        try {
            FileInfoDTO FileRecords = FileHelper.excelToFile(inputStream);
            UploadedFile incompleteFile = fileService.save(prepareFileDetails(fileName, IN_PROGRESS));

            if (CollectionUtils.isEmpty(FileRecords.getFileRecordsMap())) {
                throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), Constants.FILE_EMPTY);
            }

            Set<Integer> totalHeaders = FileRecords.getFileRecordsMap().keySet();
            AtomicInteger totalUploadedRecords = new AtomicInteger();

            totalHeaders.forEach(headerIndex -> {
                FileHeaders FileHeader = FileRecords.getFileHeadersMap().get(headerIndex);
                ArrayList<FileRecords> uploadedFileRecords = FileRecords.getFileRecordsMap().get(headerIndex);

                final List<FileRecords> validatedRecords = validateRecords(uploadedFileRecords);

                FileHeaders FileHeaders = fileHeaderService.save(prepareHeader(FileHeader, incompleteFile));
                fileRecordsService.saveAll(prepareFileRecords(validatedRecords, incompleteFile, FileHeaders));
                totalUploadedRecords.addAndGet(validatedRecords.size());
            });
            return fileService.save(fileUploadedSuccessfully(totalUploadedRecords.get(), incompleteFile, totalHeaders.size()));
        } catch (Exception exception) {
            fileService.save(prepareFileDetails(fileName, ERROR_OCCURRED));
            throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), String.format(Constants.FILE_UPLOAD_FAILED, exception.getCause()));
        }
    }

    private List<FileRecords> validateRecords(List<FileRecords> FileRecords) {
        return FileRecords
                .stream()
                .filter(FileRecord -> StringUtils.isNotBlank(FileRecord.getRecordDescription()))
                .collect(Collectors.toList());
    }

    private List<FileRecords> prepareFileRecords(List<FileRecords> FileRecordsList, UploadedFile file, FileHeaders fileHeaders) {
        return FileRecordsList
                .stream()
                .map(book -> book
                        .toBuilder()
                        .file(file)
                        .fileHeaders(fileHeaders)
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * This method is used to prepare file entity details using to save them in db
     * @param fileName : fileName
     * @param fileStatus : fileStatus
     * @return
     */
    public UploadedFile prepareFileDetails(String fileName, FileStatus fileStatus) {
        return new UploadedFile()
                .toBuilder()
                .fileName(fileName)
                .status(fileStatus.name())
                .uploadBy(Math.toIntExact(getAuthenticatedUser().getId()))
                .build();
    }

    /**
     * This method is used to prepare successfully uploaded file response
     * @param totalValidRecords : totalValidRecords
     * @param uploadedFile : uploadedFile
     * @param totalHeaders : totalHeaders
     * @return
     */
    private UploadedFile fileUploadedSuccessfully(int totalValidRecords, UploadedFile uploadedFile, int totalHeaders) {
        return uploadedFile
                .toBuilder()
                .totalUploaded(totalValidRecords)
                .totalRecords(totalValidRecords)
                .totalHeaders(totalHeaders)
                .status(COMPLETED.name())
                .build();
    }

    private FileHeaders prepareHeader(FileHeaders fileHeaders, UploadedFile file) {
        return fileHeaders
                .toBuilder()
                .file(file)
                .build();
    }
}