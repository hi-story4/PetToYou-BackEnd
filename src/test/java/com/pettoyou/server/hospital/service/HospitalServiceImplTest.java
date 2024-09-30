//package com.pettoyou.server.domains.hospital.service;
//
//import com.pettoyou.server.constant.enums.BaseStatus;
//import com.pettoyou.server.constant.enums.CustomResponseStatus;
//import com.pettoyou.server.constant.exception.CustomException;
//import com.pettoyou.server.domains.hospital.dto.request.HospitalDto;
//import com.pettoyou.server.domains.hospital.dto.response.HospitalDetail;
//import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
//import com.pettoyou.server.domains.hospital.entity.hospital.HospitalTag;
//import com.pettoyou.server.domains.hospital.entity.hospital.TagMapper;
//import com.pettoyou.server.domains.hospital.entity.enums.HospitalTagType;
//import com.pettoyou.server.domains.hospital.repository.hospital.HospitalRepository;
//import com.pettoyou.server.domains.hospital.repository.hospital.HospitalTagRepository;
//import com.pettoyou.server.domains.hospital.repository.hospital.TagMapperRepository;
//import com.pettoyou.server.domains.hospital.service.HospitalServiceImpl;
//import com.pettoyou.server.domains.photo.entity.PhotoData;
//import com.pettoyou.server.domains.photo.service.PhotoService;
//import com.pettoyou.server.domains.store.dto.BusinessHourDto;
//import com.pettoyou.server.domains.store.dto.RegistrationInfoDto;
//import com.pettoyou.server.domains.store.dto.request.AddressDto;
//import com.pettoyou.server.domains.store.entity.Address;
//import com.pettoyou.server.domains.store.entity.BusinessHour;
//import com.pettoyou.server.domains.store.entity.RegistrationInfo;
//import com.pettoyou.server.domains.store.entity.StorePhoto;
//import com.pettoyou.server.domains.store.entity.enums.StoreType;
//import com.pettoyou.server.domains.store.repository.StorePhotoRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.sql.Time;
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.Mockito.*;
//
//
//@ExtendWith(MockitoExtension.class)
//class HospitalServiceImplTest {
//
//    @Mock
//    private HospitalRepository hospitalRepository;
//
//    @Mock
//    private TagMapperRepository tagMapperRepository;
//
//    @Mock
//    private StorePhotoRepository storePhotoRepository;
//
//    @Mock
//    private HospitalTagRepository hospitalTagRepository;
//
//    @InjectMocks
//    private HospitalServiceImpl hospitalServiceImpl;
//
//    @Mock
//    private PhotoService photoService;
//
//    Long hospitalId = 1L;
//
//    HospitalTag tag1 = HospitalTag.builder()
//            .tagType(HospitalTagType.SERVICE)
//            .tagContent("병원")
//            .hospitalTagId(1L)
//            .build();
//    HospitalTag tag2 = HospitalTag.builder()
//            .tagType(HospitalTagType.SERVICE)
//            .tagContent("미용")
//            .hospitalTagId(1L)
//            .build();
//
//    List<HospitalTag> tagList = Arrays.asList(tag1, tag2);
//
//    Hospital hospital = Hospital.builder()
//            .storeId(1L)
//            .storeName("hospital")
//            .additionalServiceTag("zero-pay")
//            .build();
//    Hospital noRegInfo = Hospital.builder()
//            .storeId(2L)
//            .storeName("hospital2")
//            .thumbnail(new PhotoData("bucket", "object", "photoUrl"))
//            .storePhone("010-1234-1234")
//            .notice("notice")
//            .websiteLink("website")
//            .additionalServiceTag("additionalServicetags")
//            .storeInfo("storeInfo")
//            .storeInfoPhoto(new PhotoData("bucket", "object", "photoUrl"))
//            .address(new Address("zipCode", "addressDetail", "sido", "sigungu", "eupmyun", "doro", null))
//            .businessHours(Arrays.asList(new BusinessHour(1L, 1, Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), null, null, true, hospital)))
//            .registrationInfo(null)
//            .build();
//    Hospital hospitalSuccess = Hospital.builder()
//            .storeId(3L)
//            .storeName("hospital2")
//            .thumbnail(new PhotoData("bucket", "object", "photoUrl"))
//            .storePhone("010-1234-1234")
//            .notice("notice")
//            .websiteLink("website")
//            .additionalServiceTag("additionalServicetags")
//            .storeInfo("storeInfo")
//            .storeInfoPhoto(new PhotoData("bucket", "object", "photoUrl"))
//            .address(new Address("zipCode", "addressDetail", "sido", "sigungu", "eupmyun", "doro", null))
//            .businessHours(Arrays.asList(new BusinessHour(1L, 1, Time.valueOf("09:00:00"), Time.valueOf("18:00:00"), null, null, true, hospital)))
//            .registrationInfo(RegistrationInfo.builder()
//                    .registrationInfoId(1L)
//                    .ceoName("ceoName")
//                    .ceoPhone("ceoPhone")
//                    .ceoEmail("ceoEmail")
//                    .businessNumber("number").build())
//            .build();
//    RegistrationInfoDto.Request mockRegistrationInfo = mock(RegistrationInfoDto.Request.class);
//    AddressDto addressDto = AddressDto.builder()
//            .zipCode("123-45")
//            .sido("서울특별시")
//            .sigungu("강남구")
//            .eupmyun("청담동")
//            .doro("도산대로")
//            .addressDetail("123-45번지")
//            .latitude(37.523425)
//            .longitude(127.045581)
//            .build();
//
//    HospitalDto hospitalDto = HospitalDto.builder()
//            .hospitalName("hospitalName")
//            .hospitalPhone("hospitalPhone")
//            .businessHours(Arrays.asList(new BusinessHourDto.Request()))
//            .registrationInfo(mockRegistrationInfo)
//            .storeInfo("storeInfo")
//            .tagIdList(new ArrayList<>())
//            .additionalServiceTag("additionalServiceTag")
//            .address(addressDto)
//            .notice("notice")
//            .websiteLink("websiteLink")
//            .tagIdList(List.of(1L, 2L))
//            .build();
//
//    @Test
//    void getHospitalsList() {
//    }
//
//    @Test
//    void searchHospitalsByHospitalName() {
//    }
//
//    @Test
//    void getHospitalDetail_성공() {
//
//
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.ofNullable(hospitalSuccess));
//        when(hospitalRepository.findTagList(hospitalId)).thenReturn(tagList);
//
//        HospitalDetail hospitalDetail = hospitalServiceImpl.getHospitalDetail(hospitalId);
//
//        assertThat(hospitalDetail).isNotNull();
//        // Then
//        assertThat(hospitalSuccess.getStoreId()).isEqualTo(3L);
//        assertThat(hospitalSuccess.getStoreName()).isEqualTo("hospital2");
//        assertThat(hospitalSuccess.getThumbnail()).usingRecursiveComparison().isEqualTo(new PhotoData("bucket", "object", "photoUrl"));
//        assertThat(hospitalSuccess.getStorePhone()).isEqualTo("010-1234-1234");
//        assertThat(hospitalSuccess.getNotice()).isEqualTo("notice");
//        assertThat(hospitalSuccess.getWebsiteLink()).isEqualTo("website");
//        assertThat(hospitalSuccess.getStoreInfo()).isEqualTo("storeInfo");
//        assertThat(hospitalSuccess.getStoreInfoPhoto()).usingRecursiveComparison().isEqualTo(new PhotoData("bucket", "object", "photoUrl"));
//        assertThat(hospitalSuccess.getAddress()).usingRecursiveComparison().isEqualTo(new Address("zipCode", "addressDetail", "sido", "sigungu", "eupmyun", "doro", null));
//
//        List<BusinessHour> businessHours = hospitalSuccess.getBusinessHours();
//        assertThat(businessHours).hasSize(1);
//        BusinessHour businessHour = businessHours.get(0);
//        assertThat(businessHour.getBusinessHourId()).isEqualTo(1L);
//        assertThat(businessHour.getDayOfWeek()).isEqualTo(1);
//        assertThat(businessHour.getStartTime()).isEqualTo(Time.valueOf("09:00:00"));
//        assertThat(businessHour.getEndTime()).isEqualTo(Time.valueOf("18:00:00"));
//        assertThat(businessHour.isOpenSt()).isTrue();
//
//        RegistrationInfo registrationInfo = hospitalSuccess.getRegistrationInfo();
//        assertThat(registrationInfo).isNotNull();
//        assertThat(registrationInfo.getRegistrationInfoId()).isEqualTo(1L);
//        assertThat(registrationInfo.getCeoName()).isEqualTo("ceoName");
//        assertThat(registrationInfo.getCeoPhone()).isEqualTo("ceoPhone");
//        assertThat(registrationInfo.getCeoEmail()).isEqualTo("ceoEmail");
//        assertThat(registrationInfo.getBusinessNumber()).isEqualTo("number");
//
//    }
//
//    @Test
//    void getHospitalDetail_태그없는경우() {
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.ofNullable(hospital));
//        when(hospitalRepository.findTagList(hospitalId)).thenReturn(null);
//        HospitalDetail hospitalDetail = hospitalServiceImpl.getHospitalDetail(hospitalId);
//
//        assertThat(hospitalDetail).isNotNull();
//        assertThat(hospitalDetail.hospitalId()).isEqualTo(hospitalId);
//        assertThat(hospitalDetail.hospitalTags().services()).isEmpty();
//        assertThat(hospitalDetail.hospitalTags().specialities()).isEmpty();
//        assertThat(hospitalDetail.hospitalTags().businessHours()).isEmpty();
//        assertThat(hospitalDetail.hospitalTags().emergency()).isEmpty();
//    }
//
//    @Test
//    void getHospitalDetial_사업자없는경우() {
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.ofNullable(noRegInfo));
//        HospitalDetail hospitalDetail = hospitalServiceImpl.getHospitalDetail(hospitalId);
//
//        assertThat(hospitalDetail).isNotNull();
//        // Then
//        assertThat(noRegInfo.getStoreId()).isEqualTo(2L);
//        assertThat(noRegInfo.getStoreName()).isEqualTo("hospital2");
//        assertThat(noRegInfo.getThumbnail()).usingRecursiveComparison().isEqualTo(new PhotoData("bucket", "object", "photoUrl"));
//        assertThat(noRegInfo.getStorePhone()).isEqualTo("010-1234-1234");
//        assertThat(noRegInfo.getNotice()).isEqualTo("notice");
//        assertThat(noRegInfo.getWebsiteLink()).isEqualTo("website");
//        assertThat(noRegInfo.getStoreInfo()).isEqualTo("storeInfo");
//        assertThat(noRegInfo.getStoreInfoPhoto()).usingRecursiveComparison().isEqualTo(new PhotoData("bucket", "object", "photoUrl"));
//        assertThat(noRegInfo.getAddress()).usingRecursiveComparison().isEqualTo(new Address("zipCode", "addressDetail", "sido", "sigungu", "eupmyun", "doro", null));
//
//        List<BusinessHour> businessHours = noRegInfo.getBusinessHours();
//        assertThat(businessHours).hasSize(1);
//        BusinessHour businessHour = businessHours.get(0);
//        assertThat(businessHour.getBusinessHourId()).isEqualTo(1L);
//        assertThat(businessHour.getDayOfWeek()).isEqualTo(1);
//        assertThat(businessHour.getStartTime()).isEqualTo(Time.valueOf("09:00:00"));
//        assertThat(businessHour.getEndTime()).isEqualTo(Time.valueOf("18:00:00"));
//        assertThat(businessHour.isOpenSt()).isTrue();
//    }
//
//    @Test
//    void getHospitalDetail_영업시간없는경우() {
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.ofNullable(hospital));
//        HospitalDetail hospitalDetail = hospitalServiceImpl.getHospitalDetail(hospitalId);
//
//        assertThat(hospitalDetail).isNotNull();
//        assertThat(hospitalDetail.businessHours()).isEmpty();
//        assertThat(hospitalDetail.hospitalName()).isEqualTo("hospital");
//        // Null 일때 빈 리스트
//        assertThat(hospitalDetail.additionalServiceTag()).isEqualTo("zero-pay");
//    }
//
//    @Test
//    void getHospitalDetail_thumbnail없는경우() {
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.ofNullable(hospital));
//        HospitalDetail hospitalDetail = hospitalServiceImpl.getHospitalDetail(hospitalId);
//
//        assertThat(hospitalDetail).isNotNull();
//        assertThat(hospitalDetail.thumbnailUrl()).isEqualTo("default_url");
//        assertThat(hospitalDetail.additionalServiceTag()).isEqualTo("zero-pay");
//    }
//
//    @Test
//    void getHospitalDetail_병원없는경우() {
//        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.empty());
//        assertThatThrownBy(() -> hospitalServiceImpl.getHospitalDetail(hospitalId))
//                .isInstanceOf(CustomException.class)
//                .hasMessageContaining(CustomResponseStatus.STORE_NOT_FOUND.getMessage());
//    }
//
//    @Test
//    void hospitalWithStoreInfoImg
//() throws Exception {
//        //given
//        MultipartFile storeInfoImg = mock(MultipartFile.class);
//        PhotoData thumbnail = new PhotoData("thumbnail", "thumbnail", "thumbnail");
//        PhotoData storeInfoPhoto = new PhotoData("bucket", "object", "photoUrl");
//        //when
//        when(photoService.uploadImage(any(MultipartFile.class))).thenReturn(storeInfoPhoto);
//        Hospital hospital1 = hospitalServiceImpl.hospitalWithStoreInfoImg
//(storeInfoImg, hospitalDto, thumbnail);
//
//        //then
//        assertThat(hospital1.getAdditionalServiceTag()).isEqualTo("additionalServiceTag");
//        // then
//        assertThat(hospital1.getStoreName()).isEqualTo("hospitalName");
//        assertThat(hospital1.getStorePhone()).isEqualTo("hospitalPhone");
//        assertThat(hospital1.getBusinessHours()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getRegistrationInfo()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getStoreInfo()).isEqualTo("storeInfo");
//        assertThat(hospital1.getTags()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getAdditionalServiceTag()).isEqualTo("additionalServiceTag");
//        assertThat(hospital1.getAddress()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getNotice()).isEqualTo("notice");
//        assertThat(hospital1.getWebsiteLink()).isEqualTo("websiteLink");
//        assertThat(hospital1.getStoreInfoPhoto()).usingRecursiveComparison().isEqualTo(storeInfoPhoto); // Assuming hospitalWithStoreInfoImg
//        assertThat(hospital1.getThumbnail()).usingRecursiveComparison().isEqualTo(thumbnail); //
//    }
//
//    @Test
//    @DisplayName("StoreInfo사진 null 테스트")
//    void hospitalWithStoreInfoImg_storeInfoPhotoIsNull() throws Exception {
//        //given
//        MultipartFile storeInfoImg = mock(MultipartFile.class);
//        PhotoData thumbnail = new PhotoData("thumbnail", "thumbnail", "thumbnail");
//        PhotoData storeInfoPhoto = null;
//
//        //wheng
//        when(photoService.uploadImage(any(MultipartFile.class))).thenReturn(null);
//        Hospital hospital1 = hospitalServiceImpl.hospitalWithStoreInfoImg
//(storeInfoImg, hospitalDto, thumbnail);
//
//        //then
//        assertThat(hospital1.getAdditionalServiceTag()).isEqualTo("additionalServiceTag");
//        assertThat(hospital1.getStoreName()).isEqualTo("hospitalName");
//        assertThat(hospital1.getStorePhone()).isEqualTo("hospitalPhone");
//        assertThat(hospital1.getBusinessHours()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getRegistrationInfo()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getStoreInfo()).isEqualTo("storeInfo");
//        assertThat(hospital1.getTags()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getAdditionalServiceTag()).isEqualTo("additionalServiceTag");
//        assertThat(hospital1.getAddress()).isNotNull(); // Adjust according to the expected value
//        assertThat(hospital1.getNotice()).isEqualTo("notice");
//        assertThat(hospital1.getWebsiteLink()).isEqualTo("websiteLink");
//        assertThat(hospital1.getStoreInfoPhoto()).isNull(); // Should be null as storeInfoPhoto is null
//        assertThat(hospital1.getThumbnail()).usingRecursiveComparison().isEqualTo(thumbnail); //
//    }
//
//
//    @Test
//    void registerHospital_성공() {
//        MultipartFile hospitalImg = new MockMultipartFile("hospitalImg", "hospital.jpg", "image/jpeg", new byte[]{});
//        List<MultipartFile> hospitalImgList = Collections.singletonList(hospitalImg);
//        MultipartFile storeInfoImg = new MockMultipartFile("storeInfoImg", "storeInfo.jpg", "image/jpeg", new byte[]{});
//        MultipartFile thumbnailImg = new MockMultipartFile("thumbnailImg", "thumbnail.jpg", "image/jpeg", new byte[]{});
//
//
//        // Set other necessary fields for hospitalDto
//
//        PhotoData thumbnail = new PhotoData("thumbnail", "thumbnail", "thumbnail");
//        PhotoData storeInfoPhoto = new PhotoData("bucket", "object", "photoUrl");
//
//        List<StorePhoto> storePhotoList = new ArrayList<>();
//        StorePhoto storePhoto = StorePhoto.builder()
//                .photoOrder(1)
//                .storePhoto(storeInfoPhoto)
//                .storePhotoId(1L)
//                .photoStatus(BaseStatus.ACTIVATE)
//                .store(hospitalSuccess).build();
//
//        storePhotoList.add(storePhoto);
//
//        List<TagMapper> tagMapperList = new ArrayList<>();
//        TagMapper tagMapper = TagMapper.builder()
//                .tagMapperId(1L)
//                .hospitalTag(tag1)
//                .hospital(hospitalSuccess)
//                .build();
//        tagMapperList.add(tagMapper);
//
//        // When
//        when(photoService.handleThumbnail(thumbnailImg)).thenReturn(thumbnail);
//        when(photoService.handleHospitalImgs(anyList(), any(Hospital.class))).thenReturn(storePhotoList);
//        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalSuccess);
//        when(hospitalTagRepository.findAllById(anyList())).thenReturn(tagList);
////        when(hospitalServiceImpl.handleTags(any(), anyList())).thenReturn(tagMapperList);
//
//    // Then
//        String result = hospitalServiceImpl.registerHospital(hospitalImgList, storeInfoImg, thumbnailImg, hospitalDto);
//
//        //확인하고 싶은데 save 이후 영속성 컨텍스트에 객체의 아이디가 관리되는데 여기서 실제로 저장하지 않아서 테스트 불가... 추후 진행
//        assertThat(result).isEqualTo(hospitalSuccess.getStoreId().toString());
//
//        verify(hospitalRepository, times(1)).save(any(Hospital.class));
//        verify(storePhotoRepository, times(1)).saveAll(storePhotoList);
//        verify(tagMapperRepository, times(1)).saveAll(anyList());
//    }
//
//    @Test
//    void registerHospital_성공_Thumbnail_NULL() {
//        MultipartFile hospitalImg = new MockMultipartFile("hospitalImg", "hospital.jpg", "image/jpeg", new byte[]{});
//        List<MultipartFile> hospitalImgList = Collections.singletonList(hospitalImg);
//        MultipartFile storeInfoImg = new MockMultipartFile("storeInfoImg", "storeInfo.jpg", "image/jpeg", new byte[]{});
//        MultipartFile thumbnailImg = null;
//
//        // Set other necessary fields for hospitalDto
//
//        PhotoData thumbnail = new PhotoData("thumbnail", "thumbnail", "thumbnail");
//        PhotoData storeInfoPhoto = new PhotoData("bucket", "object", "photoUrl");
//
//        List<StorePhoto> storePhotoList = new ArrayList<>();
//        StorePhoto storePhoto = StorePhoto.builder()
//                .photoOrder(1)
//                .storePhoto(storeInfoPhoto)
//                .storePhotoId(1L)
//                .photoStatus(BaseStatus.ACTIVATE)
//                .store(hospitalSuccess).build();
//
//        storePhotoList.add(storePhoto);
//
//        List<TagMapper> tagMapperList = new ArrayList<>();
//        TagMapper tagMapper = TagMapper.builder()
//                .tagMapperId(1L)
//                .hospitalTag(tag1)
//                .hospital(hospitalSuccess)
//                .build();
//        tagMapperList.add(tagMapper);
//
//        // When
//        when(photoService.handleThumbnail(thumbnailImg)).thenReturn(thumbnail);
//        when(photoService.handleHospitalImgs(anyList(), any(Hospital.class))).thenReturn(storePhotoList);
//        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalSuccess);
//        when(hospitalTagRepository.findAllById(anyList())).thenReturn(tagList);
////        when(hospitalServiceImpl.handleTags(any(), anyList())).thenReturn(tagMapperList);
//
//        // Then
//        String result = hospitalServiceImpl.registerHospital(hospitalImgList, storeInfoImg, thumbnailImg, hospitalDto);
//
//        //확인하고 싶은데 save 이후 영속성 컨텍스트에 객체의 아이디가 관리되는데 여기서 실제로 저장하지 않아서 테스트 불가... 추후 진행
//        assertThat(result).isEqualTo(hospitalSuccess.getStoreId().toString());
//
//        verify(hospitalRepository, times(1)).save(any(Hospital.class));
//        verify(storePhotoRepository, times(1)).saveAll(storePhotoList);
//        verify(tagMapperRepository, times(1)).saveAll(anyList());
//    }
//
//    @Test
//    void registerHospital_성공_StoreImags_NULL(){
//        MultipartFile hospitalImg = new MockMultipartFile("hospitalImg", "hospital.jpg", "image/jpeg", new byte[]{});
//        List<MultipartFile> hospitalImgList = null;
//        MultipartFile storeInfoImg = new MockMultipartFile("storeInfoImg", "storeInfo.jpg", "image/jpeg", new byte[]{});
//        MultipartFile thumbnailImg = new MockMultipartFile("thumbnailImg", "thumbnail.jpg", "image/jpeg", new byte[]{});
//
//
//        // Set other necessary fields for hospitalDto
//
//        PhotoData thumbnail = new PhotoData("thumbnail", "thumbnail", "thumbnail");
//        PhotoData storeInfoPhoto = new PhotoData("bucket", "object", "photoUrl");
//
//        List<StorePhoto> storePhotoList = new ArrayList<>();
//        StorePhoto storePhoto = StorePhoto.builder()
//                .photoOrder(1)
//                .storePhoto(storeInfoPhoto)
//                .storePhotoId(1L)
//                .photoStatus(BaseStatus.ACTIVATE)
//                .store(hospitalSuccess).build();
//
//        storePhotoList.add(storePhoto);
//
//        List<TagMapper> tagMapperList = new ArrayList<>();
//        TagMapper tagMapper = TagMapper.builder()
//                .tagMapperId(1L)
//                .hospitalTag(tag1)
//                .hospital(hospitalSuccess)
//                .build();
//        tagMapperList.add(tagMapper);
//
//        // When
//        when(photoService.handleThumbnail(thumbnailImg)).thenReturn(thumbnail);
//        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalSuccess);
//        when(hospitalTagRepository.findAllById(anyList())).thenReturn(tagList);
////        when(hospitalServiceImpl.handleTags(any(), anyList())).thenReturn(tagMapperList);
//
//        // Then
//        String result = hospitalServiceImpl.registerHospital(hospitalImgList, storeInfoImg, thumbnailImg, hospitalDto);
//
//        //확인하고 싶은데 save 이후 영속성 컨텍스트에 객체의 아이디가 관리되는데 여기서 실제로 저장하지 않아서 테스트 불가... 추후 진행
//        assertThat(result).isEqualTo(hospitalSuccess.getStoreId().toString());
//
//        verify(hospitalRepository, times(1)).save(any(Hospital.class));
//        verify(storePhotoRepository, times(0)).saveAll(storePhotoList);
//        verify(tagMapperRepository, times(1)).saveAll(anyList());
//    }
//
//    @Test
//    void registerHospital_성공_StoreInfoPhoto_NULL(){
//        MultipartFile hospitalImg = new MockMultipartFile("hospitalImg", "hospital.jpg", "image/jpeg", new byte[]{});
//        List<MultipartFile> hospitalImgList = Collections.singletonList(hospitalImg);
//        MultipartFile storeInfoImg = null;
//        MultipartFile thumbnailImg = new MockMultipartFile("thumbnailImg", "thumbnail.jpg", "image/jpeg", new byte[]{});
//
//
//        // Set other necessary fields for hospitalDto
//
//        PhotoData thumbnail = new PhotoData("thumbnail", "thumbnail", "thumbnail");
//        PhotoData storeInfoPhoto = new PhotoData("bucket", "object", "photoUrl");
//
//        List<StorePhoto> storePhotoList = new ArrayList<>();
//        StorePhoto storePhoto = StorePhoto.builder()
//                .photoOrder(1)
//                .storePhoto(storeInfoPhoto)
//                .storePhotoId(1L)
//                .photoStatus(BaseStatus.ACTIVATE)
//                .store(hospitalSuccess).build();
//
//        storePhotoList.add(storePhoto);
//
//        List<TagMapper> tagMapperList = new ArrayList<>();
//        TagMapper tagMapper = TagMapper.builder()
//                .tagMapperId(1L)
//                .hospitalTag(tag1)
//                .hospital(hospitalSuccess)
//                .build();
//        tagMapperList.add(tagMapper);
//
//        // When
//        when(photoService.handleThumbnail(thumbnailImg)).thenReturn(thumbnail);
//        when(photoService.handleHospitalImgs(anyList(), any(Hospital.class))).thenReturn(storePhotoList);
//        when(hospitalRepository.save(any(Hospital.class))).thenReturn(hospitalSuccess);
//        when(hospitalTagRepository.findAllById(anyList())).thenReturn(tagList);
////        when(hospitalServiceImpl.handleTags(any(), anyList())).thenReturn(tagMapperList);
//
//        // Then
//        String result = hospitalServiceImpl.registerHospital(hospitalImgList, storeInfoImg, thumbnailImg, hospitalDto);
//
//        //확인하고 싶은데 save 이후 영속성 컨텍스트에 객체의 아이디가 관리되는데 여기서 실제로 저장하지 않아서 테스트 불가... 추후 진행
//        assertThat(result).isEqualTo(hospitalSuccess.getStoreId().toString());
//        verify(hospitalRepository, times(1)).save(any(Hospital.class));
//        verify(storePhotoRepository, times(1)).saveAll(storePhotoList);
//        verify(tagMapperRepository, times(1)).saveAll(anyList());
//    }
//}