package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;
import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.RecyclerBookAdapter;
import com.ua.plamber_android.api.interfaces.PlamberAPI;
import com.ua.plamber_android.model.Book;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailBookActivity extends AppCompatActivity {

    public static final String PDFURL = "PDFURL";

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

    Book.BookData bookData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);
        ButterKnife.bind(this);

        bookData = new Gson().fromJson(getIntent().getStringExtra(RecyclerBookAdapter.BOOKKEY),
                Book.BookData.class);

        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + bookData.getPhoto();
        Glide.with(this).load(currentUrl).into(mImageBook);

        mBookName.setText(bookData.getBookName());
        mAuthorBook.setText(bookData.getIdAuthor());
        mLanguageBook.setText(bookData.getLanguage());
        mGenreBook.setText(bookData.getIdCategory());
        mAboutBook.setText(bookData.getDescription());

        setSupportActionBar(mToolbarDeatil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(bookData.getBookName());
        getSupportActionBar().setElevation(10);
    }

    @OnClick(R.id.btn_detail_read_book)
    public void readBook() {
        Intent intent = BookReaderActivity.startBookActivity(this);
        intent.putExtra(PDFURL, bookData.getBookFile());
        startActivity(intent);
    }

    public static Intent startDetailActivity(Context context) {
        Intent intent = new Intent(context, DetailBookActivity.class);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
