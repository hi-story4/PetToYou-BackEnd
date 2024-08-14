package com.pettoyou.server.domains.photo.service;


import com.pettoyou.server.domains.photo.converter.PhotoConverter;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.entity.StorePhoto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoServiceImpl implements PhotoService {


    private final PhotoConverter photoConverter;


    public PhotoData handleThumbnail(MultipartFile thumbnailImg) {
        if ((thumbnailImg != null) && (!thumbnailImg.isEmpty())) {
            return photoConverter.ImgUpload(thumbnailImg);
        } else {
            return new PhotoData(null, null, null);
            // 기본값 처리하기!!!
            // default_url in builder
        }
    }

    @Override
    public PhotoData uploadImage(MultipartFile image) {
        if ((image != null) && (!image.isEmpty())) {
            return photoConverter.ImgUpload(image);
        } else {
            return new PhotoData(null, null, null);
        }
    }

    @Override
    public List<StorePhoto> handleHospitalImgs(List<MultipartFile> storeImgs, Store store) {
        if ((storeImgs != null) && (!storeImgs.isEmpty())) {

            return  photoConverter.StoreImgsToEntity(storeImgs, store);
        } else {
            return new ArrayList<>();
        }
    }
}
