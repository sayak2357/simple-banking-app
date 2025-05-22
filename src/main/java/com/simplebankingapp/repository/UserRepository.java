package com.simplebankingapp.repository;

import com.simplebankingapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    public User findByUsernameAndPassword(String name, String password);
    public User findByUsername(String sername);
}
