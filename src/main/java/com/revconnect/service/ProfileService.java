package com.revconnect.service;

import java.util.Scanner;
import com.revconnect.core.Profile;
import com.revconnect.dao.ProfileDaoImpl;

public class ProfileService {

    private ProfileDaoImpl dao = new ProfileDaoImpl();

    // Returns true if profile saved successfully
    public boolean createOrEditProfile(int userId, Scanner sc) {

        try {
            Profile p = new Profile();
            p.setUserId(userId);

            System.out.print("Name: ");
            p.setName(sc.nextLine());

            System.out.print("Bio: ");
            p.setBio(sc.nextLine());

            System.out.print("Profile Picture Path: ");
            p.setProfilePic(sc.nextLine());

            System.out.print("Location: ");
            p.setLocation(sc.nextLine());

            System.out.print("Website: ");
            p.setWebsite(sc.nextLine());

            dao.saveOrUpdate(p);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public void viewProfile(int userId) {
        dao.viewProfile(userId);
    }
}
