package com.ua.plamber_android.api;

import android.content.Context;

import com.ua.plamber_android.api.interfaces.BooksCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkAPI {

    private Context context;
    private TokenUtils tokenUtils;
    private APIUtils apiUtils;

    public WorkAPI(Context context) {
        this.context = context;
        tokenUtils = new TokenUtils(context);
        apiUtils = new APIUtils(context);
    }

    public void getUserBook(final BooksCallback callback, String url) {
        if (callback != null) {
            final List<Book.BookData> booksData = new ArrayList<>();
            final Book.BookRequest book = new Book.BookRequest(tokenUtils.readToken());
            Call<Book.BookRespond> request = apiUtils.initializePlamberAPI().getBooks(book, url);
            request.enqueue(new Callback<Book.BookRespond>() {
                @Override
                public void onResponse(Call<Book.BookRespond> call, Response<Book.BookRespond> response) {
                    if (response.isSuccessful()) {
                        booksData.addAll(response.body().getBookData());
                    }
                    callback.onSuccess(booksData);
                }

                @Override
                public void onFailure(Call<Book.BookRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }
}
