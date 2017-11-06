package com.ua.plamber_android.utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.ua.plamber_android.model.Book;

import java.util.List;

public class RecyclerUserBooksUpdate extends DiffUtil.Callback {

    private List<Book.BookData> oldBooks;
    private List<Book.BookData> newBooks;

    public RecyclerUserBooksUpdate(List<Book.BookData> oldBooks, List<Book.BookData> newBooks) {
        this.oldBooks = oldBooks;
        this.newBooks = newBooks;
    }

    @Override
    public int getOldListSize() {
        return oldBooks.size();
    }

    @Override
    public int getNewListSize() {
        return newBooks.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBooks.get(oldItemPosition).getBookFile()
                .equals(newBooks.get(newItemPosition).getBookFile());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBooks.get(oldItemPosition).equals(newBooks.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
