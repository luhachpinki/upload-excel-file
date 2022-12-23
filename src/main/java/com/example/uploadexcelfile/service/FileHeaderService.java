package com.example.uploadexcelfile.service;


import com.example.uploadexcelfile.model.FileHeaders;
import com.example.uploadexcelfile.repository.FileHeaderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FileHeaderService extends BaseService {

    private FileHeaderRepository fileHeaderRepository;

    @Transactional
    public FileHeaders save(final FileHeaders FileHeader) {
        return fileHeaderRepository.save(FileHeader);
    }

    @Transactional
    public void saveAll(final List<FileHeaders> FileHeaders) {
        fileHeaderRepository.saveAll(FileHeaders);
    }
}
