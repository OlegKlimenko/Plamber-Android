package com.ua.plamber_android.api;

import com.ua.plamber_android.model.Account;
import com.ua.plamber_android.model.AutoComplete;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Comment;
import com.ua.plamber_android.model.GetReminder;
import com.ua.plamber_android.model.Language;
import com.ua.plamber_android.model.LoadMoreBook;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.model.Page;
import com.ua.plamber_android.model.Password;
import com.ua.plamber_android.model.Rating;
import com.ua.plamber_android.model.Support;
import com.ua.plamber_android.model.UpdateReminder;
import com.ua.plamber_android.model.Upload;
import com.ua.plamber_android.model.User;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface PlamberAPI {

    @Headers("Content-Type: application/json")
    @POST("api/v1/user-login/")
    Call<User.UserRespond> login(@Body User.UserRequest userRequest);

    @Headers("Content-Type: application/json")
    @POST("{homeBooks}")
    Call<Book.BookRespond> getBooks(@Body Book.BookRequest bookRequest, @Path("homeBooks") String urlHomeBooks);

    @Headers("Content-Type: application/json")
    @POST("api/v1/is-user-exists/")
    Call<Account.LoginRespond> checkLogin(@Body Account.LoginRequest loginRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/is-mail-exists/")
    Call<Account.EmailRespond> checkEmail(@Body Account.EmailRequest emailRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/sign-in/")
    Call<User.UserRespond> registerUser(@Body Account.RegisterRequest registerRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/send-mail/")
    Call<Account.EmailRespond> restoreAccount(@Body Account.EmailRequest emailRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/categories/")
    Call<Library.LibraryRespond> getAllCategory(@Body Library.LibraryRequest libraryRequest);


    @GET
    @Streaming
    Call<ResponseBody> downloadBigFile(@Url String fileUrl);

    @Headers("Content-Type: application/json")
    @POST("api/v1/change-password/")
    Call<Password.PasswordRespond> changePassword(@Body Password.PasswordRequest passwordRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/category/")
    Call<LoadMoreBook.LoadMoreBookRespond>
    getCurrentCategory(@Body LoadMoreBook.LoadMoreRequestCategory categoryRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/set-current-page/")
    Call<Page.SetPageRespond> setPage(@Body Page.SetPageRequest setCurrentPage);

    @Headers("Content-Type: application/json")
    @POST("api/v1/read-book/")
    Call<Page.GetPageRespond> getPage(@Body Page.GetPageRequest getCurrentPage);

    @Headers("Content-Type: application/json")
    @POST("api/v1/book/")
    Call<Book.BookDetailRespond> getBookDetail(@Body Book.BookDetailRequest bookDetailRequest);

    @Headers("Content-Type: application/json")
    @POST("{manageBook}")
    Call<Book.BookDetailRespond> manageBookInLibrary(@Body Book.BookDetailRequest bookDetailRequest, @Path("manageBook") String manageBookUrl);

    @Headers("Content-Type: application/json")
    @POST("api/v1/my-profile/")
    Call<User.ProfileRespond> getProfileData(@Body User.ProfileRequest profileRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/search-book/")
    Call<LoadMoreBook.LoadMoreBookRespond> searchBook(@Body LoadMoreBook.LoadMoreRequestSearch searchBook);

    @Multipart
    @POST("api/v1/upload-book/")
    Call<Upload.UploadBookRespond> uploadFile(
            @Part("app_key") RequestBody appKey,
            @Part("user_token") RequestBody userToken,
            @Part("book_name") RequestBody bookName,
            @Part("author") RequestBody authorName,
            @Part("category") RequestBody categoryName,
            @Part("about") RequestBody aboutBook,
            @Part("language") RequestBody languageBook,
            @Part("private_book") boolean private_book,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part cover);

    @Headers("Content-Type: application/json")
    @POST("api/v1/generate-authors/")
    Call<AutoComplete.AuthorRespond> generateAuthor(@Body AutoComplete.AuthorRequest completeAuthor);

    @Headers("Content-Type: application/json")
    @POST("api/v1/generate-books/")
    Call<Book.BookRespond> generateBooks(@Body AutoComplete.BookRequest completeBook);

    @Headers("Content-Type: application/json")
    @POST("api/v1/generate-languages/")
    Call<Language.LanguageRespond> getLanguage(@Body Language.LanguageRequest languages);

    @Headers("Content-Type: application/json")
    @POST("api/v1/comment-add/")
    Call<Comment.CommentRespond> addComment(@Body Comment.CommentRequest commentRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/change-rating")
    Call<Rating.RatingRespond> addRating(@Body Rating.RatingRequest ratingRequest);

    @Headers("Content-Type: application/json")
    @POST("api/v1/send-support-message/")
    Call<Support.SupportRespond> sendSupport(@Body Support.SupportRequest supportRequest);

    @Multipart
    @POST("api/v1/upload-avatar/")
    Call<Upload.UploadAvatarRespond> uploadAvatar(
            @Part("app_key") RequestBody appKey,
            @Part("user_token") RequestBody userToken,
            @Part MultipartBody.Part file);

    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @Headers("Content-Type: application/json")
    @POST("api/v1/get-reminders/")
    Call<GetReminder.GetReminderRespond> getAllReminders(@Body GetReminder.GetReminderRequest request);

    @Headers("Content-Type: application/json")
    @POST("api/v1/update-reminder/")
    Call<ResponseBody> disableReminder(@Body UpdateReminder.UpdateReminderRequest request);
}
