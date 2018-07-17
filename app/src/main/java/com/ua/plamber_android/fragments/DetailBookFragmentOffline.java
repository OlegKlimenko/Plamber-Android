package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.BuildConfig;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderActivity;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.activitys.UploadActivity;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailBookFragmentOffline extends Fragment {

    @BindView(R.id.parent_main_layout_offline)
    LinearLayout mParentLayout;
    @BindView(R.id.iv_detail_book_image_offline)
    ImageView mImageBook;
    @BindView(R.id.tv_detail_book_name_offline)
    TextView mBookName;
    @BindView(R.id.tv_detail_author_book_offline)
    TextView mAuthorBook;
    @BindView(R.id.tv_detail_language_offline)
    TextView mLanguageBook;
    @BindView(R.id.tv_detail_genre_book_offline)
    TextView mGenreBook;
    @BindView(R.id.tv_detail_who_added_offline)
    TextView mWhoAddedBook;
    @BindView(R.id.tv_detail_date_added_offline)
    TextView mDateAdded;
    @BindView(R.id.tv_detail_about_book_offline)
    TextView mAboutBook;
    @BindView(R.id.liner_detail)
    LinearLayout mLinearDetail;
    @BindView(R.id.upload_offline_message)
    LinearLayout upload_offline_message;

    Book.BookData bookData;
    BookUtilsDB bookUtilsDB;
    Utils utils;
    PreferenceUtils preferenceUtils;
    private static final String TAG = "DetailBookFragment";
    public static final String BOOK_OFFLINE_KEY = "BOOK_OFFLINE_KEY";
    public static final String IS_OFFLINE_BOOK = "IS_OFFLINE_BOOK";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookUtilsDB = new BookUtilsDB(getActivity());
        utils = new Utils(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
        bookData = bookUtilsDB.readBookFromDB(getArguments().getString(DetailBookActivity.BOOK_ID));
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_detail_fragment_offline, container, false);
        ButterKnife.bind(this, view);
        viewAlways();
        if (bookData.isOfflineBook()) {
            hideDetail();
            viewPhotoBook(utils.getPngFileWithPath(bookUtilsDB.getBookPrimaryKey(bookData.getIdServerBook())));
        } else {
            viewDetailBook();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bookData != null)
            checkBook();
    }

    private void viewAlways() {
        mBookName.setText(bookData.getBookName());
    }

    private void hideDetail() {
        mLinearDetail.setVisibility(View.GONE);
        upload_offline_message.setVisibility(View.VISIBLE);
    }

    private void viewDetailBook() {
        String url = BuildConfig.END_POINT;
        String currentUrl = url.substring(0, url.length() - 1) + bookData.getPhoto();
        viewPhotoBook(currentUrl);

        mAuthorBook.setText(bookData.getIdAuthor());
        mLanguageBook.setText(bookData.getLanguage());
        mGenreBook.setText(bookData.getIdCategory());
        mAboutBook.setText(bookData.getDescription());
        mWhoAddedBook.setText(bookData.getWhoAdded());
        mDateAdded.setText(Utils.dateUpload(bookData.getUploadDate()));
    }

    private void viewPhotoBook(String path) {
        if (getActivity() != null)
        Glide.with(getActivity()).load(path).into(mImageBook);
    }

    @OnClick(R.id.btn_detail_download_book_offline)
    public void readBook() {
       startReadBook();
    }

    private void startReadBook() {
        Intent intent = BookReaderActivity.startReaderActivity(getActivity());
        intent.putExtra(DetailBookActivity.BOOK_ID, bookData.getIdBook());
        intent.putExtra(DetailBookActivity.BOOK_PHOTO, bookData.getPhoto());
        intent.putExtra(DetailBookActivity.BOOK_AUTHOR, bookData.getIdAuthor());
        startActivity(intent);
    }

    @OnClick(R.id.iv_detail_book_image_offline)
    public void bookImageAction() {
        startReadBook();
    }

    @OnClick(R.id.upload_offline_message)
    public void uploadOfflineBook() {
        openLoadManager();
    }

    private void openLoadManager() {
        if (!preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE)) {
            Intent intent = UploadActivity.startUploadActivity(getActivity());
            intent.putExtra(BOOK_OFFLINE_KEY, bookData.getIdBook());
            intent.putExtra(IS_OFFLINE_BOOK, bookData.isOfflineBook());
            if (getActivity() != null)
            getActivity().finish();
            startActivity(intent);
        } else
            Utils.messageSnack(mParentLayout, getString(R.string.now_in_offlane_mode));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manage_book, menu);
        menu.findItem(R.id.item_remove_library).setVisible(false);
        if (bookData.isOfflineBook())
            menu.findItem(R.id.item_upload_book_to_server).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null)
                getActivity().onBackPressed();
                return false;
            case R.id.item_remove_device:
                removeBookFromDevice();
                break;
            case R.id.item_upload_book_to_server:
                openLoadManager();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeBookFromDevice() {
        deleteFile(utils.getPdfFileWithPath(bookData.getIdBook()));
        deleteFile(utils.getPngFileWithPath(bookData.getIdBook()));
        bookUtilsDB.removeBookFromDatabase(bookData.getIdBook());
        if (getActivity() != null)
        getActivity().finish();
    }

    private void deleteFile(String name) {
        File file = new File(name);
        file.delete();
    }

    private void checkBook() {
        File file = new File(utils.getPdfFileWithPath(bookData.getIdBook()));
        if (!file.exists()) {
            if (getActivity() != null)
            getActivity().finish();
            Toast.makeText(getActivity(), R.string.this_book_not_access_in_offline, Toast.LENGTH_SHORT).show();
        }
    }
}
