package com.pettoyou.server.domains.healthNote.entity;

import com.pettoyou.server.constant.entity.BaseEntity;
import com.pettoyou.server.constant.enums.CustomResponseStatus;
import com.pettoyou.server.constant.exception.CustomException;
import com.pettoyou.server.domains.healthNote.dto.request.HealthNoteRegistAndModifyReqDto;
import jakarta.persistence.*;
import lombok.*;
import software.amazon.awssdk.annotations.NotNull;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HealthNote extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "health_note_id")
    private Long healthNoteId;

    @NotNull
    @Column(name = "visit_date")
    private LocalDate visitDate;

    @NotNull
    @Column(name = "medical_record")
    private String medicalRecord;

    @Column(name = "caution")
    private String caution;

    @Column(name = "vet_name")
    private String vetName;

    @NotNull
    @Column(name = "store_id")
    private Long storeId;

    @NotNull
    @Column(name = "pet_id")
    private Long petId;

    @NotNull
    @Column(name = "member_id")
    private Long memberId;

    public static HealthNote of(HealthNoteRegistAndModifyReqDto registDto, Long memberId) {
        return HealthNote.builder()
                .visitDate(registDto.visitDate())
                .medicalRecord(registDto.medicalRecord())
                .caution(registDto.caution())
                .vetName(registDto.vetName())
                .storeId(registDto.hospitalId())
                .petId(registDto.petId())
                .memberId(memberId)
                .build();
    }

    public void modifyHealthNote(HealthNoteRegistAndModifyReqDto modifyReqDto) {
        this.visitDate = modifyReqDto.visitDate();
        this.medicalRecord = modifyReqDto.medicalRecord();
        this.caution = modifyReqDto.caution();
        this.vetName = modifyReqDto.vetName();
        this.storeId = modifyReqDto.hospitalId();
        this.petId = modifyReqDto.petId();
    }

    public void validateMemberAuthorization(Long authMemberId) {
        if (!this.memberId.equals(authMemberId)) {
            throw new CustomException(CustomResponseStatus.MEMBER_NOT_MATCH);
        }
    }
}
