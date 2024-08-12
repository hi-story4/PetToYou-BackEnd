package com.pettoyou.server.domains.hospital.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.repository.TagMapperRepository;
import com.pettoyou.server.domains.hospital.dto.request.HospitalDto;
import com.pettoyou.server.domains.hospital.dto.request.HospitalQueryAddressInfo;
import com.pettoyou.server.domains.hospital.dto.request.HospitalQueryCond;
import com.pettoyou.server.domains.hospital.dto.request.HosptialSearchQueryInfo;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDetail;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDtoWithAddress;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDtoWithDistance;
import com.pettoyou.server.domains.hospital.entity.Hospital;
import com.pettoyou.server.domains.hospital.entity.HospitalTag;
import com.pettoyou.server.domains.hospital.entity.TagMapper;
import com.pettoyou.server.domains.hospital.repository.HospitalRepository;
import com.pettoyou.server.domains.hospital.repository.HospitalTagRepository;
import com.pettoyou.server.domains.photo.entity.PhotoData;
import com.pettoyou.server.domains.photo.service.PhotoService;
import com.pettoyou.server.domains.store.entity.StorePhoto;
import com.pettoyou.server.domains.store.repository.StorePhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.*;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class HospitalServiceImpl implements HospitalService {
    private final TagMapperRepository tagMapperRepository;
    private final HospitalTagRepository hospitalTagRepository;
    private final HospitalRepository hospitalRepository;
    private final StorePhotoRepository storePhotoRepository;
    private final PhotoService photoService;

    @Override
    public Page<HospitalDtoWithDistance> getHospitalsTest(Pageable pageable, HospitalQueryAddressInfo queryInfo, HospitalQueryCond queryCond) {
        return hospitalRepository.findHospitalOptimization(
                pageable,
                getDayOfWeekNum(),
                queryInfo.toPointString(),
                LocalTime.now(),
                queryCond
        );
    }

    @Override
    public Page<HospitalDtoWithAddress> getHospitalSearch(Pageable pageable, HosptialSearchQueryInfo queryInfo) {
        return hospitalRepository.findHospitalBySearch(pageable, queryInfo, getDayOfWeekNum());
    }

    // 병원 상세 조회
    @Override
    public HospitalDetail getHospitalDetail(Long hospitalId) {
//        return hospitalRepository.findHospitalDetailById(hospitalId);
        Hospital hospital = hospitalRepository.findById(hospitalId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.STORE_NOT_FOUND));

        List<HospitalTag> tagList = hospitalRepository.findTagList(hospitalId);

        return HospitalDetail.from(hospital, tagList);

    }

    @Override
    public String registerHospital(List<MultipartFile> hospitalImg, MultipartFile storeInfoImg, MultipartFile thumbnailImg, HospitalDto hospitalDto) {

        PhotoData thumbnail = photoService.handleThumbnail(thumbnailImg);
        Hospital hospital = hospitalWithStoreInfoImg
                (storeInfoImg, hospitalDto, thumbnail);
        // 테스트 코드용.
        Long savedHospitalId = hospitalRepository.save(hospital).getStoreId();
//         hospitalRepository.save(hospital);

        if (hospitalImg != null && !hospitalImg.isEmpty()) {
            List<StorePhoto> storePhotoList = photoService.handleHospitalImgs(hospitalImg, hospital);
            hospital.getStorePhotos().addAll(storePhotoList);
            storePhotoRepository.saveAll(storePhotoList);
        }

        if (hospitalDto.tagIdList() != null && !hospitalDto.tagIdList().isEmpty()) {
            List<TagMapper> tagMapperList = handleTags(hospital, hospitalDto.tagIdList());
            hospital.getTags().addAll(tagMapperList);
            //병원 객체에 저장
            tagMapperRepository.saveAll(tagMapperList);

        }

        if (savedHospitalId == null) {
            throw new CustomException(CustomResponseStatus.STORE_SAVE_FAIL);
        }

        return savedHospitalId.toString();
//        return  hospital.getStoreId().toString();
    }

    public Hospital hospitalWithStoreInfoImg
            (MultipartFile storeInfoImg, HospitalDto hospitalDto, PhotoData thumbnail) {
        if ((storeInfoImg != null) && (!storeInfoImg.isEmpty())) {
            PhotoData photoData = photoService.uploadImage(storeInfoImg);
            //thumbnail은 null일 수 있다.
            return HospitalDto.toHospitalEntity(hospitalDto, thumbnail, photoData);
        } else {
            return HospitalDto.toHospitalEntity(hospitalDto, thumbnail);
        }
    }

    public List<TagMapper> handleTags(Hospital hospital, List<Long> tagIds) {
        List<HospitalTag> tags = hospitalTagRepository.findAllById(tagIds);
        return HospitalTag.toEntity(hospital, tags);
    }

    // Get 요일 숫자 데이터 1~7
    public Integer getDayOfWeekNum() {
        // UTC 타임존으로 현재 날짜를 가져옵니다.
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate().getDayOfWeek().getValue();
    }
}
