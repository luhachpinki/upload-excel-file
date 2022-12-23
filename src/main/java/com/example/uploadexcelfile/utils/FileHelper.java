package com.example.uploadexcelfile.utils;


import com.example.uploadexcelfile.exception.FileUploadException;
import com.example.uploadexcelfile.constants.Constants;
import com.example.uploadexcelfile.dto.FileInfoDTO;
import com.example.uploadexcelfile.model.FileHeaders;
import com.example.uploadexcelfile.model.FileRecords;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FileHelper {

    public static FileInfoDTO excelToFile(InputStream inputStream) {
        try {
            Map<Integer, ArrayList<FileRecords>> columns = new HashMap<>();
            Map<Integer, FileHeaders> headers = new HashMap<>();

            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            AtomicBoolean isFirstRow = new AtomicBoolean(true);
            rows.forEachRemaining(currentRow -> {
                if (isFirstRow.get()) {
                    currentRow.forEach(cell -> {
                        headers.put(cell.getColumnIndex(), new FileHeaders().toBuilder().headerName(cell.getStringCellValue()).build());
                    });
                    isFirstRow.set(false);
                    return;
                }
                currentRow.forEach(cell -> {
                    if (!columns.containsKey(cell.getColumnIndex())) {
                        columns.put(cell.getColumnIndex(),
                                new ArrayList<>(Arrays.asList(
                                        new FileRecords().toBuilder().recordDescription(cell.getStringCellValue()).build())));
                    } else {
                        columns.get(cell.getColumnIndex()).add(new FileRecords().toBuilder().recordDescription(cell.getStringCellValue()).build());
                    }
                });
            });
            FileInfoDTO fileInfoDTO = new FileInfoDTO();
            fileInfoDTO.setFileHeadersMap(headers);
            fileInfoDTO.setFileRecordsMap(columns);
            return fileInfoDTO;

        } catch (Exception e) {
            throw new FileUploadException(HttpStatus.BAD_REQUEST.value(), String.format(Constants.FILE_UPLOAD_FAILED, e.getCause()));
        }
    }
}
