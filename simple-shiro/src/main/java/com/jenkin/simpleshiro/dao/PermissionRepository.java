package com.jenkin.simpleshiro.dao;

import com.jenkin.simpleshiro.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission,Integer> {

}