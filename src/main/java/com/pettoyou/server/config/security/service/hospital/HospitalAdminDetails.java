package com.pettoyou.server.config.security.service.hospital;

import com.pettoyou.server.domains.hospital.entity.hospitalAdmin.HospitalAdmin;
import com.pettoyou.server.domains.hospital.entity.hospitalAdmin.HospitalAdminRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class HospitalAdminDetails implements UserDetails {
    private final HospitalAdmin hospitalAdmin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (HospitalAdminRole role : hospitalAdmin.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getRole().getRoleType().toString()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return hospitalAdmin.getUsername();
    }

    public Long getHospitalAdminId() {
        return hospitalAdmin.getHospitalAdminId();
    }

    public Long getHospitalId() {return hospitalAdmin.getHospitalId();}
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
