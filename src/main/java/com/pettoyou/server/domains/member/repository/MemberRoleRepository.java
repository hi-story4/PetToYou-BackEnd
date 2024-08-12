package com.pettoyou.server.domains.member.repository;

import com.pettoyou.server.domains.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {
}
