package com.example.reviewapplication.infrastructure;

import com.example.reviewapplication.application.S3Repository;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class S3RepositoryImpl implements S3Repository {

    @Override
    public void save(MultipartFile file) {
        // dummy method
    }

}
