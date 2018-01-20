package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Account {

    public static class LoginRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("username")
        private String username;

        public LoginRequest(String username, String appKey) {
            this.username = username;
            this.appKey = appKey;
        }
    }

    public static class LoginRespond {
        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private LoginData data;

        public String getDetail() {
            return detail;
        }

        public LoginData getData() {
            return data;
        }
    }

    public class LoginData {
        @SerializedName("user")
        private boolean loginStatus;

        public boolean isLoginStatus() {
            return loginStatus;
        }
    }

    public static class EmailRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("email")
        private String email;

        public EmailRequest(String appKey, String email) {
            this.appKey = appKey;
            this.email = email;
        }
    }

    public static class EmailRespond {

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private EmailData data;


        public String getDetail() {
            return detail;
        }

        public EmailData getData() {
            return data;
        }
    }

    public class EmailData {
        @SerializedName("email")
        private boolean emailStatus;


        public boolean isEmailStatus() {
            return emailStatus;
        }
    }

    public static class RegisterRequest {
        @SerializedName("app_key")
        private String appKey;

        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("passw1")
        private String password;

        public RegisterRequest(String appKey, String username, String email, String password) {
            this.appKey = appKey;
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
}
