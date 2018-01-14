package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Rating {
    public static class RatingRequest {
        @SerializedName("user_token")
        private String userToken;
        @SerializedName("book_id")
        private long bookId;
        @SerializedName("rating")
        private int rating;

        public RatingRequest(String userToken, long bookId, int rating) {
            this.userToken = userToken;
            this.bookId = bookId;
            this.rating = rating;
        }
    }

    public static class RatingRespond {
        @SerializedName("detail")
        private String detail;
        @SerializedName("data")
        private RatingData ratingData;

        public String getDetail() {
            return detail;
        }

        public RatingData getRatingData() {
            return ratingData;
        }
    }

    public class RatingData {
        @SerializedName("book_rated_count")
        int bookRatedCount;
        @SerializedName("book_rating")
        double bookRating;
    }
}
