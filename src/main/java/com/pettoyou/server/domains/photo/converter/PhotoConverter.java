package com.pettoyou.server.domains.photo.converter;

import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.entity.StorePhoto;
import com.pettoyou.server.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
@Slf4j
public class PhotoConverter {

    private final S3Util s3Util;

    public List<StorePhoto> StoreImgsToEntity(List<MultipartFile> storeImgs, Store store) {

        List<StorePhoto> photoList = new ArrayList<>();
        int order = 0;
        for (MultipartFile storeImg : storeImgs) {
            ++order;
            PhotoData photoData = s3Util.uploadFile(storeImg);

            StorePhoto storePhoto = StorePhoto.builder()
                    .storePhoto(photoData)
                    .photoOrder(order)
                    .store(store)
                    .build();
            photoList.add(storePhoto);
        }
        return photoList;
    }

    public PhotoData ImgUpload(MultipartFile Img) {
            // 파일 처리 로직

            return s3Util.uploadFile(Img);
    }
}


