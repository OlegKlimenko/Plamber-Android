package com.ua.plamber_android.model;

import com.google.gson.annotations.SerializedName;

public class Account {

    public static class LoginRequest {
        @SerializedName("username")
        private String username;

        public LoginRequest(String username) {
            this.username = username;
        }
    }

    public static class LoginRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private LoginData data;

        public int getStatus() {
            return status;
        }

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
        @SerializedName("email")
        private String email;

        public EmailRequest(String email) {
            this.email = email;
        }
    }

    public static class EmailRespond {
        @SerializedName("status")
        private int status;

        @SerializedName("detail")
        private String detail;

        @SerializedName("data")
        private EmailData data;

        public int getStatus() {
            return status;
        }

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
        @SerializedName("username")
        private String username;

        @SerializedName("email")
        private String email;

        @SerializedName("passw1")
        private String password;

        public RegisterRequest(String username, String email, String password) {
            this.username = username;
            this.email = email;
            this.password = password;
        }
    }
}
