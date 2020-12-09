package com.jenkin.simpleshiro.dao;

import com.jenkin.simpleshiro.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole,Integer> {
        List<UserRole> findAllByUserId(Integer userId);
}