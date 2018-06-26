package com.ua.plamber_android.utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.ua.plamber_android.database.model.LocalBookDB;

import java.util.List;

public class LocalFilesSortUpdate extends DiffUtil.Callback {
    private List<LocalBookDB> oldFiles;
    private List<LocalBookDB> newFiles;

    public LocalFilesSortUpdate(List<LocalBookDB> oldFiles, List<LocalBookDB> newFiles) {
        this.oldFiles = oldFiles;
        this.newFiles = newFiles;
    }

    @Override
    public int getOldListSize() {
        return oldFiles.size();
    }

    @Override
    public int getNewListSize() {
        return newFiles.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFiles.get(oldItemPosition).getLastReadDate() == newFiles.get(newItemPosition).getLastReadDate();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldFiles.get(oldItemPosition).equals(newFiles.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
