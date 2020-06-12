package com.Konnect.App.Model;

public class Post {
    String PostId;
    String PostImage;
    String Description;
    String Publisher;

    public Post() {
    }




    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostImage() {
        return PostImage;
    }

    public void setPostImage(String postImage) {
        PostImage = postImage;
    }

    public Post(String postId, String postImage, String description, String publisher) {
        PostId = postId;
        PostImage = postImage;
        Description = description;
        Publisher = publisher;
    }
}
