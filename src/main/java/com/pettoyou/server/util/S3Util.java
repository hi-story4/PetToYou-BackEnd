package com.pettoyou.server.util;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Util {
    private final S3Template s3Template;
    @Value("${aws.s3.bucket}")
    private String bucket;

    public PhotoData uploadFile(MultipartFile file) {
        String fileName =
                UUID.randomUUID() + "." + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata.Builder()
                .contentLength(file.getSize())
                .contentType(file.getContentType())
                .build();

        try (InputStream is = file.getInputStream();) {
            S3Resource upload = s3Template.upload(bucket, fileName, is, metadata);
            return PhotoData.of(
                    upload.getLocation().getBucket(),
                    upload.getLocation().getObject(),
                    upload.getURL().toString()
            );
        } catch (IOException exception) {
            log.error("[S3 Upload Fail] : {}", exception.getMessage());
            throw new CustomException(CustomResponseStatus.S3_UPLOAD_FAIL);
        }
    }

    public void deleteFile(String bucket, String key) {
        s3Template.deleteObject(bucket, key);
    }
}
