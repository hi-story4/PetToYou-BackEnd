package com.pettoyou.server.domains.hospital.repository.hospital;

import com.pettoyou.server.domains.hospital.entity.hospital.Hospital;
import com.pettoyou.server.domains.hospital.repository.custom.HospitalCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HospitalRepository extends JpaRepository<Hospital, Long>, HospitalCustomRepository {


    @Query("SELECT h.storeName FROM Hospital h WHERE h.storeId = :hospitalId")
    String getHospitalNameNameByStoreId(Long hospitalId);

}
