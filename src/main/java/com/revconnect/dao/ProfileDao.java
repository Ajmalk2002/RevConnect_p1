package com.revconnect.dao;

import com.revconnect.model.Profile;

public interface ProfileDao {
	void saveOrUpdate(Profile profile);

	void viewProfile(int userId);
}
