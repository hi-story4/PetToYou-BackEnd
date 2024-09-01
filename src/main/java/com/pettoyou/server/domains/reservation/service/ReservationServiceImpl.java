package com.pettoyou.server.domains.reservation.service;

import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.repository.vet.VetRepository;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.reservation.dto.request.ReservationRegistReqDto;
import com.pettoyou.server.domains.reservation.entity.Reservation;
import com.pettoyou.server.domains.reservation.repository.ReservationRepository;
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
    private final StoreRepository storeRepository;
    private final VetRepository vetRepository;

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

        // Store Valid 체크
        Store store = storeRepository.findById(registReqDto.storeId()).orElseThrow(
                () -> new CustomException(CustomResponseStatus.STORE_NOT_FOUND)
        );

        // 수의사 Valid 체크
        vetRepository.findByIdAndHospitalId(registReqDto.vetId(), registReqDto.storeId()).orElseThrow(
                () -> new CustomException(CustomResponseStatus.VET_NOT_FOUND)
        );

        // Date & Time Valid 체크
        Reservation reservation = reservationRepository.findByStoreIdAndReserveDateAndReserveStartAndEndTimeAndReserveStatus(
                store.getStoreId(),
                registReqDto.reservationDate(),
                registReqDto.reservationStartTime(),
                registReqDto.reservationEndTime()
        );
        if (reservation != null) {
            throw new CustomException(CustomResponseStatus.RESERVATION_ALREADY_EXIST);
        }
        // Todo : MongoDB 에서도 존재하는지 파악해야함

        // 예약 저장
        reservationRepository.save(Reservation.of(registReqDto, authMemberId));
    }
}
