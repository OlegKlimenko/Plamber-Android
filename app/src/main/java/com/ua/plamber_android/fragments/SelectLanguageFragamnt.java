package com.ua.plamber_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.UploadActivity;
import com.ua.plamber_android.adapters.RecyclerSimpleAdapter;
import com.ua.plamber_android.api.WorkAPI;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.interfaces.callbacks.StringListCallback;
import com.ua.plamber_android.utils.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectLanguageFragamnt extends Fragment {

    @BindView(R.id.language_recycler_view)
    RecyclerView mRecycler;
    @BindView(R.id.progress_language)
    ProgressBar mProgress;

    private List<String> languages;
    private RecyclerSimpleAdapter mLanguageAdapter;

    WorkAPI workAPI;
    PreferenceUtils preferenceUtils;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        workAPI = new WorkAPI(getActivity());
        preferenceUtils = new PreferenceUtils(getActivity());
        languages = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_language, container, false);
        ButterKnife.bind(this, v);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        visibleProgress(mProgress, true);
        workAPI.getAllLanguage(new StringListCallback() {
            @Override
            public void onSuccess(@NonNull List<String> stringsList) {
                languages.clear();
                languages.addAll(stringsList);
                actionSelectLanguage();
            }

            @Override
            public void onError(@NonNull Throwable t) {

            }
        });
        return v;
    }

    private void setAdapter(RecyclerViewClickListener listener) {
        if (mLanguageAdapter == null) {
            mLanguageAdapter = new RecyclerSimpleAdapter(languages, listener);
        }
        mRecycler.setAdapter(mLanguageAdapter);
        visibleProgress(mProgress, false);
        visibleProgress(mRecycler, true);
    }

    private void actionSelectLanguage() {
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(UploadActivity.BOOK_LANGUAGE, languages.get(position));
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }
        };
        setAdapter(listener);
    }

    private void visibleProgress(View v, boolean status) {
        if (status) {
            v.setVisibility(View.VISIBLE);
        } else {
            v.setVisibility(View.GONE);
        }
    }
}
