package com.ua.plamber_android.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ua.plamber_android.R;
import com.ua.plamber_android.interfaces.callbacks.BookDetailCallback;
import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.interfaces.callbacks.CategoryCallback;
import com.ua.plamber_android.interfaces.callbacks.CommentCallback;
import com.ua.plamber_android.interfaces.callbacks.PageCallback;
import com.ua.plamber_android.interfaces.callbacks.ProfileCallback;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.interfaces.callbacks.StringListCallback;
import com.ua.plamber_android.interfaces.callbacks.LoadMoreCallback;
import com.ua.plamber_android.interfaces.callbacks.ManageBookCallback;
import com.ua.plamber_android.model.AutoComplete;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Comment;
import com.ua.plamber_android.model.Language;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.model.LoadMoreBook;
import com.ua.plamber_android.model.Page;
import com.ua.plamber_android.model.Rating;
import com.ua.plamber_android.model.Support;
import com.ua.plamber_android.model.User;
import com.ua.plamber_android.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkAPI {

    private PreferenceUtils preferenceUtils;
    private APIUtils apiUtils;
    private static final String TAG = "WorkAPI";
    private String appKey;

    public WorkAPI(Context context) {
        appKey = context.getString(R.string.app_key);
        preferenceUtils = new PreferenceUtils(context);
        apiUtils = new APIUtils(context);
    }

    public void getUserBook(final BooksCallback callback, String url) {
        if (callback != null) {
            final Book.BookRequest book = new Book.BookRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN));
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
        LoadMoreBook.LoadMoreRequestCategory category = new LoadMoreBook.LoadMoreRequestCategory(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), pageNumber, idCategory);
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
        LoadMoreBook.LoadMoreRequestSearch search = new LoadMoreBook.LoadMoreRequestSearch(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), term, pageNumber);
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
            Book.BookDetailRequest book = new Book.BookDetailRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), bookId);
            Call<Book.BookDetailRespond> request = apiUtils.initializePlamberAPI().manageBookInLibrary(book, manageUrl);
            request.enqueue(new Callback<Book.BookDetailRespond>() {
                @Override
                public void onResponse(Call<Book.BookDetailRespond> call, Response<Book.BookDetailRespond> response) {
                    if (response.isSuccessful()) {
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
            final AutoComplete.AuthorRequest complete = new AutoComplete.AuthorRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), part);
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
            final AutoComplete.BookRequest complete = new AutoComplete.BookRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), part);
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
            final Language.LanguageRequest language = new Language.LanguageRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN));
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

    public void addRated(final StatusCallback callback, long bookId, int rated) {
        if (callback != null) {
            Rating.RatingRequest rating = new Rating.RatingRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), bookId, rated);
            Call<Rating.RatingRespond> request = apiUtils.initializePlamberAPI().addRating(rating);
            request.enqueue(new Callback<Rating.RatingRespond>() {
                @Override
                public void onResponse(Call<Rating.RatingRespond> call, Response<Rating.RatingRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.code());
                    } else {
                        Log.i(TAG, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Rating.RatingRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void addComment(final CommentCallback callback, long bookId, String text) {
        if (callback != null) {
            Comment.CommentRequest comment = new Comment.CommentRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), bookId, text);
            Call<Comment.CommentRespond> request = apiUtils.initializePlamberAPI().addComment(comment);
            request.enqueue(new Callback<Comment.CommentRespond>() {
                @Override
                public void onResponse(Call<Comment.CommentRespond> call, Response<Comment.CommentRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    } else {
                        Log.i(TAG, response.message());
                    }
                }

                @Override
                public void onFailure(Call<Comment.CommentRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void getProfileData(final ProfileCallback callback) {
        if (callback != null) {
            final User.ProfileRequest profile = new User.ProfileRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN));
            Call<User.ProfileRespond> request = apiUtils.initializePlamberAPI().getProfileData(profile);
            request.enqueue(new Callback<User.ProfileRespond>() {
                @Override
                public void onResponse(Call<User.ProfileRespond> call, Response<User.ProfileRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().getData().getProfile());
                    }
                }

                @Override
                public void onFailure(Call<User.ProfileRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void getAllCategory(final CategoryCallback callback) {
        Library.LibraryRequest library = new Library.LibraryRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN));
        Call<Library.LibraryRespond> request = apiUtils.initializePlamberAPI().getAllCategory(library);
        request.enqueue(new Callback<Library.LibraryRespond>() {
            @Override
            public void onResponse(Call<Library.LibraryRespond> call, Response<Library.LibraryRespond> response) {
                callback.onSuccess(response.body().getLibraryData());
            }

            @Override
            public void onFailure(Call<Library.LibraryRespond> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public void getBookDetail(final BookDetailCallback callback, long bookId) {
        if (callback != null) {
            Book.BookDetailRequest book = new Book.BookDetailRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), bookId);
            Call<Book.BookDetailRespond> request = apiUtils.initializePlamberAPI().getBookDetail(book);
            request.enqueue(new Callback<Book.BookDetailRespond>() {
                @Override
                public void onResponse(Call<Book.BookDetailRespond> call, Response<Book.BookDetailRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(Call<Book.BookDetailRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void getLastPageFromCloud(final PageCallback callback, long id) {
        if (callback != null) {
            final Page.GetPageRequest page = new Page.GetPageRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), id);
            Call<Page.GetPageRespond> request = apiUtils.initializePlamberAPI().getPage(page);
            request.enqueue(new Callback<Page.GetPageRespond>() {
                @Override
                public void onResponse(Call<Page.GetPageRespond> call, @NonNull Response<Page.GetPageRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<Page.GetPageRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void setLastPage(final StatusCallback callback, long id, int currentPage) {
        if (callback != null) {
            final Page.SetPageRequest page = new Page.SetPageRequest(appKey, preferenceUtils.readPreference(PreferenceUtils.TOKEN), id, currentPage);
            Call<Page.SetPageRespond> request = apiUtils.initializePlamberAPI().setPage(page);
            request.enqueue(new Callback<Page.SetPageRespond>() {
                @Override
                public void onResponse(Call<Page.SetPageRespond> call, Response<Page.SetPageRespond> response) {
                    if (response.isSuccessful())
                        callback.onSuccess(response.code());
                }

                @Override
                public void onFailure(Call<Page.SetPageRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    public void sendSupportMessage(final StatusCallback callback, String email, String message) {
        if (callback != null) {
            final Support.SupportRequest support = new Support.SupportRequest(appKey, email, message);
            Call<Support.SupportRespond> request = apiUtils.initializePlamberAPI().sendSupport(support);
            request.enqueue(new Callback<Support.SupportRespond>() {
                @Override
                public void onResponse(Call<Support.SupportRespond> call, Response<Support.SupportRespond> response) {
                    if (response.isSuccessful())
                    callback.onSuccess(response.code());
                }

                @Override
                public void onFailure(Call<Support.SupportRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

}
