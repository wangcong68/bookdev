package com.wangcong.library.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAllByTel(String tel);
    User findByUsername(String username);
    User findByTelAndPassword(String username, String password);
    User findByTel(String tel);
}
