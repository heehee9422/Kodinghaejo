package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.FileEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {

}