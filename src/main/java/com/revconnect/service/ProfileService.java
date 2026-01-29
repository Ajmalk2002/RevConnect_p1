package com.revconnect.service;

import java.util.Scanner;

import com.revconnect.core.Profile;
import com.revconnect.core.UserType;
import com.revconnect.dao.ProfileDaoImpl;

public class ProfileService {

    private ProfileDaoImpl dao = new ProfileDaoImpl();

    // Create or update profile based on user type
    public boolean createOrEditProfile(int userId, UserType userType, Scanner sc) {

        try {
            Profile p = new Profile();
            p.setUserId(userId);

            // ================= PERSONAL USER =================
            if (userType == UserType.PERSONAL) {

                System.out.print("Name: ");
                p.setName(sc.nextLine());

                System.out.print("Bio: ");
                p.setBio(sc.nextLine());

                System.out.print("Location: ");
                p.setLocation(sc.nextLine());
                
                System.out.print("Profile Picture Path: ");
                p.setProfilePic(sc.nextLine());


                System.out.print("Website (optional): ");
                p.setWebsite(sc.nextLine());
            }

            // ================= BUSINESS USER =================
            else if (userType == UserType.BUSINESS) {

                System.out.print("Business Name: ");
                p.setName(sc.nextLine());

                System.out.print("Category / Industry (mandatory): ");
                p.setCategory(sc.nextLine());

                System.out.print("Business Address (mandatory): ");
                p.setBusinessAddress(sc.nextLine());

                System.out.print("Contact Information (mandatory): ");
                p.setContactInfo(sc.nextLine());

                System.out.print("Business Description: ");
                p.setBio(sc.nextLine());

                System.out.print("Business Website: ");
                p.setWebsite(sc.nextLine());
                
                System.out.print("Profile Picture Path: ");
                p.setProfilePic(sc.nextLine());


                // Java 8 compatible validation
                if (p.getCategory() == null || p.getCategory().trim().isEmpty()
                        || p.getBusinessAddress() == null || p.getBusinessAddress().trim().isEmpty()) {

                    System.out.println("❌ Category and Business Address are mandatory for Business accounts");
                    return false;
                }
            }

            // ================= CREATOR USER =================
            else if (userType == UserType.CREATOR) {

                System.out.print("Creator Name: ");
                p.setName(sc.nextLine());

                System.out.print("Category (Photography, Tech, etc.): ");
                p.setCategory(sc.nextLine());

                System.out.print("Bio: ");
                p.setBio(sc.nextLine());

                System.out.print("Website / Social Link: ");
                p.setWebsite(sc.nextLine());

               
            }

            dao.saveOrUpdate(p);
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error while saving profile");
            return false;
        }
    }

    public void viewProfile(int userId) {
        dao.viewProfile(userId);
    }
}
