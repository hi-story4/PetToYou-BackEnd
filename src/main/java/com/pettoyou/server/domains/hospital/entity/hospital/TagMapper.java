package com.pettoyou.server.domains.hospital.entity.hospital;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "tag_mapper")
public class TagMapper {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagMapperId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hospital_tag_id")
    private HospitalTag hospitalTag;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Hospital hospital;
}
