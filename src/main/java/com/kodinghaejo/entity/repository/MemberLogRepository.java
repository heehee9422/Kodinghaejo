package com.kodinghaejo.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberLogEntity;
import com.kodinghaejo.entity.MemberLogEntityId;

public interface MemberLogRepository extends JpaRepository<MemberLogEntity, MemberLogEntityId> {

}