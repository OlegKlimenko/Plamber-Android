package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.BookReaderActivity;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.database.utils.BookUtilsDB;
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

    Book.BookData bookData;
    BookUtilsDB bookUtilsDB;
    Utils utils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookUtilsDB = new BookUtilsDB(getActivity());
        utils = new Utils(getActivity());
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.book_detail_fragment_offline, container, false);
        ButterKnife.bind(this, view);
        viewDetailBook();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bookData != null)
            checkBook();
    }

    private void viewDetailBook() {
        bookData = bookUtilsDB.readBookFromDB(getArguments().getLong(DetailBookActivity.BOOK_ID));
        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + bookData.getPhoto();
        Glide.with(getActivity()).load(currentUrl).into(mImageBook);

        mBookName.setText(bookData.getBookName());
        mAuthorBook.setText(bookData.getIdAuthor());
        mLanguageBook.setText(bookData.getLanguage());
        mGenreBook.setText(bookData.getIdCategory());
        mAboutBook.setText(bookData.getDescription());
        mWhoAddedBook.setText(bookData.getWhoAdded());
        mDateAdded.setText(Utils.dateUpload(bookData.getUploadDate()));
    }

    @OnClick(R.id.btn_detail_download_book_offline)
    public void readBook() {
        Intent intent = BookReaderActivity.startReaderActivity(getActivity());
        intent.putExtra(DetailBookActivity.PDF_PATH, utils.getFullFileName(bookData.getBookName()));
        intent.putExtra(DetailBookActivity.BOOK_ID, bookData.getIdBook());
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.manage_book, menu);
        menu.findItem(R.id.item_remove_library).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return false;
            case R.id.item_remove_device:
                removeBookFromDevice(bookData.getBookName());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeBookFromDevice(String bookName) {
        File file = new File(utils.getFullFileName(bookName));
        file.delete();
        bookUtilsDB.removeBookFromDatabase(bookData.getIdBook());
        getActivity().finish();
    }

    private void checkBook() {
        File file = new File(utils.getFullFileName(bookData.getBookName()));
        if (!file.exists()) {
            getActivity().finish();
            Toast.makeText(getActivity(), "This book dont`t access in offline", Toast.LENGTH_SHORT).show();
        }
    }
}
