package com.example.sportnews.pojo;

public class Post {
    private String type;
    private String title;
    private String url;
    private String sectionName;
    private String publishedAt;
    private Author author;

    public Post(String type, String title, String url, String sectionName, String publishedAt,Author author) {
        this.type = type;
        this.title = title;
        this.url = url;
        this.sectionName = sectionName;
        this.publishedAt = publishedAt;
        this.author=author;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }
}

