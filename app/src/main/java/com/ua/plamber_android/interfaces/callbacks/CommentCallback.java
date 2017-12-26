package com.ua.plamber_android.interfaces.callbacks;

import android.support.annotation.NonNull;

import com.ua.plamber_android.model.Comment;
import com.ua.plamber_android.model.LoadMoreBook;

public interface CommentCallback {
    void onSuccess(@NonNull Comment.CommentRespond comment);

    void onError(@NonNull Throwable t);
}
