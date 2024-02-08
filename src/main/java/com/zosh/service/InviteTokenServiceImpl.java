package com.zosh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.InviteToken;
import com.zosh.repository.InviteTokenRepository;

@Service
public class InviteTokenServiceImpl implements InviteTokenService {
	@Autowired
	private InviteTokenRepository inviteTokenRepository;
 
	@Override
	public InviteToken addToken(String token,String userEmail) {
		InviteToken inviteToken = new InviteToken();
		inviteToken.setToken(token);
		inviteToken.setUserEmail(userEmail);
		
		return inviteTokenRepository.save(inviteToken);
	}

	@Override
	public void deleteToken(String token) {
		inviteTokenRepository.deleteByToken(token);

	}

	@Override
	public String getTokenByUserMail(String userEmail) {
	    InviteToken token= inviteTokenRepository.findByUserEmail(userEmail);	
		return token.getToken();
	}

}
