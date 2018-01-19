package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.activitys.LibraryActivity;
import com.ua.plamber_android.adapters.RecyclerBookAdapter;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.PreferenceUtils;
import com.ua.plamber_android.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseViewBookFragment extends Fragment {

    private RecyclerBookAdapter mAdapter;
    private WorkAPI workAPI;
    private PreferenceUtils preferenceUtils;
    private Utils utils;
    private BookUtilsDB bookUtilsDB;
    public static final int ADDED_REQUEST = 142;
    public static boolean isShowError;
    private static final String TAG = "BaseViewBookFragment";
    private static final int HALF_STANDARD_WIDTH_DP = 180;

    @BindView(R.id.user_book_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.user_book_progress)
    ProgressBar mUserBookProgress;
    @BindView(R.id.tv_user_book_again)
    TextView mMessageAgain;
    @BindView(R.id.user_book_refresh_layout)
    SwipeRefreshLayout mSwipeRefresh;

    public abstract String getBookAPI();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workAPI = new WorkAPI(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
        utils = new Utils(getActivity());
        bookUtilsDB = new BookUtilsDB(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();
        if (preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE))
            viewBookOffline();
        else
            viewUserBook();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.base_view_book_fragment, container, false);
        ButterKnife.bind(this, v);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), (int) (utils.getWidthDeviceDP() / HALF_STANDARD_WIDTH_DP));
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == mAdapter.VIEW_TYPE_LOADING) {
                    return gridLayoutManager.getSpanCount();
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);
        initSwipeRefresh();
        return v;
    }

    public void initSwipeRefresh() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE)) {
                    viewBookOffline();
                } else {
                    isShowError = false;
                    viewUserBook();
                }
            }
        });
    }

    public void viewBookOffline() {
        visible(recyclerView, false);
        visible(mUserBookProgress, false);
        visible(mMessageAgain, true);
        mSwipeRefresh.setRefreshing(false);
        mMessageAgain.setText(R.string.now_in_offlane_mode);
    }

    public void viewUserBook() {
        workAPI.getUserBook(new BooksCallback() {
            @Override
            public void onSuccess(@NonNull List<Book.BookData> books) {
                initAdapter(books);
            }

            @Override
            public void onError(@NonNull Throwable t) {
                errorViewBook(t);
            }
        }, getBookAPI());
    }

    public List<Book.BookData> addOfflineBook(List<Book.BookData> books) {
        List<Book.BookData> offlineBooks = new ArrayList<>();
        for (Book.BookData book : bookUtilsDB.getListBookFromDB()) {
            if (book.isOfflineBook())
                offlineBooks.add(book);
        }
        List<Book.BookData> newList = new ArrayList<>(books);
        newList.addAll(offlineBooks);
        return newList;
    }

    public void errorViewBook(Throwable t) {
        visible(recyclerView, false);
        visible(mUserBookProgress, false);
        visible(mMessageAgain, true);
        mSwipeRefresh.setRefreshing(false);
        if (!isShowError) {
            getLibraryActivity().runErrorDialog(getString(R.string.no_connection_error));
            isShowError = true;
        }
    }

    public void initAdapter(final List<Book.BookData> books) {
        if (books.isEmpty()) {
            viewMessageListEmpty(getString(R.string.no_added_book_in_library));
        } else {
            visible(mMessageAgain, false);
            visible(mUserBookProgress, false);
            visible(recyclerView, true);
            mSwipeRefresh.setRefreshing(false);
        }

        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = DetailBookActivity.startDetailActivity(view.getContext());
                intent.putExtra(DetailBookActivity.BOOK_SERVER_ID, books.get(position).getIdServerBook());
                intent.putExtra(DetailBookActivity.BOOK_ID, books.get(position).getIdBook());
                intent.putExtra(DetailBookActivity.BOOK_NAME, books.get(position).getBookName());
                intent.putExtra(DetailBookActivity.IS_OFFLINE_BOOK, books.get(position).isOfflineBook());
                startActivity(intent);
            }
        };
        mAdapter = new RecyclerBookAdapter(recyclerView, books, listener);
        recyclerView.setAdapter(mAdapter);
    }

    public void viewMessageListEmpty(String message) {
        visible(mMessageAgain, true);
        mMessageAgain.setText(message);
        visible(mUserBookProgress, false);
        visible(recyclerView, false);
        mSwipeRefresh.setRefreshing(false);
    }

    public void visible(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public RecyclerBookAdapter getmAdapter() {
        return mAdapter;
    }

    public ProgressBar getmUserBookProgress() {
        return mUserBookProgress;
    }

    public TextView getmMessageAgain() {
        return mMessageAgain;
    }

    public SwipeRefreshLayout getmSwipeRefresh() {
        return mSwipeRefresh;
    }

    public WorkAPI getWorkAPI() {
        return workAPI;
    }

    public void hideFloatingButton() {
        final FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab_upload);
        getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && fab.isShown()) {
                    fab.hide();
                } else if (LibraryActivity.currentPosition == 3 || LibraryActivity.currentPosition == 0) {
                    fab.show();
                }
            }
        });
    }

    public LibraryActivity getLibraryActivity() {
        return ((LibraryActivity) getActivity());
    }
}
