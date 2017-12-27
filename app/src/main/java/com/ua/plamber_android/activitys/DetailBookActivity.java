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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ua.plamber_android.adapters.RecyclerCommentAdapter;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.fragments.AddCommentFragment;
import com.ua.plamber_android.fragments.AddRatedFragment;
import com.ua.plamber_android.fragments.AllCommentsFragment;
import com.ua.plamber_android.fragments.BaseViewBookFragment;
import com.ua.plamber_android.fragments.DownloadDialogFragmant;
import com.ua.plamber_android.interfaces.callbacks.BookDetailCallback;
import com.ua.plamber_android.interfaces.callbacks.ManageBookCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Comment;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    @BindView(R.id.tv_detail_book_rating)
    TextView mRatingBook;
    @BindView(R.id.tv_detail_book_rating_count)
    TextView mCountRatingBook;
    @BindView(R.id.tv_detail_now_read)
    TextView mNowReading;
    @BindView(R.id.tv_detail_who_added)
    TextView mWhoAddedBook;
    @BindView(R.id.tv_detail_date_added)
    TextView mDateAddedBook;
    @BindView(R.id.toolbar)
    Toolbar mToolbarDeatil;
    @BindView(R.id.btn_detail_download_book)
    Button mDetailButton;
    @BindView(R.id.detail_progress_bar)
    LinearLayout mLoadDetailProgress;
    @BindView(R.id.recycler_comment_preview)
    RecyclerView mRecyclerCommentPreview;
    @BindView(R.id.detail_comments_count)
    TextView mCommentCountBook;
    @BindView(R.id.frame_view_all_comments)
    LinearLayout mFrameViewAllComments;
    @BindView(R.id.frame_add_comment)
    LinearLayout mFrameAddComment;
    @BindView(R.id.frame_add_rated)
    LinearLayout mFrameAddRated;
    @BindView(R.id.detail_main_layout)
    LinearLayout mMainLayout;

    private Book.BookDetailData bookDataDetail;
    private APIUtils apiUtils;
    private Utils utils;
    private PreferenceUtils preferenceUtils;
    private WorkAPI workAPI;
    private RecyclerCommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        apiUtils = new APIUtils(this);
        utils = new Utils(this);
        preferenceUtils = new PreferenceUtils(this);
        workAPI = new WorkAPI(this);
        getBookDetail(new BookDetailCallback() {
            @Override
            public void onSuccess(@NonNull Book.BookDetailRespond bookDetail) {
                bookDataDetail = bookDetail.getData();
                Collections.reverse(bookDataDetail.getCommentData());
                viewProgress(false);
                initDetailBook();
                checkBook();
                initCommentsPreview();
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

        Glide.with(getApplicationContext()).load(currentUrl).into(mImageBook);

        mBookName.setText(bookData.getBookName());
        mAuthorBook.setText(bookData.getIdAuthor());
        mLanguageBook.setText(bookData.getLanguage());
        mGenreBook.setText(bookData.getIdCategory());
        mAboutBook.setText(bookData.getDescription());
        mRatingBook.setText(String.valueOf(bookDataDetail.getBookRating() + " "));
        mCountRatingBook.setText(String.valueOf("(" + bookDataDetail.getCountBookRated() + ")"));
        mNowReading.setText(String.valueOf(bookDataDetail.getCountUserReading()));
        mWhoAddedBook.setText(bookData.getWhoAdded());
        mDateAddedBook.setText(dateUpload(bookData.getUploadDate()));
        mDetailButton.setTag("Download");
    }

    public void initCommentsPreview() {
        mRecyclerCommentPreview.setLayoutManager(new LinearLayoutManager(this));
        mCommentCountBook.setText(String.valueOf("(" + bookDataDetail.getCommentData().size() + ")"));
        if (bookDataDetail.getCommentData().size() == 0) {
            mFrameViewAllComments.setVisibility(View.GONE);
        } else {
            mFrameViewAllComments.setVisibility(View.VISIBLE);
            if (bookDataDetail.getCommentData().size() > 3) {
                List<Comment.CommentData> previewComments = new ArrayList<>();
                previewComments.add(bookDataDetail.getCommentData().get(0));
                previewComments.add(bookDataDetail.getCommentData().get(1));
                previewComments.add(bookDataDetail.getCommentData().get(2));
                commentAdapter = new RecyclerCommentAdapter(previewComments);
            } else {
                commentAdapter = new RecyclerCommentAdapter(bookDataDetail.getCommentData());
            }
            mRecyclerCommentPreview.setAdapter(commentAdapter);
            mRecyclerCommentPreview.setLayoutFrozen(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bookDataDetail != null)
            checkBook();
    }

    @OnClick(R.id.share_book_btn)
    public void shareBook() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, PlamberAPI.ENDPOINT + "book/" + bookDataDetail.getBookData().getIdBook());
        startActivity(intent);
    }

    @OnClick(R.id.comments_preview_frame)
    public void viewAllCommentsRecycler() {
        openAllcoments();
    }

    @OnClick(R.id.frame_add_comment)
    public void addNewComment() {
        AddCommentFragment commentFragment = new AddCommentFragment();
        commentFragment.setArguments(bundleBookId(AddCommentFragment.BOOK_ID_COMMENT));
        commentFragment.setCancelable(false);
        commentFragment.show(getSupportFragmentManager(), AddCommentFragment.TAG);
    }

    private Bundle bundleBookId(String id) {
        Bundle args = new Bundle();
        args.putLong(id, bookDataDetail.getBookData().getIdBook());
        return args;
    }

    @OnClick(R.id.frame_add_rated)
    public void addRatedBook() {
        AddRatedFragment ratedFragment = new AddRatedFragment();
        ratedFragment.setArguments(bundleBookId(AddRatedFragment.BOOK_ID_RATED));
        ratedFragment.setCancelable(false);
        ratedFragment.show(getSupportFragmentManager(), AddRatedFragment.TAG);
    }

    private void openAllcoments() {
        Bundle args = new Bundle();
        args.putString(AllCommentsFragment.BOOK_COMMENTS, new Gson().toJson(bookDataDetail.getCommentData()));
        AllCommentsFragment dialogComments = new AllCommentsFragment();
        dialogComments.setArguments(args);
        dialogComments.show(getSupportFragmentManager(), AllCommentsFragment.TAG);
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
                intent.putExtra(PDFPATH, utils.getFullFileName(bookDataDetail.getBookData().getBookName()));
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
                return false;
            case R.id.item_remove_library:
                removeBookFromLibrary(bookDataDetail.getBookData().getIdBook());
                break;
            case R.id.item_remove_device:
                removeBookFromDevice(bookDataDetail.getBookData().getBookName());
                break;
        }
        return super.onOptionsItemSelected(item);
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
            menu.findItem(R.id.item_remove_library).setEnabled(false);
            menu.findItem(R.id.item_remove_device).setEnabled(false);
        } else if (mDetailButton.getTag() == "Read") {
            menu.findItem(R.id.item_remove_device).setEnabled(true);
            menu.findItem(R.id.item_remove_library).setEnabled(true);
        } else {
            menu.findItem(R.id.item_remove_device).setEnabled(false);
            menu.findItem(R.id.item_remove_library).setEnabled(true);
        }
        return true;
    }

    public void checkBook() {
        if (bookDataDetail.isAddedBook()) {
            File file = new File(utils.getFullFileName(bookDataDetail.getBookData().getBookName()));
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
            Book.BookDetailRequest book = new Book.BookDetailRequest(preferenceUtils.readPreference(PreferenceUtils.TOKEN), bookId);
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

    private void removeBookFromLibrary(long id) {
        workAPI.manageBookInLibrary(new ManageBookCallback() {
            @Override
            public void onSuccess(@NonNull boolean result) {
                if (result) {
                    message("Book was removed");
                    bookDataDetail.setAddedBook(false);
                    Intent intentResult = new Intent();
                    setResult(Activity.RESULT_OK, intentResult);
                    removeBookFromDevice(bookDataDetail.getBookData().getBookName());
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

    private void removeBookFromDevice(String bookName) {
        File file = new File(utils.getFullFileName(bookName));
        file.delete();
        checkBook();
    }

    private void viewProgress(boolean status) {
        if (status) {
            mLoadDetailProgress.setVisibility(View.VISIBLE);
            mBookName.setVisibility(View.GONE);
            mAuthorBook.setVisibility(View.GONE);
            mLanguageBook.setVisibility(View.GONE);
            mGenreBook.setVisibility(View.GONE);
            mAboutBook.setVisibility(View.GONE);
            mDetailButton.setVisibility(View.GONE);
        } else {
            mLoadDetailProgress.setVisibility(View.GONE);
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

    private String dateUpload(String date) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DateFormat plamberFormat = new SimpleDateFormat("dd.MM.yy", Locale.US);
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return plamberFormat.format(newDate);
    }

    public void viewMessage(String message) {
        Utils.messageSnack(mMainLayout, message);
    }

    public void addNewCommentToList(Comment.CommentData comment) {
        bookDataDetail.getCommentData().add(comment);
    }

}
