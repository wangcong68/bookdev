package com.wangcong.library.db;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollectionRepository extends JpaRepository<Collection,Long> {
    List<Collection> findAllByUid(Long uid);
}
