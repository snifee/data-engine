package com.snifee.data_engine.mongodb.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;

@Data
@Document
@Entity(name = "file_upload_log")
public class FileUploadLog {
    @MongoId
    private ObjectId id;
    private String fileName;
    private String uploadedBy;
    private LocalDate uploadedDate;
    private boolean isProcessed;
}
