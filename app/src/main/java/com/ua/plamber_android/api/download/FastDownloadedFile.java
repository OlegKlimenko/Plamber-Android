package com.ua.plamber_android.api.download;

import com.ua.plamber_android.api.download.type.FastCurrentSpeed;
import com.ua.plamber_android.api.download.type.FastTimeLeft;

public class FastDownloadedFile {
    private int fileSize;
    private int persent;
    private float currentSize;
    private float startTime;
    private FileCreateHelper file;
    private FastCurrentSpeed speed;
    private FastTimeLeft timeLeft;

    public void updateData(int fileSize, int persent, float currentSize, FileCreateHelper file, float startTime) {
        this.fileSize = fileSize;
        this.persent = persent;
        this.currentSize = currentSize;
        this.file = file;
        this.startTime = startTime;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getPersent() {
        return persent;
    }

    public void setPersent(int persent) {
        this.persent = persent;
    }

    public float getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(float currentSize) {
        this.currentSize = currentSize;
    }

    public FileCreateHelper getFile() {
        return file;
    }

    public void setFile(FileCreateHelper file) {
        this.file = file;
    }

    public FastCurrentSpeed getSpeed() {
        return speed;
    }

    public void setSpeed(FastCurrentSpeed speed) {
        this.speed = speed;
    }

    public FastTimeLeft getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(FastTimeLeft timeLeft) {
        this.timeLeft = timeLeft;
    }

    public float getStartTime() {
        return startTime;
    }
}
