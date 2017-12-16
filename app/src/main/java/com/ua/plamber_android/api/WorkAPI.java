package com.ua.plamber_android.api;

import android.content.Context;
import android.util.Log;

import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.interfaces.callbacks.StringListCallback;
import com.ua.plamber_android.interfaces.callbacks.LoadMoreCallback;
import com.ua.plamber_android.interfaces.callbacks.ManageBookCallback;
import com.ua.plamber_android.model.AutoComplete;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Language;
import com.ua.plamber_android.model.LoadMoreBook;
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
    private static final String TAG = "WorkAPI";

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

    public void getBooksFromCategory(final LoadMoreCallback callback, int pageNumber, long idCategory) {
        LoadMoreBook.LoadMoreRequestCategory category = new LoadMoreBook.LoadMoreRequestCategory(tokenUtils.readToken(), pageNumber, idCategory);
        final Call<LoadMoreBook.LoadMoreBookRespond> request = apiUtils.initializePlamberAPI().getCurrentCategory(category);

        request.enqueue(new Callback<LoadMoreBook.LoadMoreBookRespond>() {
            @Override
            public void onResponse(Call<LoadMoreBook.LoadMoreBookRespond> call, Response<LoadMoreBook.LoadMoreBookRespond> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<LoadMoreBook.LoadMoreBookRespond> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void searchBook(final LoadMoreCallback callback, int pageNumber, String term) {
        LoadMoreBook.LoadMoreRequestSearch search = new LoadMoreBook.LoadMoreRequestSearch(tokenUtils.readToken(), term, pageNumber);
        final Call<LoadMoreBook.LoadMoreBookRespond> request = apiUtils.initializePlamberAPI().searchBook(search);
        request.enqueue(new Callback<LoadMoreBook.LoadMoreBookRespond>() {
            @Override
            public void onResponse(Call<LoadMoreBook.LoadMoreBookRespond> call, Response<LoadMoreBook.LoadMoreBookRespond> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<LoadMoreBook.LoadMoreBookRespond> call, Throwable t) {
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

    public void autoCompleteAuthor(final StringListCallback callback, String part) {
        if (callback != null) {
            final AutoComplete.AuthorRequest complete = new AutoComplete.AuthorRequest(tokenUtils.readToken(), part);
            Call<AutoComplete.AuthorRespond> request = apiUtils.initializePlamberAPI().generateAuthor(complete);
            request.enqueue(new Callback<AutoComplete.AuthorRespond>() {
                @Override
                public void onResponse(Call<AutoComplete.AuthorRespond> call, Response<AutoComplete.AuthorRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<AutoComplete.AuthorRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void autoCompleteBook(final BooksCallback callback, String part) {
        if (callback != null) {
            final AutoComplete.BookRequest complete = new AutoComplete.BookRequest(tokenUtils.readToken(), part);
            Call<Book.BookRespond> request = apiUtils.initializePlamberAPI().generateBooks(complete);
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

    public void getAllLanguage(final StringListCallback callback) {
        if (callback != null) {
            final Language.LanguageRequest language = new Language.LanguageRequest(tokenUtils.readToken());
            final Call<Language.LanguageRespond> request = apiUtils.initializePlamberAPI().getLanguage(language);
            request.enqueue(new Callback<Language.LanguageRespond>() {
                @Override
                public void onResponse(Call<Language.LanguageRespond> call, Response<Language.LanguageRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().getData());
                    } else {
                        Log.i(TAG, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Language.LanguageRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }
}
