package com.example.uploadexcelfile.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "file_details")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UploadedFile extends BaseEntity {

    private String fileName;
    private int totalRecords;
    private int totalHeaders;
    private int totalUploaded;
    private String status = null;
    private int uploadBy;
    private int lastReviewedBy;
    private LocalDateTime lastReviewedTime;
}
