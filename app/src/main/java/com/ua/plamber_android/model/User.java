package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class User {

    public static class UserRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("username")
        private String username;

        @SerializedName("password")
        private String password;

        public UserRequest(String appKey, String username, String password) {
            this.appKey = appKey;
            this.username = username;
            this.password = password;
        }
    }

    public static class ProfileRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("user_token")
        private String token;

        public ProfileRequest(String appKey, String token) {
            this.appKey = appKey;
            this.token = token;
        }
    }

    public static class ProfileRespond {
        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private Profile data;

        public String getDetail() {
            return detail;
        }

        public Profile getData() {
            return data;
        }
    }

    public static class UserRespond {
        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private UserData data;

        public String getDetail() {
            return detail;
        }

        public UserData getData() {
            return data;
        }
    }

    public class UserData {
        @SerializedName("token")
        private String token;

        public String getToken() {
            return token;
        }
    }

    public class Profile {
        @SerializedName("profile")
        private ProfileData profile;

        public ProfileData getProfile() {
            return profile;
        }
    }


    public static class ProfileData {
        @SerializedName("id")
        private long userId;

        @SerializedName("username")
        private String userName;

        @SerializedName("user_photo")
        private String userPhotoUrl;

        @SerializedName("email")
        private String userEmail;

        public long getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserPhotoUrl() {
            return userPhotoUrl;
        }

        public String getUserEmail() {
            return userEmail;
        }
    }
}
