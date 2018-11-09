package com.ua.plamber_android.api.download.type;

import com.ua.plamber_android.api.download.type.enums.FastTimeEnum;

public class FastTimeLeft {
    private int leftTime;
    private FastTimeEnum type;

    public FastTimeLeft(int leftTime, FastTimeEnum type) {
        this.leftTime = leftTime;
        this.type = type;
    }

    public int getLeftTime() {
        return leftTime;
    }

    public FastTimeEnum getType() {
        return type;
    }
}
