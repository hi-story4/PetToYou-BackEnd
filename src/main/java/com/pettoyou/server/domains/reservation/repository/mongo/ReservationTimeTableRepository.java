package com.pettoyou.server.domains.reservation.repository.mongo;

import com.pettoyou.server.domains.reservation.entity.ReservationTimeTable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationTimeTableRepository extends MongoRepository<ReservationTimeTable, String> {
}
