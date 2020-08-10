package com.wangcong.library.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow,Long> {
    List<Borrow> findAllByUid(Long uid);
}
