package com.example.uploadexcelfile.repository;

import com.example.uploadexcelfile.model.UploadedFile;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends PagingAndSortingRepository<UploadedFile, Long> {

}
