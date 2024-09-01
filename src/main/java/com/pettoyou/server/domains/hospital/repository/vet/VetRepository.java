package com.pettoyou.server.domains.hospital.repository.vet;

import com.pettoyou.server.domains.hospital.entity.vet.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VetRepository extends JpaRepository<Vet, Long> {
    Optional<Vet> findByIdAndHospitalId(Long id, Long hospitalId);
}
