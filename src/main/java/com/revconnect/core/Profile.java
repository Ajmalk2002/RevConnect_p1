
package com.revconnect.core;
public class Profile {
 private int userId;
 private String name;
 private String bio;
 private String location;
 private String website;
 private String profilePic;
 public int getUserId(){return userId;}
 public void setUserId(int id){this.userId=id;}
 public String getName(){return name;}
 public void setName(String n){this.name=n;}
 public String getBio(){return bio;}
 public void setBio(String b){this.bio=b;}
 public String getLocation(){return location;}
 public void setLocation(String l){this.location=l;}
 public String getWebsite(){return website;}
 public void setWebsite(String w){this.website=w;}
 public String getProfilePic() {
     return profilePic;
 }
 public void setProfilePic(String profilePic) {
     this.profilePic = profilePic;
 }
}
