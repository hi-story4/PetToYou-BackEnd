package com.pettoyou.server.domains.reservation.service;

import com.pettoyou.server.config.security.service.hospital.HospitalAdminDetails;
import com.pettoyou.server.config.security.service.member.PrincipalDetails;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.hospital.repository.vet.VetRepository;
import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.PetRepository;
import com.pettoyou.server.domains.reservation.dto.request.ReservationRegistReqDto;
import com.pettoyou.server.domains.reservation.dto.request.ReservationStatusReqDto;
import com.pettoyou.server.domains.reservation.entity.Reservation;
import com.pettoyou.server.domains.reservation.entity.enums.ReservationStatus;
import com.pettoyou.server.domains.reservation.repository.ReservationRepository;
import com.pettoyou.server.domains.reservation.service.scheduler.SchedulerService;
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
    private final TimeTableHelperService timeTableHelperService;
    private final SchedulerService schedulerService;

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
//        vetRepository.findByIdAndHospitalId(registReqDto.vetId(), registReqDto.storeId()).orElseThrow(
//                () -> new CustomException(CustomResponseStatus.VET_NOT_FOUND)
//        );
         //Date & Time Valid 체크
        if (timeTableHelperService.timeTableExistsWithVetIdAndDateTime(registReqDto.vetId(), registReqDto.reservationDateTime())) {
            throw new CustomException(CustomResponseStatus.RESERVATION_ALREADY_EXIST);
        }
        // 예약 저장
        Reservation savedReservation = reservationRepository.save(Reservation.of(registReqDto, authMemberId));

        schedulerService.scheduleReservationCompletion(savedReservation);

    }



    public ReservationStatus updateReservationStatusByAdmin(ReservationStatusReqDto reservationStatusReqDto, HospitalAdminDetails hospitalAdminDetails) {
        Reservation reservation = reservationRepository.findById(reservationStatusReqDto.reservationId())
                .orElseThrow(() -> new CustomException(CustomResponseStatus.RESERVATION_NOT_FOUND));

        isEqualIds(reservation.getStoreId(), hospitalAdminDetails.getHospitalId());

        reservation.modifyReserationStatus(reservation, reservationStatusReqDto.reservationStatus());
        return reservation.getReservationStatus();

    }

    public ReservationStatus updateReservationStatusByUser(ReservationStatusReqDto reservationStatusReqDto, PrincipalDetails principalDetails) {
        Reservation reservation = reservationRepository.findById(reservationStatusReqDto.reservationId())
                        .orElseThrow(() -> new CustomException(CustomResponseStatus.RESERVATION_NOT_FOUND));
        isEqualIds(reservation.getMemberId(), principalDetails.getUserId());

        reservation.modifyReserationStatus(reservation, reservationStatusReqDto.reservationStatus());
        return reservation.getReservationStatus();
    }


    private void isEqualIds(Long idFromReservation, Long idFromUser){
        if(!idFromReservation.equals(idFromUser)){
            throw new CustomException(CustomResponseStatus.ACCESS_DENIED);
        }
    }


}
