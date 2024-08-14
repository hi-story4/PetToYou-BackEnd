package com.pettoyou.server.domains.alarm.repository;

import com.pettoyou.server.domains.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
