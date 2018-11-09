package com.ua.plamber_android.api.download.type;

import com.ua.plamber_android.api.download.type.enums.FastSpeedEnum;
import com.ua.plamber_android.api.download.type.enums.FastTimeEnum;

public class FastCalculateHelper {

    public static FastCurrentSpeed getCurrentSpeed(float currentSize, float startTime) {
        float speed = currentSize / ((System.nanoTime() - startTime) / FastSpeedEnum.BYTE.getType());
        return new FastCurrentSpeed(speed, FastSpeedEnum.BYTE);
    }

    public static FastTimeLeft getLeftTime(int fileSize, float currentSize, float startTime) {
        float time = (fileSize - currentSize) / getCurrentSpeed(currentSize, startTime).getSpeed();
        if (time < 60) {
            return new FastTimeLeft((int) Math.ceil(time), FastTimeEnum.SECONDS);
        } else if (time >= 60 && time < 3600) {
            return new FastTimeLeft((int) Math.ceil(time) / 60, FastTimeEnum.MINUTES);
        } else
            return new FastTimeLeft((int) Math.ceil(time) / (60 * 60), FastTimeEnum.HOURS);
    }
}
