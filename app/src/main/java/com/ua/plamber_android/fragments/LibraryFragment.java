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
import com.ua.plamber_android.api.interfaces.callbacks.CategoryCallback;
import com.ua.plamber_android.api.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.model.CategoryBook;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.utils.TokenUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibraryFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerLibraryAdapter mLibraryAdapter;
    private ProgressBar mProgressLibrary;

    TokenUtils tokenUtils;
    APIUtils apiUtils;

    private static final String TAG = "LibraryFragment";
    public static final int MENU_REQUEST = 123;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenUtils = new TokenUtils(getActivity());
        apiUtils = new APIUtils(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragmnet_library, container, false);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.library_recycler_view);
        mProgressLibrary = (ProgressBar) v.findViewById(R.id.progress_library);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        visibleProgress(mProgressLibrary, true);
        getAllCategory(new CategoryCallback() {
            @Override
            public void onSuccess(@NonNull final List<Library.LibraryData> categories) {
                RecyclerViewClickListener listener = new RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                       //getBooksFromCategory(categories.get(position).getId(), 1);
                        Intent intent = new Intent(getActivity(), CategoryActivity.class);
                        getActivity().startActivityForResult(intent, MENU_REQUEST);
                    }
                };

                if (mLibraryAdapter == null) {
                    mLibraryAdapter = new RecyclerLibraryAdapter(categories, listener);
                }
                mRecyclerView.setAdapter(mLibraryAdapter);
                visibleProgress(mProgressLibrary, false);
                visibleProgress(mRecyclerView, true);
            }

            @Override
            public void onError(@NonNull Throwable t) {

            }
        });
        return v;
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

    private void getBooksFromCategory(long idCategory, int pageNumber) {
        CategoryBook.CategoryBookRequest category = new CategoryBook.CategoryBookRequest(tokenUtils.readToken(), pageNumber);
        final Call<CategoryBook.CategoryBookRespond> request = apiUtils.initializePlamberAPI().getCurrentCategory(idCategory, category);

        request.enqueue(new Callback<CategoryBook.CategoryBookRespond>() {
            @Override
            public void onResponse(Call<CategoryBook.CategoryBookRespond> call, Response<CategoryBook.CategoryBookRespond> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, response.body().getDetail());
                } else {
                    Log.i(TAG, "Error");
                }
            }

            @Override
            public void onFailure(Call<CategoryBook.CategoryBookRespond> call, Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
            }
        });

    }

    private void visibleProgress(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(v.GONE);
        }
    }
}
