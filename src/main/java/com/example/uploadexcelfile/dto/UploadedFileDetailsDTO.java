package com.example.uploadexcelfile.dto;

import com.example.uploadexcelfile.model.FileHeaders;
import com.example.uploadexcelfile.model.FileRecords;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class UploadedFileDetailsDTO {
    private String fileName;
    private int totalRecords;
    private int totalHeaders;
    private int totalUploaded;
    private String status = null;
    private Boolean deleted = Boolean.FALSE;
    private int uploadBy;
    private int lastReviewedBy;
    private LocalDateTime lastReviewedTime;
    private List<FileHeaders> fileHeadersList;
    private List<FileRecords> fileRecordsList;
}
