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
import com.ua.plamber_android.fragments.ContinueReadingDialog;
import com.ua.plamber_android.interfaces.callbacks.PageCallback;
import com.ua.plamber_android.interfaces.callbacks.StatusCallback;
import com.ua.plamber_android.utils.PreferenceUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookReaderActivity extends AppCompatActivity {

    @BindView(R.id.pdf_view)
    PDFView mPdfView;

    private static final String TAG = "BookReaderActivity";
    public static final String CLOUD_PAGE = "CLOUD_PAGE";
    public static final String LOCAL_PAGE = "LOCAL_PAGE";
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
            viewFromCloud(file);
        } else {
            viewFromDB(file);
        }
    }

    private void viewFromCloud(final File file) {
        workAPI.getLastPageFromCloud(new PageCallback() {
            @Override
            public void onSuccess(@NonNull int status, @NonNull int page) {
                if (!pageUtilsDB.isBookCreate()) {
                    pageUtilsDB.createPageData(page);
                    viewPdf(file, page - 1);
                } else {
                    if (pageUtilsDB.readPageData().isReadOffline() &&
                            pageUtilsDB.readPageData().getBookPage() != page) {
                            runContinueDialog(page);
                    } else {
                        continueRead(page);
                    }

                }
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
            }
        }, bookId);
    }

    private void runContinueDialog(int cloudPage) {
        ContinueReadingDialog dialog = new ContinueReadingDialog();
        Bundle args = new Bundle();
        args.putInt(CLOUD_PAGE, cloudPage);
        args.putInt(LOCAL_PAGE, pageUtilsDB.readPageData().getBookPage());
        dialog.setArguments(args);
        dialog.setCancelable(false);
        dialog.show(getSupportFragmentManager(), ContinueReadingDialog.TAG);
    }

    private void viewFromDB(File file) {
        if (!pageUtilsDB.isBookCreate())
            pageUtilsDB.createPageData(0);
        pageUtilsDB.updateReadStatus(true);
        viewPdf(file, pageUtilsDB.readPageData().getBookPage() - 1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentPage();
    }

    private void saveCurrentPage() {
        pageUtilsDB.updatePage(getCurrentPage());
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

    private void viewPdf(File file, final int currentPage) {
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

    public void continueRead(int page) {
        pageUtilsDB.updatePage(page);
        pageUtilsDB.updateReadStatus(false);
        viewPdf(file, page - 1);
    }
}
