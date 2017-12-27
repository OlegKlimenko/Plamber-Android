package com.ua.plamber_android.fragments;

import android.app.Activity;
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
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.activitys.LibraryActivity;
import com.ua.plamber_android.adapters.RecyclerBookAdapter;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.interfaces.callbacks.BooksCallback;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseViewBookFragment extends Fragment {

    private RecyclerBookAdapter mAdapter;
    private WorkAPI workAPI;
    private PreferenceUtils preferenceUtils;
    public static final String BOOKKEY = "BOOKKEY";
    public static final int ADDEDREQUEST = 142;
    public static boolean isUpdate = false;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.base_view_book_fragment, container, false);
        ButterKnife.bind(this, v);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (preferenceUtils.readStatusOffline())
                    viewBookOffline();
                else
                    viewUserBook();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == mAdapter.VIEW_TYPE_LOADING) {
                    return 2;
                } else {
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        if (preferenceUtils.readStatusOffline())
            viewBookOffline();
         else
            viewUserBook();
        return v;
    }

    public void viewBookOffline() {
        offlineMessage();
    }

    public void offlineMessage() {
        visible(recyclerView, false);
        visible(mUserBookProgress, false);
        visible(mMessageAgain, true);
        mSwipeRefresh.setRefreshing(false);
        mMessageAgain.setText("Now in offline mode");
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

    public void errorViewBook(Throwable t) {
        visible(recyclerView, false);
        visible(mUserBookProgress, false);
        visible(mMessageAgain, true);
        mSwipeRefresh.setRefreshing(false);
        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public void initAdapter(final List<Book.BookData> books) {
        visible(mMessageAgain, false);
        visible(mUserBookProgress, false);
        visible(recyclerView, true);
        mSwipeRefresh.setRefreshing(false);
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = DetailBookActivity.startDetailActivity(view.getContext());
                intent.putExtra(BOOKKEY, books.get(position).getIdBook());
                startActivityForResult(intent, ADDEDREQUEST);
                isUpdate = false;
            }
        };
        mAdapter = new RecyclerBookAdapter(recyclerView, books, listener);
        recyclerView.setAdapter(mAdapter);
    }

    public void visible(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADDEDREQUEST && resultCode == Activity.RESULT_OK) {
            isUpdate = true;
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
                } else if (LibraryActivity.currentPosition == 3 || LibraryActivity.currentPosition == 0){
                    fab.show();
                }
            }
        });
    }
}
