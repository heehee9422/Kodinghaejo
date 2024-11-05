package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.TestBookmarkEntity;
import com.kodinghaejo.entity.TestBookmarkEntityID;

public interface TestBookmarkRepository extends JpaRepository<TestBookmarkEntity, TestBookmarkEntityID> {

}