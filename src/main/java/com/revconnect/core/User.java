
package com.revconnect.core;
public class User {
 private int userId;
 private String email;
 private String password;
 private UserType userType;
 public int getUserId(){return userId;}
 public void setUserId(int id){this.userId=id;}
 public String getEmail(){return email;}
 public void setEmail(String e){this.email=e;}
 public String getPassword(){return password;}
 public void setPassword(String p){this.password=p;}
 public UserType getUserType(){return userType;}
 public void setUserType(UserType u){this.userType=u;}
 

}
