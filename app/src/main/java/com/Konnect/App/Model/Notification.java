package com.Konnect.App.Model;

public class Notification {
    String userid;
    String text;
    String postid;
    boolean ispost;
    public Notification(String userid, String text, String postid, boolean isPost) {
        this.userid = userid;
        this.text = text;
        this.postid = postid;
        this.ispost = isPost;
    }

    public Notification() {
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isIspost() {
        return ispost;
    }

    public void setIspost(boolean ispost) {
        this.ispost = ispost;
    }
}
