package com.pettoyou.server.domains.hospital.service;

import com.pettoyou.server.domains.hospital.dto.request.HospitalDto;
import com.pettoyou.server.domains.hospital.dto.request.HospitalQueryCond;
import com.pettoyou.server.domains.hospital.dto.request.HospitalQueryAddressInfo;
import com.pettoyou.server.domains.hospital.dto.request.HospitalSearchQueryInfo;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDetail;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDtoWithAddress;
import com.pettoyou.server.domains.hospital.dto.response.HospitalDtoWithDistance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HospitalService {

    String registerHospital(
            List<MultipartFile> hospitalImg,
            MultipartFile storeInfoImg,
            MultipartFile thumbnailImg,
            HospitalDto hospitalDto
    );

    /***
     * 필터링 조건에 부합하는 병원들을 조회합니다.
     * @param pageable : 페이징을 위한 페이징 객체입니다.
     * @param queryInfo : 조회시점의 클라이언트의 위치(위도, 경도)입니다.
     * @param queryCond : 조회시 선택한 필터링 정보들입니다.
     * @return : 필터조건에 맞게 조회된 병원들의 리스트를 반환합니다.
     */
    Page<HospitalDtoWithDistance> getHospitalsList(
            Pageable pageable,
            HospitalQueryAddressInfo queryInfo,
            HospitalQueryCond queryCond
    );

    /***
     * 병원이름을 이용하여 병원을 검색합니다.
     * @param pageable : 페이징을 위한 페이징 객체입니다.
     * @param queryInfo : 검색시 입력한 '병원이름' 입니다.
     * @return : 검색 조건에 부합하는 병원들을 반환합니다.
     */
    // 병원 검색. 거리 대신 주소로.
    Page<HospitalDtoWithAddress> searchHospitalsByHospitalName(
            Pageable pageable,
            HospitalSearchQueryInfo queryInfo
    );

    /***
     * 병원 상세 정보를 조회합니다.
     * @param hospitalId : 조회할 병원의 id(PK) 값 입니다.
     * @return : 선택한 병원의 상세 정보를 반환합니다.
     */
    HospitalDetail getHospitalDetail(
            Long hospitalId
    );
}
