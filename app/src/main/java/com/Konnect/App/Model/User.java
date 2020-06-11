package com.Konnect.App.Model;

public class User {
    private  String UserId;
    private  String UserName;
    private  String Bio;
    private  String ImageUrl;
    private  String FullName;

    public User(String userId, String userName, String bio, String imageUrl, String fullName) {
        UserId = userId;
        UserName = userName;
        Bio = bio;
        ImageUrl = imageUrl;
        FullName = fullName;
    }

    public User(){

    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }
}
