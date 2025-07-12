package com.snifee.data_engine.mongodb.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import org.apache.commons.net.ntp.TimeStamp;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@Document
@Entity(name = "file_upload_log")
public class FileUploadLog {
    @MongoId
    private ObjectId id;
    private String fileName;
    private String uploadedBy;
    private TimeStamp uploadedDate;
    private boolean isProcessed;
}
