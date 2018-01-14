package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    public static class CommentRequest {
        @SerializedName("user_token")
        private String userToken;

        @SerializedName("book_id")
        private long bookId;

        @SerializedName("text")
        private String commentText;

        public CommentRequest(String userToken, long bookId, String commentText) {
            this.userToken = userToken;
            this.bookId = bookId;
            this.commentText = commentText;
        }
    }

    public class CommentRespond {
               @SerializedName("detail")
        private String detail;
        @SerializedName("data")
        CommentData commentData;

        public String getDetail() {
            return detail;
        }

        public CommentData getCommentData() {
            return commentData;
        }
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
