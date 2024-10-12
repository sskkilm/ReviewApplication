package com.example.reviewapplication.application;

import org.springframework.web.multipart.MultipartFile;

public interface S3Repository {

    void save(MultipartFile file);
}
