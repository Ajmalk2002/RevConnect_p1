
package com.revconnect.dao;
import com.revconnect.core.Profile;
public interface ProfileDao {
 void saveOrUpdate(Profile profile);
 void viewProfile(int userId);
}
