package com.ua.plamber_android.api.download.type;

import com.ua.plamber_android.api.download.type.enums.FastSpeedEnum;

public class FastCurrentSpeed {
    private float speed;
    private FastSpeedEnum type;

    public FastCurrentSpeed(float speed, FastSpeedEnum type) {
        this.speed = speed;
        this.type = type;
    }

    public float getSpeed() {
        return speed;
    }

    public FastSpeedEnum getType() {
        return type;
    }
}
