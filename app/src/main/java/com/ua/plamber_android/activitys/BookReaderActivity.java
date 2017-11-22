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
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.api.interfaces.callbacks.PageCallback;
import com.ua.plamber_android.model.Page;
import com.ua.plamber_android.utils.TokenUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookReaderActivity extends AppCompatActivity {

    @BindView(R.id.pdf_view)
    PDFView mPdfView;

    private static final String CURRENTPAGE = "CURRENTPAGE";
    private static final String TAG = "BookReaderActivity";
    private long bookId;

    private TokenUtils tokenUtils;
    private APIUtils apiUtils;

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
        tokenUtils = new TokenUtils(this);
        apiUtils = new APIUtils(this);
        Intent intent = getIntent();

        String pathFile = intent.getStringExtra(DetailBookActivity.PDFPATH);
        bookId = intent.getLongExtra(DetailBookActivity.BOOKID, 0);

        final File file = new File(pathFile);

        if (savedInstanceState != null) {
            viewPdf(file, savedInstanceState.getInt(CURRENTPAGE));
        } else {
            getLastPage(new PageCallback() {
                @Override
                public void onSuccess(@NonNull int status, @NonNull int page) {
                    if (status == 200) {
                        viewPdf(file, page - 1);
                    } else {
                        Log.i(TAG, "Error get page");
                    }
                }

                @Override
                public void onError(@NonNull Throwable t) {

                }
            }, bookId);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveCurrentPage();
    }

    private void saveCurrentPage() {
        setLastPage(new PageCallback() {
            @Override
            public void onSuccess(@NonNull int status, @NonNull int page) {
                if (status == 200) {
                    Log.i(TAG, "Current page save");
                } else
                    Log.i(TAG, "Error " + status);
            }

            @Override
            public void onError(@NonNull Throwable t) {

            }
        }, bookId, mPdfView.getCurrentPage() + 1);
    }

    public static Intent startReaderActivity(Context context) {
        return new Intent(context, BookReaderActivity.class);
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

    private void getLastPage(final PageCallback callback, long id) {
        if (callback != null) {
            final Page.GetPageRequest page = new Page.GetPageRequest(tokenUtils.readToken(), id);
            Call<Page.GetPageRespond> request = apiUtils.initializePlamberAPI().getPage(page);
            request.enqueue(new Callback<Page.GetPageRespond>() {
                @Override
                public void onResponse(Call<Page.GetPageRespond> call, Response<Page.GetPageRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().getStatus(), response.body().getData().getLastPage());
                    }
                }

                @Override
                public void onFailure(Call<Page.GetPageRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }

    private void setLastPage(final PageCallback callback, long id, int currentPage) {
        if (callback != null) {
            final Page.SetPageRequest page = new Page.SetPageRequest(tokenUtils.readToken(), id, currentPage);
            Call<Page.SetPageRespond> request = apiUtils.initializePlamberAPI().setPage(page);
            request.enqueue(new Callback<Page.SetPageRespond>() {
                @Override
                public void onResponse(Call<Page.SetPageRespond> call, Response<Page.SetPageRespond> response) {
                    if (response.isSuccessful()) {
                        callback.onSuccess(response.body().getStatus(), 0);
                    }
                }

                @Override
                public void onFailure(Call<Page.SetPageRespond> call, Throwable t) {
                    callback.onError(t);
                }
            });
        }
    }
}
