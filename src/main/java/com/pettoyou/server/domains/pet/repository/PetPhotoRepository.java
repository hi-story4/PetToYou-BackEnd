package com.pettoyou.server.domains.pet.repository;

import com.pettoyou.server.domains.pet.entity.PetProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetPhotoRepository extends JpaRepository<PetProfilePhoto, Long> {
}
