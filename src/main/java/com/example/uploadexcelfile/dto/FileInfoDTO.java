package com.example.uploadexcelfile.dto;

import com.example.uploadexcelfile.model.FileHeaders;
import com.example.uploadexcelfile.model.FileRecords;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@Data
public class FileInfoDTO {
    private Map<Integer, FileHeaders> fileHeadersMap = new HashMap<>();
    private Map<Integer, ArrayList<FileRecords>> fileRecordsMap =  new HashMap<>();
}

