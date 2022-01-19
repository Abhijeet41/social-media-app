package com.abhi41.socialmediaapp.model;

public class UserStories {
    private String image;
    private long storyAt;

    public UserStories() {
    }

    public UserStories(String image, long storyAt) {
        this.image = image;
        this.storyAt = storyAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    @Override
    public String toString() {
        return "UserStories{" +
                "image='" + image + '\'' +
                ", storyAt=" + storyAt +
                '}';
    }
}
