package com.ua.plamber_android.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ua.plamber_android.api.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.api.interfaces.callbacks.CurrentCategoryCallback;
import com.ua.plamber_android.api.interfaces.callbacks.ManageBookCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.CategoryBook;
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
            final Book.BookRequest book = new Book.BookRequest(tokenUtils.readToken());
            Call<Book.BookRespond> request = apiUtils.initializePlamberAPI().getBooks(book, url);
            request.enqueue(new Callback<Book.BookRespond>() {
                @Override
                public void onResponse(Call<Book.BookRespond> call, Response<Book.BookRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().getBookData());
                    }
                }

                @Override
                public void onFailure(Call<Book.BookRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void getBooksFromCategory(final CurrentCategoryCallback callback, int pageNumber, long idCategory) {
        CategoryBook.CategoryBookRequest category = new CategoryBook.CategoryBookRequest(tokenUtils.readToken(), pageNumber, idCategory);
        final Call<CategoryBook.CategoryBookRespond> request = apiUtils.initializePlamberAPI().getCurrentCategory(category);

        request.enqueue(new Callback<CategoryBook.CategoryBookRespond>() {
            @Override
            public void onResponse(Call<CategoryBook.CategoryBookRespond> call, Response<CategoryBook.CategoryBookRespond> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<CategoryBook.CategoryBookRespond> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void manageBookInLibrary(final ManageBookCallback callback, long bookId, String manageUrl) {
        if (callback != null) {
            Book.BookDetailRequest book = new Book.BookDetailRequest(tokenUtils.readToken(), bookId);
            Call<Book.BookDetailRespond> request = apiUtils.initializePlamberAPI().manageBookInLibrary(book, manageUrl);
            request.enqueue(new Callback<Book.BookDetailRespond>() {
                @Override
                public void onResponse(Call<Book.BookDetailRespond> call, Response<Book.BookDetailRespond> response) {
                    if (response.isSuccessful() && response.body().getStatus() == 200) {
                        callback.onSuccess(true);
                    } else {
                        callback.onSuccess(false);
                    }
                }

                @Override
                public void onFailure(Call<Book.BookDetailRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }
}
