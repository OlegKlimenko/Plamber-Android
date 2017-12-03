package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    public class CommentRequest {

    }

    public class CommentRespond {

    }

    public class CommentData {
        @SerializedName("user")
        private String userName;

        @SerializedName("user_photo")
        private String userPhotoUrl;

        @SerializedName("text")
        private String commentText;

        @SerializedName("posted_date")
        private String postedDate;

        public String getUserName() {
            return userName;
        }

        public String getUserPhotoUrl() {
            return userPhotoUrl;
        }

        public String getCommentText() {
            return commentText;
        }

        public String getPostedDate() {
            return postedDate;
        }
    }
}
