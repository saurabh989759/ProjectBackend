package com.zosh.service;

import com.zosh.model.InviteToken;

public interface InviteTokenService {

	public InviteToken addToken(String token,String userEmail);
	public String getTokenByUserMail(String userEmail);
	public void deleteToken(String token);
}
