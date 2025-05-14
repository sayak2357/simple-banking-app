package com.simplebankingapp.repository;

import com.simplebankingapp.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole,Long> {
    public UserRole findByUserId(long userId);
}
