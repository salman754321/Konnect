package com.Konnect.App.Model;

public class User {
    private  String id;
    private  String userName;
    private  String Bio;
    private  String ImageUrl;
    private  String FullName;

    public User(String id, String userName, String bio, String imageUrl, String fullName) {
        this.id = id;
        this.userName = userName;
        Bio = bio;
        ImageUrl = imageUrl;
        FullName = fullName;
    }

    public User(){

    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
