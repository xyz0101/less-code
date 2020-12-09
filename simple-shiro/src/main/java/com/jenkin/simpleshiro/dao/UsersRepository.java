package com.jenkin.simpleshiro.dao;

import com.jenkin.simpleshiro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<User,Integer> {
    User getByName(String name);
}