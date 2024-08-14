package com.pettoyou.server.domains.member.repository;

import com.pettoyou.server.domains.member.entity.Role;
import com.pettoyou.server.domains.member.entity.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleType(RoleType roleType);
}
