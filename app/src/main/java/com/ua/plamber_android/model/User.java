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
}
