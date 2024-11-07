package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.FileEntity;
import com.kodinghaejo.entity.MemberEntity;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
	
	public List<FileEntity> findByFilePrntAndPrntIdxAndIsUse(String filePrnt, Long prntIdx, String isUse);
	public List<FileEntity> findByEmail(MemberEntity email);

}