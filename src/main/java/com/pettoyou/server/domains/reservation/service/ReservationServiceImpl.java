package com.pettoyou.server.domains.reservation.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.repository.vet.VetRepository;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.reservation.dto.request.ReservationRegistReqDto;
import com.pettoyou.server.domains.reservation.entity.Reservation;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationTimeStatus;
import com.pettoyou.server.domains.reservation.repository.ReservationRepository;
import com.pettoyou.server.domains.reservation.repository.TimeTableRepository;
import com.pettoyou.server.domains.store.entity.Store;
import com.pettoyou.server.domains.store.repository.StoreRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final PetRepository petRepository;
    private final VetRepository vetRepository;
    private final TimeTableRepository timeTableRepository;

    @Override
    public void reservationRegist(
            ReservationRegistReqDto registReqDto,
            Long authMemberId
    ) {
        // Valid 체크
        // 반려동물 Valid 체크
        Pet pet = petRepository.findById(registReqDto.petId()).orElseThrow(
                () -> new CustomException(CustomResponseStatus.PET_NOT_FOUND)
        );
        pet.validateOwnerAuthorization(authMemberId);


        // 수의사 Valid 체크
        vetRepository.findByIdAndHospitalId(registReqDto.vetId(), registReqDto.storeId()).orElseThrow(
                () -> new CustomException(CustomResponseStatus.VET_NOT_FOUND)
        );

        // Date & Time Valid 체크
//        // 해당 병원인지 확인하기 위해 id double 체크.
//        timeTableRepository.findTimeTableAndStoreId(registReqDto.reservationDateTime(), registReqDto.storeId())
//                .orElseThrow(() -> new CustomException(CustomResponseStatus.RESERVATION_ALREADY_EXIST));

        // 예약 저장
        reservationRepository.save(Reservation.of(registReqDto, authMemberId));


    }
}
