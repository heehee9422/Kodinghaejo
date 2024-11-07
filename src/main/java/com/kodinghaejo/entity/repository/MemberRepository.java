package com.kodinghaejo.entity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberEntity;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

	public Optional<MemberEntity> findByEmailAndIsUse(String email, String isUse);
	public Optional<MemberEntity> findByUsernameAndTelAndIsUse(String username, String tel, String isUse);

}