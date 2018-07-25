package com.ua.plamber_android.adapters;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;
import com.ua.plamber_android.database.model.LocalBookDB;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.utils.FileUtils;
import com.ua.plamber_android.utils.JumpToChangeItem;
import com.ua.plamber_android.utils.LocalFilesSortUpdate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerLocalBookAdapter extends RecyclerView.Adapter<RecyclerLocalBookAdapter.LocalBookHolder>{

    private List<LocalBookDB> mLocalBooks;
    private RecyclerViewClickListener mListener;
    private RecyclerView mRecyclerView;

    public RecyclerLocalBookAdapter(RecyclerView mRecyclerView, List<LocalBookDB> mLocalBooks, RecyclerViewClickListener mListener) {
        this.mLocalBooks = new ArrayList<>(mLocalBooks);
        this.mListener = mListener;
        this.mRecyclerView = mRecyclerView;
    }

    public class LocalBookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View view;
        @BindView(R.id.local_book_image)
        ImageView img;
        @BindView(R.id.local_book_name)
        TextView bookName;
        @BindView(R.id.local_book_size)
        TextView bookSize;
        @BindView(R.id.tv_local_file_size)
        TextView bookSizeType;
        private RecyclerViewClickListener mListener;

        public LocalBookHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
            this.mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public LocalBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_local_book, parent, false);
        return new LocalBookHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(LocalBookHolder holder, int position) {
        LocalBookDB book = mLocalBooks.get(position);
        holder.bookName.setText(book.getBookName());
        holder.bookSize.setText(FileUtils.getFileSize(new File(book.getBookPath())));
        Glide.with(holder.view).load(R.drawable.pdf_book).into(holder.img);
    }

    public void updateLocalBooks(List<LocalBookDB> newList) {
        JumpToChangeItem jumpToChange = new JumpToChangeItem();
        jumpToChange.bind(this);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new LocalFilesSortUpdate(mLocalBooks, newList), false);
        mLocalBooks.clear();
        mLocalBooks.addAll(newList);
        diffResult.dispatchUpdatesTo(jumpToChange);
        mRecyclerView.scrollToPosition(jumpToChange.getFirstInsert());
    }

    @Override
    public int getItemCount() {
        return mLocalBooks.size();
    }
}
