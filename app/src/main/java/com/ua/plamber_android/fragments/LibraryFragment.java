package com.ua.plamber_android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.CategoryActivity;
import com.ua.plamber_android.adapters.RecyclerLibraryAdapter;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.interfaces.callbacks.CategoryCallback;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.utils.TokenUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryFragment extends Fragment {
    private RecyclerLibraryAdapter mLibraryAdapter;

    @BindView(R.id.library_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_library)
    ProgressBar mProgressLibrary;

    TokenUtils tokenUtils;
    APIUtils apiUtils;

    private static final String TAG = "LibraryFragment";
    public static final String IDCATEGORI = "IdCategory";
    public static final String NAMECATEGORI = "NAMECategory";
    public static final int MENU_REQUEST = 123;
    public List<Library.LibraryData> categoriesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenUtils = new TokenUtils(getActivity());
        apiUtils = new APIUtils(getActivity());
        categoriesList = new ArrayList<>();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmnet_library, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        visibleProgress(mProgressLibrary, true);
        getAllCategory(new CategoryCallback() {
            @Override
            public void onSuccess(@NonNull List<Library.LibraryData> categories) {
                categoriesList.clear();
                categoriesList.addAll(categories);
                actionSelectCategory();
            }

            @Override
            public void onError(@NonNull Throwable t) {

            }
        });
        return v;
    }

    public void actionSelectCategory() {
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                intent.putExtra(IDCATEGORI, categoriesList.get(position).getId());
                intent.putExtra(NAMECATEGORI, categoriesList.get(position).getCategoryName());
                getActivity().startActivityForResult(intent, MENU_REQUEST);
            }
        };
        setAdapter(listener);
    }

    public void setAdapter(RecyclerViewClickListener listener) {
        if (mLibraryAdapter == null) {
            mLibraryAdapter = new RecyclerLibraryAdapter(categoriesList, listener);
        }
        mRecyclerView.setAdapter(mLibraryAdapter);
        visibleProgress(mProgressLibrary, false);
        visibleProgress(mRecyclerView, true);
    }

    private void getAllCategory(final CategoryCallback callback) {
        Library.LibraryRequest library = new Library.LibraryRequest(tokenUtils.readToken());
        Call<Library.LibraryRespond> request = apiUtils.initializePlamberAPI().getAllCategory(library);
        request.enqueue(new Callback<Library.LibraryRespond>() {
            @Override
            public void onResponse(Call<Library.LibraryRespond> call, Response<Library.LibraryRespond> response) {
                callback.onSuccess(response.body().getLibraryData());
            }

            @Override
            public void onFailure(Call<Library.LibraryRespond> call, Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
                callback.onError(t);
                visibleProgress(mProgressLibrary, false);
                visibleProgress(mRecyclerView, true);
            }
        });
    }

    private void visibleProgress(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }

    public List<Library.LibraryData> getCategoriesList() {
        return categoriesList;
    }
}
