package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class User {

    public static class UserRequest {
        @SerializedName("username")
        private String username;

        @SerializedName("password")
        private String password;

        public UserRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class ProfileRequest {
        @SerializedName("user_token")
        private String token;

        public ProfileRequest(String token) {
            this.token = token;
        }
    }

    public static class ProfileRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private Profile data;

        public int getStatus() {
            return status;
        }

        public String getDetail() {
            return detail;
        }

        public Profile getData() {
            return data;
        }
    }

    public static class UserRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private UserData data;

        public int getStatus() {
            return status;
        }

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
