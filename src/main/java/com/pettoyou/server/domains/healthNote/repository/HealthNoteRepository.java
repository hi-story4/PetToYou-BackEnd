package com.pettoyou.server.domains.healthNote.repository;

import com.pettoyou.server.domains.healthNote.entity.HealthNote;
import com.pettoyou.server.domains.healthNote.repository.custom.HealthNoteCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthNoteRepository extends JpaRepository<HealthNote, Long>, HealthNoteCustomRepository {
}
