package com.simplebankingapp.repository;

import com.simplebankingapp.entity.ApiRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiRoleRepository extends JpaRepository<ApiRole,Long> {
    public ApiRole findByUriAndHttpMethod(String uri, String httpMethod);
}
