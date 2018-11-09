package com.ua.plamber_android.api.download.type.enums;

public enum FastSpeedEnum {
    BYTE(1000 * 1000 * 1000),
    KBYTE(1024 * 1024),
    MBYTE(1024),
    GBYTE(0);

    private int id;

    FastSpeedEnum(int id) {
        this.id = id;
    }

    public int getType() {
        return id;
    }
}
