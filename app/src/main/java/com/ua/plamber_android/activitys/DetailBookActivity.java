package com.ua.plamber_android.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.interfaces.callbacks.BookDetailCallback;
import com.ua.plamber_android.interfaces.callbacks.ManageBookCallback;
import com.ua.plamber_android.fragments.BaseViewBookFragment;
import com.ua.plamber_android.fragments.DownloadDialogFragmant;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.TokenUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailBookActivity extends AppCompatActivity {

    public static final String PDFPATH = "PDFPATH";
    public static final String BOOKID = "BOOKID";
    public static final String TAG = "DetailBookActivity";
    public static final String URL_ADDED_BOOK = "api/v1/add-book-home/";
    public static final String URL_REMOVE_BOOK = "api/v1/remove-book-home/";
    private static final int REQUEST_WRITE_STORAGE = 101;

    @BindView(R.id.iv_detail_book_image)
    ImageView mImageBook;
    @BindView(R.id.tv_detail_book_name)
    TextView mBookName;
    @BindView(R.id.tv_detail_author_book)
    TextView mAuthorBook;
    @BindView(R.id.tv_detail_language)
    TextView mLanguageBook;
    @BindView(R.id.tv_detail_genre_book)
    TextView mGenreBook;
    @BindView(R.id.tv_detail_about_book)
    TextView mAboutBook;
    @BindView(R.id.toolbar)
    Toolbar mToolbarDeatil;
    @BindView(R.id.btn_detail_download_book)
    Button mDetailButton;
    @BindView(R.id.detail_progress_bar)
    LinearLayout loadDetailProgress;

    private Book.BookDetailData bookDataDetail;
    private APIUtils apiUtils;
    private Utils utils;
    private TokenUtils tokenUtils;
    private WorkAPI workAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        apiUtils = new APIUtils(this);
        utils = new Utils(this);
        tokenUtils = new TokenUtils(this);
        workAPI = new WorkAPI(this);

        getBookDetail(new BookDetailCallback() {
            @Override
            public void onSuccess(@NonNull Book.BookDetailRespond bookDetail) {
                bookDataDetail = bookDetail.getData();
                viewProgress(false);
                initDetailBook();
                checkBook();
            }

            @Override
            public void onError(@NonNull Throwable t) {
                viewProgress(false);
            }
        }, getIntent().getLongExtra(BaseViewBookFragment.BOOKKEY, 0));
    }

    private void initDetailBook() {
        Book.BookData bookData = bookDataDetail.getBookData();
        setSupportActionBar(mToolbarDeatil);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(bookData.getBookName());
            getSupportActionBar().setElevation(10);
        }

        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + bookData.getPhoto();
        if (!this.isFinishing())
        Glide.with(this).load(currentUrl).into(mImageBook);

        mBookName.setText(bookData.getBookName());
        mAuthorBook.setText(bookData.getIdAuthor());
        mLanguageBook.setText(bookData.getLanguage());
        mGenreBook.setText(bookData.getIdCategory());
        mAboutBook.setText(bookData.getDescription());
        mDetailButton.setTag("Download");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bookDataDetail != null)
        checkBook();
    }


    @OnClick(R.id.btn_detail_download_book)
    public void downloadBookButton() {
        if (checkPermission()) {
            if (mDetailButton.getTag() == "Download" && apiUtils.isOnline()) {
                runDownloadDialog();
            } else if (mDetailButton.getTag() == "Added") {

                addBookToLibrary(bookDataDetail.getBookData().getIdBook());
            } else {
                Intent intent = BookReaderActivity.startReaderActivity(this);
                intent.putExtra(PDFPATH, utils.getBooksPath() + utils.getFileName(bookDataDetail.getBookData()));
                intent.putExtra(BOOKID, bookDataDetail.getBookData().getIdBook());
                startActivity(intent);
            }
        } else {
            runQuestionPermissions();
        }
    }

    private void runQuestionPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Don`t have write permission", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.item_remove:
                removeBookFromLinrary(bookDataDetail.getBookData().getIdBook());
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.manage_book, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mDetailButton.getTag() == "Added") {
            menu.findItem(R.id.item_remove).setEnabled(false);
        } else {
            menu.findItem(R.id.item_remove).setEnabled(true);
        }
        return true;
    }

    public void checkBook() {
        if (bookDataDetail.isAddedBook()) {
            File file = new File(utils.getBooksPath(), utils.getFileName(bookDataDetail.getBookData()));
            if (file.exists()) {
                mDetailButton.setText("Read Book");
                mDetailButton.setTag("Read");
            } else {
                mDetailButton.setText("Download Book");
                mDetailButton.setTag("Download");
            }
        } else {
            mDetailButton.setText("Added Book");
            mDetailButton.setTag("Added");
        }

    }

    public static Intent startDetailActivity(Context context) {
        Intent intent = new Intent(context, DetailBookActivity.class);
        return intent;
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void runDownloadDialog() {
        Bundle args = new Bundle();
        args.putString(DownloadDialogFragmant.DOWNLOADBOOK, new Gson().toJson(bookDataDetail.getBookData()));
        DownloadDialogFragmant dialogFragmant = new DownloadDialogFragmant();
        dialogFragmant.setArguments(args);
        dialogFragmant.setCancelable(false);
        dialogFragmant.show(getSupportFragmentManager(), "DownloadDialog");
    }

    private void getBookDetail(final BookDetailCallback callback, long bookId) {
        if (callback != null) {
            viewProgress(true);
            Book.BookDetailRequest book = new Book.BookDetailRequest(tokenUtils.readToken(), bookId);
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

    private void addBookToLibrary(long id) {
        workAPI.manageBookInLibrary(new ManageBookCallback() {
            @Override
            public void onSuccess(@NonNull boolean result) {
                if (result) {
                    message("Book added");
                    bookDataDetail.setAddedBook(true);
                    checkBook();
                    Intent intentResult = new Intent();
                    setResult(Activity.RESULT_OK, intentResult);
                } else {
                    message("Book added error");
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
               message(t.getLocalizedMessage());
            }
        }, id, URL_ADDED_BOOK);
    }

    private void removeBookFromLinrary(long id) {
        workAPI.manageBookInLibrary(new ManageBookCallback() {
            @Override
            public void onSuccess(@NonNull boolean result) {
                if (result) {
                    message("Book was removed");
                    bookDataDetail.setAddedBook(false);
                    checkBook();
                    Intent intentResult = new Intent();
                    setResult(Activity.RESULT_OK, intentResult);
                    File file = new File(utils.getBooksPath() + utils.getFileName(bookDataDetail.getBookData()));
                    file.delete();
                } else {
                    message("Boor remove error");
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                message(t.getLocalizedMessage());
            }
        }, id, URL_REMOVE_BOOK);
    }

    private void viewProgress(boolean status) {
        if (status) {
            loadDetailProgress.setVisibility(View.VISIBLE);
            mBookName.setVisibility(View.GONE);
            mAuthorBook.setVisibility(View.GONE);
            mLanguageBook.setVisibility(View.GONE);
            mGenreBook.setVisibility(View.GONE);
            mAboutBook.setVisibility(View.GONE);
            mDetailButton.setVisibility(View.GONE);
        } else {
            loadDetailProgress.setVisibility(View.GONE);
            mBookName.setVisibility(View.VISIBLE);
            mAuthorBook.setVisibility(View.VISIBLE);
            mLanguageBook.setVisibility(View.VISIBLE);
            mGenreBook.setVisibility(View.VISIBLE);
            mAboutBook.setVisibility(View.VISIBLE);
            mDetailButton.setVisibility(View.VISIBLE);
        }
    }

    private void message(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
