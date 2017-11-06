package com.ua.plamber_android.api.interfaces;

import com.ua.plamber_android.model.Account;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.model.Password;
import com.ua.plamber_android.model.User;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface PlamberAPI {

    String ENDPOINT = "http://ec2-52-201-246-230.compute-1.amazonaws.com/";

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
    @POST("api/v1/is-mail-exists")
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

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);

    @Headers("Content-Type: application/json")
    @POST("api/v1/change-password/")
    Call<Password.PasswordRespond> changePassword(@Body Password.PasswordRequest passwordRequest);
}
