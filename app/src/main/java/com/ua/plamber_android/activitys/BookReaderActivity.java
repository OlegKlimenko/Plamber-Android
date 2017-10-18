package com.ua.plamber_android.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.ua.plamber_android.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookReaderActivity extends AppCompatActivity {

    @BindView(R.id.pdf_view)
    PDFView mPdfView;

    private static final String CURRENTPAGE = "CURRENTPAGE";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENTPAGE, mPdfView.getCurrentPage());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_reader);
        ButterKnife.bind(this);
        String pathFile = getIntent().getStringExtra(DetailBookActivity.PDFPATH);
        File file = new File(pathFile);
        if (savedInstanceState != null) {
            viewPdf(file, savedInstanceState.getInt(CURRENTPAGE));
        } else {
            viewPdf(file, 0);
        }
    }

    public static Intent startReaderActivity(Context context) {
        Intent intent = new Intent(context, BookReaderActivity.class);
        return intent;
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

}
