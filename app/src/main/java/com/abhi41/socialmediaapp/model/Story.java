package com.abhi41.socialmediaapp.model;

import java.util.List;

public class Story {

        private String storyBy;
        private long storyAt;
        List<UserStories> userStories;

        public Story() {
        }

        public String getStoryBy() {
                return storyBy;
        }

        public void setStoryBy(String storyBy) {
                this.storyBy = storyBy;
        }

        public long getStoryAt() {
                return storyAt;
        }

        public void setStoryAt(long storyAt) {
                this.storyAt = storyAt;
        }

        public List<UserStories> getUserStories() {
                return userStories;
        }

        public void setUserStories(List<UserStories> userStories) {
                this.userStories = userStories;
        }
}
