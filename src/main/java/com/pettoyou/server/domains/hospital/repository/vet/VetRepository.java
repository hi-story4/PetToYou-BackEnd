package com.pettoyou.server.domains.hospital.repository.vet;

import com.pettoyou.server.domains.hospital.entity.vet.Vet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetRepository extends JpaRepository<Vet, Long> {
}
