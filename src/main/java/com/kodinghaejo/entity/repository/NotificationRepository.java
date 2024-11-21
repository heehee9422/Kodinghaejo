package com.kodinghaejo.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.NotificationEntity;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

	//이메일과 사용여부로 알림 찾기
	public List<NotificationEntity> findByEmailAndIsUse(MemberEntity email, String isUse);

}