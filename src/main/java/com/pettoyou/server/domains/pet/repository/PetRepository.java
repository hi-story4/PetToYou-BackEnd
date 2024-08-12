package com.pettoyou.server.domains.pet.repository;

import com.pettoyou.server.domains.pet.entity.Pet;
import com.pettoyou.server.domains.pet.repository.custom.PetCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long>, PetCustomRepository {
}
