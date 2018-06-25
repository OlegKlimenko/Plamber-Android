package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.ua.plamber_android.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookReaderLocalActivity extends AppCompatActivity {

    public static final String LOCAL_BOOK_NAME = "LOCAL_BOOK_NAME";
    public static final String LOCAL_BOOK_FILE = "LOCAL_BOOK_FILE";

    @BindView(R.id.pdf_view)
    PDFView mPdfView;
    @BindView(R.id.reader_drawer_layout)
    DrawerLayout mReaderDrawer;
    @BindView(R.id.reader_navigation_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private boolean isLoadPdf;
    private String mBookName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        mBookName = getIntent().getStringExtra(LOCAL_BOOK_NAME);
        initToolbar();
        viewPdf(0);
        mReaderDrawer.setEnabled(false);
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(mBookName);
            getSupportActionBar().setElevation(10);
        }
    }

    private void fitWidth() {
        mPdfView.zoomTo((mPdfView.getWidth() - 1) / mPdfView.getOptimalPageWidth());
        mPdfView.jumpTo(mPdfView.getCurrentPage());
    }

    private void viewPdf(final int currentPage) {
        mPdfView.fromFile(new File(getIntent().getStringExtra(LOCAL_BOOK_FILE)))
                .onRender(new OnRenderListener() {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight) {
                        mPdfView.zoomTo((mPdfView.getWidth() - 1) / mPdfView.getOptimalPageWidth());
                        mPdfView.jumpTo(currentPage);
                        setPages(mPdfView.getCurrentPage(), mPdfView.getPageCount());
                        isLoadPdf = true;
                    }
                }).enableAntialiasing(true).spacing(10).load();

    }

    private void setPages(int current, int all) {
        TextView currentText = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.reader_current_page);
        currentText.setText(String.valueOf(current));
        TextView allPageText = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.all_page);
        allPageText.setText(String.valueOf(all));
    }

    public static Intent startLocalReaderActivity(Context context) {
        return new Intent(context, BookReaderLocalActivity.class);
    }
}
