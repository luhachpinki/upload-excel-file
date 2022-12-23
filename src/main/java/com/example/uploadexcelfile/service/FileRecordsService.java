package com.example.uploadexcelfile.service;


import com.example.uploadexcelfile.model.FileRecords;
import com.example.uploadexcelfile.repository.FileRecordsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FileRecordsService extends BaseService {

    private FileRecordsRepository fileRecordsRepository;

    @Transactional
    public void saveAll(final List<FileRecords> fileRecordsList) {
        fileRecordsRepository.saveAll(fileRecordsList);
    }
}
