package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.ua.plamber_android.BuildConfig;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderActivity;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.adapters.RecyclerCommentAdapter;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.fragments.dialogs.AddCommentDialog;
import com.ua.plamber_android.fragments.dialogs.AddRatedDialog;
import com.ua.plamber_android.fragments.dialogs.DownloadDialogFragmant;
import com.ua.plamber_android.interfaces.callbacks.BookDetailCallback;
import com.ua.plamber_android.interfaces.callbacks.ManageBookCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.model.Comment;
import com.ua.plamber_android.utils.PlamberAnalytics;
import com.ua.plamber_android.utils.Utils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailBookFragment extends Fragment {

    public static final String TAG = "DetailBookFragment";
    public static final String URL_ADDED_BOOK = "api/v1/add-book-home/";
    public static final String URL_REMOVE_BOOK = "api/v1/remove-book-home/";
    public static final int COMMENT_LENGTH = 3;

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
    @BindView(R.id.review_detail_layout)
    LinearLayout mReviewDetailLayout;
    @BindView(R.id.parent_main_layout)
    LinearLayout mParentLayout;
    @BindView(R.id.share_book_btn)
    ImageView mShareBook;
    @BindView(R.id.frame_gray_line_first)
    FrameLayout mFrameGreyLineFirst;
    @BindView(R.id.frame_gray_line_second)
    FrameLayout mFrameGreyLineSecond;
    @BindView(R.id.comment_detail_layout)
    LinearLayout mCommentLayout;
    @BindView(R.id.popularity_book_layout)
    LinearLayout mPopularityLayout;
    @BindView(R.id.main_layout_detail)
    LinearLayout mMainDetailLayout;
    @BindView(R.id.file_download_indicator_btn)
    ImageButton mFileIndicator;

    private Book.BookDetailData bookDataDetail;
    private BookUtilsDB bookUtilsDB;
    private APIUtils apiUtils;
    private Utils utils;
    private WorkAPI workAPI;

    Tracker mTracker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiUtils = new APIUtils(getActivity());
        utils = new Utils(getActivity());
        workAPI = new WorkAPI(getActivity());
        bookUtilsDB = new BookUtilsDB(getActivity());
        setHasOptionsMenu(true);
        initGoogleAnalytics();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_detail_fragment, container, false);
        ButterKnife.bind(this, view);
        viewDetailBook();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bookDataDetail != null)
            checkBook();
    }

    private void initGoogleAnalytics() {
        if (getActivity() != null) {
            PlamberAnalytics plamberAnalytics = (PlamberAnalytics) getActivity().getApplication();
            mTracker = plamberAnalytics.getTracker();
            mTracker.setScreenName(TAG);
        }
    }

    private void viewDetailBook() {
        viewProgress(true);
        workAPI.getBookDetail(new BookDetailCallback() {
            @Override
            public void onSuccess(@NonNull Book.BookDetailRespond bookDetail) {
                bookDataDetail = bookDetail.getData();
                bookUtilsDB = new BookUtilsDB(getActivity());
                Book.BookDetailData bookDataDetail = bookDetail.getData();
                Collections.reverse(bookDataDetail.getCommentData());
                viewProgress(false);
                initDetailBook(bookDataDetail.getBookData());
                initDetailAdditionally(bookDataDetail);
                checkBook();
                initCommentsPreview();
            }

            @Override
            public void onError(@NonNull Throwable t) {
                viewProgress(false);
            }
        }, getArguments().getLong(DetailBookActivity.BOOK_SERVER_ID));
    }

    private void setDownloadIndicator(boolean view) {
        if (view) {
            mFileIndicator.setVisibility(View.VISIBLE);
        } else {
            mFileIndicator.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.file_download_indicator_btn)
    public void messageIndicator() {
        Utils.messageSnack(mParentLayout, getString(R.string.this_book_was_saved_on_your_device));
    }

    private File getBookFile() {
        return new File(utils.getPdfFileWithPath(getCurrentBookKey()));
    }

    private void initDetailBook(Book.BookData book) {
        String url = BuildConfig.END_POINT;
        String currentUrl = url.substring(0, url.length() - 1) + book.getPhoto();
        if (getActivity() != null)
            Glide.with(getActivity()).load(currentUrl).into(mImageBook);

        mBookName.setText(book.getBookName());
        mAuthorBook.setText(book.getIdAuthor());
        mLanguageBook.setText(book.getLanguage());
        mGenreBook.setText(book.getIdCategory());
        mAboutBook.setText(book.getDescription());
        mWhoAddedBook.setText(book.getWhoAdded());
        mDateAddedBook.setText(Utils.dateUpload(book.getUploadDate()));
    }

    private void initDetailAdditionally(Book.BookDetailData book) {
        mRatingBook.setText(String.valueOf(book.getBookRating() + " "));
        mCountRatingBook.setText(String.valueOf("(" + book.getCountBookRated() + ")"));
        mNowReading.setText(String.valueOf(book.getCountUserReading()));
    }

    public void initCommentsPreview() {
        mRecyclerCommentPreview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCommentCountBook.setText(String.valueOf("(" + bookDataDetail.getCommentData().size() + ")"));
        if (bookDataDetail.getCommentData().isEmpty()) {
            mFrameViewAllComments.setVisibility(View.GONE);
        } else {
            mFrameViewAllComments.setVisibility(View.VISIBLE);
            RecyclerCommentAdapter commentAdapter;
            if (bookDataDetail.getCommentData().size() > COMMENT_LENGTH) {
                List<Comment.CommentData> previewComments = bookDataDetail.getCommentData().subList(0, 3);
                commentAdapter = new RecyclerCommentAdapter(previewComments);
            } else {
                commentAdapter = new RecyclerCommentAdapter(bookDataDetail.getCommentData());
            }
            mRecyclerCommentPreview.setAdapter(commentAdapter);
            mRecyclerCommentPreview.setLayoutFrozen(true);
        }
    }

    @OnClick(R.id.share_book_btn)
    public void shareBook() {
        if (bookDataDetail != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, BuildConfig.END_POINT + "book/" + bookDataDetail.getBookData().getIdServerBook());
            startActivity(intent);
        }
    }

    @OnClick(R.id.comments_preview_frame)
    public void viewAllCommentsRecycler() {
        openAllComments();
    }

    @OnClick(R.id.frame_add_comment)
    public void addNewComment() {
        AddCommentDialog commentFragment = new AddCommentDialog();
        commentFragment.setArguments(bundleBookId(AddCommentDialog.BOOK_ID_COMMENT));
        commentFragment.setCancelable(false);
        if (getFragmentManager() != null)
            commentFragment.show(getFragmentManager(), AddCommentDialog.TAG);
    }

    private Bundle bundleBookId(String id) {
        Bundle args = new Bundle();
        if (bookDataDetail != null)
        args.putLong(id, bookDataDetail.getBookData().getIdServerBook());
        return args;
    }

    @OnClick(R.id.frame_add_rated)
    public void addRatedBook() {
        AddRatedDialog ratedFragment = new AddRatedDialog();
        ratedFragment.setArguments(bundleBookId(AddRatedDialog.BOOK_ID_RATED));
        ratedFragment.setCancelable(false);
        if (getFragmentManager() != null)
            ratedFragment.show(getFragmentManager(), AddRatedDialog.TAG);
    }

    private void openAllComments() {
        if (bookDataDetail != null) {
            Bundle args = new Bundle();
            args.putString(AllCommentsFragment.BOOK_COMMENTS, new Gson().toJson(bookDataDetail.getCommentData()));
            AllCommentsFragment dialogComments = new AllCommentsFragment();
            dialogComments.setArguments(args);
            if (getFragmentManager() != null)
                dialogComments.show(getFragmentManager(), AllCommentsFragment.TAG);
        }
    }


    @OnClick(R.id.btn_detail_download_book)
    public void downloadBookButton() {
       actionBook();
    }

    @OnClick(R.id.iv_detail_book_image)
    public void imageBookAction() {
        actionBook();
    }

    private void actionBook() {
        if (bookDataDetail != null && mDetailButton.getTag() == "Added") {
            addBookToLibrary(bookDataDetail.getBookData().getIdServerBook());
        } else if (mDetailButton.getTag() == "Read") {
            if (getBookFile().exists())
                startReadBook();
            else
                runDownloadDialog();
        }
    }

    public void startReadBook() {
        if (bookDataDetail != null) {
            Intent intent = BookReaderActivity.startReaderActivity(getActivity());
            intent.putExtra(DetailBookActivity.BOOK_ID, bookUtilsDB.getBookPrimaryKey(bookDataDetail.getBookData().getIdServerBook()));
            intent.putExtra(DetailBookActivity.BOOK_PHOTO, bookDataDetail.getBookData().getPhoto());
            intent.putExtra(DetailBookActivity.BOOK_AUTHOR, bookDataDetail.getBookData().getIdAuthor());
            startActivity(intent);
        }
    }

    public void checkBook() {
        setDownloadIndicator(getBookFile().exists());
        if (bookDataDetail != null && bookDataDetail.isAddedBook()) {
            mDetailButton.setText(R.string.read_book_detail_btn);
            mDetailButton.setTag("Read");
        } else {
            mDetailButton.setText(R.string.added_book_detail_btn);
            mDetailButton.setTag("Added");
        }
    }

    private void runDownloadDialog() {
        if (bookDataDetail != null && apiUtils.isOnline(mParentLayout)) {
            Bundle args = new Bundle();
            args.putString(DownloadDialogFragmant.DOWNLOADBOOK, new Gson().toJson(bookDataDetail.getBookData()));
            DownloadDialogFragmant dialogFragment = new DownloadDialogFragmant();
            dialogFragment.setArguments(args);
            dialogFragment.setCancelable(false);
            if (getFragmentManager() != null)
                dialogFragment.show(getFragmentManager(), "DownloadDialog");
        }
    }

    private void addBookToLibrary(long id) {
        workAPI.manageBookInLibrary(new ManageBookCallback() {
            @Override
            public void onSuccess(@NonNull boolean result) {
                if (result) {
                    Utils.messageSnack(mParentLayout, getString(R.string.detail_book_added));
                    bookDataDetail.setAddedBook(true);
                    checkBook();
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory(getString(R.string.start_read_action))
                            .setAction(bookDataDetail.getBookData().getBookName()).build());
                } else {
                    Utils.messageSnack(mParentLayout, getString(R.string.detail_error_added));
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Utils.messageSnack(mParentLayout, getString(R.string.detail_error_added));
            }
        }, id, URL_ADDED_BOOK);
    }

    private void removeBookFromLibrary(long id) {
        workAPI.manageBookInLibrary(new ManageBookCallback() {
            @Override
            public void onSuccess(@NonNull boolean result) {
                if (result) {
                    Utils.messageSnack(mParentLayout, getString(R.string.detail_book_removed));
                    bookDataDetail.setAddedBook(false);
                    removeBookFromDevice(getCurrentBookKey(), false);
                    removeBookFromDB();
                } else {
                    Utils.messageSnack(mParentLayout, getString(R.string.detail_remove_error));
                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Utils.messageSnack(mParentLayout, getString(R.string.detail_remove_error));
            }
        }, id, URL_REMOVE_BOOK);
    }

    private void removeBookFromDevice(String bookId, boolean viewMessage) {
        File bookFile = new File(utils.getPdfFileWithPath(bookId));
        File bookCover = new File(utils.getPngFileWithPath(bookId));
        bookFile.delete();
        bookCover.delete();
        bookCover.delete();
        checkBook();
        if (viewMessage)
            Utils.messageSnack(mParentLayout, getString(R.string.book_remove_from_device));
        removeBookFromDB();
    }

    private void viewProgress(boolean status) {
        if (status) {
            mLoadDetailProgress.setVisibility(View.VISIBLE);
            mMainDetailLayout.setVisibility(View.GONE);
        } else {
            mLoadDetailProgress.setVisibility(View.GONE);
            mMainDetailLayout.setVisibility(View.VISIBLE);
        }
    }

    public void viewMessage(String message) {
        Utils.messageSnack(mParentLayout, message);
    }

    public void addNewCommentToList(Comment.CommentData comment) {
        bookDataDetail.getCommentData().add(comment);
    }

    public void writeBookToDB(String bookId) {
        bookUtilsDB.writeBookToDataBase(bookId, bookDataDetail.getBookData());
    }

    public void removeBookFromDB() {
        bookUtilsDB.removeBookFromDatabase(bookUtilsDB.getBookPrimaryKey(bookDataDetail.getBookData().getIdServerBook()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manage_book, menu);
    }

    public void updateComment(Comment.CommentData comment, String message) {
        addNewCommentToList(comment);
        viewMessage(message);
        initCommentsPreview();
    }

    private String getCurrentBookKey() {
        return bookUtilsDB.getBookPrimaryKey(bookDataDetail.getBookData().getIdServerBook());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null)
                    getActivity().onBackPressed();
                return false;
            case R.id.item_remove_library:
                removeBookFromLibrary(bookDataDetail.getBookData().getIdServerBook());
                break;
            case R.id.item_remove_device:
                removeBookFromDevice(getCurrentBookKey(), true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (mDetailButton.getTag() == "Added" || bookDataDetail == null) {
            menu.findItem(R.id.item_remove_library).setEnabled(false);
            menu.findItem(R.id.item_remove_device).setEnabled(false);
        } else if (mDetailButton.getTag() == "Read" && getBookFile().exists()) {
            menu.findItem(R.id.item_remove_device).setEnabled(true);
            menu.findItem(R.id.item_remove_library).setEnabled(true);
        } else {
            menu.findItem(R.id.item_remove_device).setEnabled(false);
            menu.findItem(R.id.item_remove_library).setEnabled(true);
        }
    }
}
