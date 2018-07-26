package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.CategoryActivity;
import com.ua.plamber_android.activitys.MainActivity;
import com.ua.plamber_android.adapters.RecyclerSimpleAdapter;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.database.utils.CategoryDBUtils;
import com.ua.plamber_android.interfaces.callbacks.CategoryCallback;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LibraryFragment extends Fragment {

    @BindView(R.id.library_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_library)
    ProgressBar mProgressLibrary;
    @BindView(R.id.library_refresh_layout)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.tv_library_book_again)
    TextView mMessageAgain;

    private RecyclerSimpleAdapter mLibraryAdapter;

    private PreferenceUtils preferenceUtils;
    private WorkAPI workAPI;
    private CategoryDBUtils categoryDBUtils;

    private static final String TAG = "LibraryFragment";
    public static final String IDCATEGORI = "IdCategory";
    public static final String NAMECATEGORI = "NameCategory";
    public static final int MENU_REQUEST = 123;
    public List<Library.LibraryData> categoriesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceUtils = new PreferenceUtils(getActivity());
        workAPI = new WorkAPI(getActivity());
        categoriesList = new ArrayList<>();
        categoryDBUtils = new CategoryDBUtils(getActivity());
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmnet_library, container, false);
        ButterKnife.bind(this, v);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSwipeRefresh.setOnRefreshListener(() -> {
            if (preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE)) {
                viewCategoryOffline();
            } else {
                BaseViewBookFragment.isShowError = false;
                viewCategoryFromServer();
            }
        });

        if (preferenceUtils.readLogic(PreferenceUtils.OFFLINE_MODE))
            viewCategoryOffline();
        else
            viewCategory();
    }


    private void viewCategoryOffline() {
        offlineMessage();
    }

    private void offlineMessage() {
        viewElement(mProgressLibrary, false);
        viewElement(mRecyclerView, false);
        viewElement(mMessageAgain, true);
        mSwipeRefresh.setRefreshing(false);
        mMessageAgain.setText(R.string.now_in_offlane_mode);
    }

    private void viewCategoryFromServer() {
        workAPI.getAllCategory(new CategoryCallback() {
            @Override
            public void onSuccess(@NonNull List<Library.LibraryData> categories) {
                categoriesList.clear();
                categoryDBUtils.removeAllCategory();
                categoryDBUtils.addCategoryToDB(categories);
                categoriesList.addAll(categories);
                actionSelectCategory();
            }

            @Override
            public void onError(@NonNull Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
                viewElement(mProgressLibrary, false);
                viewElement(mRecyclerView, false);
                viewElement(mMessageAgain, true);
                mSwipeRefresh.setRefreshing(false);
                if (!BaseViewBookFragment.isShowError) {
                    getLibraryActivity().runErrorDialog(getString(R.string.no_connection_error));
                    BaseViewBookFragment.isShowError = true;
                }
            }
        });
    }

    private void viewCategory() {
        if (categoryDBUtils.categoryIsSave()) {
            categoriesList.clear();
            categoriesList.addAll(categoryDBUtils.getCategoriesFromDB());
            actionSelectCategory();
            return;
        }

       viewCategoryFromServer();
    }

    public void actionSelectCategory() {
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra(IDCATEGORI, categoriesList.get(position).getId());
                intent.putExtra(NAMECATEGORI, categoriesList.get(position).getCategoryName());
                if (getActivity() != null)
                getActivity().startActivityForResult(intent, MENU_REQUEST);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        };
        setAdapter(listener);
    }

    public void setAdapter(RecyclerViewClickListener listener) {

        List<String> items = new ArrayList<>();
        for (Library.LibraryData libraryData : categoriesList) {
            items.add(libraryData.getCategoryName());
        }
        if (mRecyclerView.getAdapter() == null) {
            mLibraryAdapter = new RecyclerSimpleAdapter(items, listener);
            mRecyclerView.setAdapter(mLibraryAdapter);
        }
        viewElement(mProgressLibrary, false);
        viewElement(mMessageAgain, false);
        viewElement(mRecyclerView, true);
        mSwipeRefresh.setRefreshing(false);
    }

    private void viewElement(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public List<Library.LibraryData> getCategoriesList() {
        return categoriesList;
    }

    public MainActivity getLibraryActivity() {
        return ((MainActivity) getActivity());
    }
}
