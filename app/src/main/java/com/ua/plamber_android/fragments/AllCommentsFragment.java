package com.ua.plamber_android.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ua.plamber_android.R;
import com.ua.plamber_android.adapters.RecyclerCommentAdapter;
import com.ua.plamber_android.model.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllCommentsFragment extends DialogFragment {

    public static final String TAG = "AllCommentsFragment";
    public static final String BOOK_COMMENTS = "BOOK_COMMENTS";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_comments)
    RecyclerView recyclerComments;
    RecyclerCommentAdapter commentAdapter;
    List<Comment.CommentData> commentData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreenDialogThem);
        commentData = new Gson().fromJson(getArguments().getString(BOOK_COMMENTS), new TypeToken<List<Comment.CommentData>>() {}.getType());
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragament_all_comments, container, false);
        ButterKnife.bind(this, view);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        getToolbar().setTitle(R.string.all_comments_title);
        getToolbar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);
        getToolbar().setDisplayHomeAsUpEnabled(true);
        getToolbar().setElevation(10);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initCommentList();
        return view;

    }

    private void initCommentList() {
        recyclerComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentAdapter = new RecyclerCommentAdapter(commentData);
        recyclerComments.setAdapter(commentAdapter);
    }

    private ActionBar getToolbar() {
        if (getActivity() != null && ((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            return ((AppCompatActivity) getActivity()).getSupportActionBar();
        } else {
            return null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.setGroupVisible(R.id.manage_book_group, false);
    }
}
