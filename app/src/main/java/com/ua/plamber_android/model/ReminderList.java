package com.ua.plamber_android.model;

public class ReminderList {
    public static final String VK = "vk";
    public static final String TWITTER = "twitter";
    public static final String FB_PAGE = "fb_page";
    public static final String APP_RATE = "app_rate";
    public static final String FB_GROUP = "fb_group";
    public static final String DISABLE_ALL = "disabled_all";

    public static class Data {
        private int layout;
        private String nameId;
        private String url;
        private boolean status;

        public Data(int layout, String nameId, String url, boolean status) {
            this.layout = layout;
            this.nameId = nameId;
            this.url = url;
            this.status = status;
        }

        public int getLayout() {
            return layout;
        }

        public String getNameId() {
            return nameId;
        }

        public boolean isStatus() {
            return status;
        }

        public String getUrl() {
            return url;
        }
    }
}
