package com.pettoyou.server.domains.photo.service;

import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.entity.StorePhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PhotoService {

        PhotoData handleThumbnail(MultipartFile thumbnailImg);
        PhotoData uploadImage(MultipartFile image);
        List<StorePhoto> handleHospitalImgs(List<MultipartFile> storeImgs, Store store);

}
