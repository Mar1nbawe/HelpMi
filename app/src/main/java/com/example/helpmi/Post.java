package com.example.helpmi;

public class Post {
    int id;
    String title;
    String content;
    String posted_at;
    String username;
    String[][] comments;

    public Post(int id, String title, String content, String posted_at, String username) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.posted_at = posted_at;
        this.username = username;
    }

    public void setComments(String[][] comments) {
        this.comments = comments;
    }
}
