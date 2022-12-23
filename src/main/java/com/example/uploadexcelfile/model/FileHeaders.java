package com.example.uploadexcelfile.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "header_info")
@NoArgsConstructor
public class FileHeaders extends BaseEntity {

    private String headerName;

    @ManyToOne
    @JoinColumn(name = "file_id")
    @NotNull
    private UploadedFile file;

}