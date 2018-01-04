package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.PageUtilsDB;
import com.ua.plamber_android.interfaces.callbacks.PageCallback;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.model.Page;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookReaderActivity extends AppCompatActivity {

    @BindView(R.id.pdf_view)
    PDFView mPdfView;

    private static final String TAG = "BookReaderActivity";
    private long bookId;

    private PreferenceUtils preferenceUtils;
    private WorkAPI workAPI;
    private PageUtilsDB pageUtilsDB;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        ButterKnife.bind(this);
        preferenceUtils = new PreferenceUtils(this);
        workAPI = new WorkAPI(this);

        Intent intent = getIntent();
        String pathFile = intent.getStringExtra(DetailBookActivity.PDF_PATH);
        file = new File(pathFile);
        bookId = intent.getLongExtra(DetailBookActivity.BOOK_ID, 0);
        pageUtilsDB = new PageUtilsDB(this, bookId);

        if (!preferenceUtils.readStatusOffline()) {
            viewFromCloud();
        } else {
            viewFromDB();
        }
    }

    private void viewFromCloud() {
        workAPI.getLastPageFromCloud(new PageCallback() {
            @Override
            public void onSuccess(@NonNull Page.PageData page) {
                                if (!pageUtilsDB.isBookCreate()) {
                    pageUtilsDB.createPageData(page.getLastPage(), page.getLastReadData());
                    viewPdf(page.getLastPage() - 1);
                } else {
                    if (Utils.convertStringToDate(page.getLastReadData()).getTime() > Utils.convertStringToDate(pageUtilsDB.readPageDate().getLastRead()).getTime()) {
                            pageUtilsDB.updateLastReadDate(page.getLastReadData());
                            pageUtilsDB.updatePage(page.getLastPage());
                            viewFromDB();
                    } else {
                        viewFromDB();
                    }

                }
            }

            @Override
            public void onError(@NonNull Throwable t) {

            }
        }, bookId);
    }

    private void viewFromDB() {
        if (!pageUtilsDB.isBookCreate())
            pageUtilsDB.createPageData(0, Utils.getCurrentTime());
        viewPdf(pageUtilsDB.readPageDate().getBookPage() - 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentPage();
    }

    private void saveCurrentPage() {
        pageUtilsDB.updatePage(getCurrentPage());
        pageUtilsDB.updateLastReadDate(Utils.getCurrentTime());
        if (!preferenceUtils.readStatusOffline())
        workAPI.setLastPage(new StatusCallback() {
            @Override
            public void onSuccess(@NonNull int status) {

            }

            @Override
            public void onError(@NonNull Throwable t) {

            }
        },  bookId, getCurrentPage());
    }

    private int getCurrentPage() {
        return mPdfView.getCurrentPage() + 1;
    }

    private void viewPdf(final int currentPage) {
        mPdfView.fromFile(file)
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        mPdfView.zoomTo((mPdfView.getWidth() - 1) / mPdfView.getOptimalPageWidth());
                        mPdfView.jumpTo(currentPage);
                    }
                }).spacing(10).enableAntialiasing(true).load();
    }

    public static Intent startReaderActivity(Context context) {
        return new Intent(context, BookReaderActivity.class);
    }

}
