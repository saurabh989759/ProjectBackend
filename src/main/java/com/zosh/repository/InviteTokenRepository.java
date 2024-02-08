package com.zosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.InviteToken;

public interface InviteTokenRepository extends JpaRepository<InviteToken, Long> {

	void deleteByToken(String token);

	InviteToken findByUserEmail(String userEmail);



}
