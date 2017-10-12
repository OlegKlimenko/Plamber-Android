package com.ua.plamber_android.fragments;

import android.os.Bundle;
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
import com.ua.plamber_android.adapters.RecyclerLibraryAdapter;
import com.ua.plamber_android.api.APIUtils;
import com.ua.plamber_android.model.Library;
import com.ua.plamber_android.utils.TokenUtils;

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
        getAllCategory();
        return v;
    }

    private void getAllCategory() {
        Library.LibraryRequest library = new Library.LibraryRequest(tokenUtils.readToken());
        Call<Library.LibraryRespond> request = apiUtils.initializePlamberAPI().getAllCategory(library);
        request.enqueue(new Callback<Library.LibraryRespond>() {
            @Override
            public void onResponse(Call<Library.LibraryRespond> call, Response<Library.LibraryRespond> response) {
                mLibraryAdapter = new RecyclerLibraryAdapter(response.body().getLibraryData());
                mRecyclerView.setAdapter(mLibraryAdapter);
                visibleProgress(mProgressLibrary, false);
                visibleProgress(mRecyclerView, true);
            }

            @Override
            public void onFailure(Call<Library.LibraryRespond> call, Throwable t) {
                Log.i(TAG, t.getLocalizedMessage());
                visibleProgress(mProgressLibrary, false);
                visibleProgress(mRecyclerView, true);
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
