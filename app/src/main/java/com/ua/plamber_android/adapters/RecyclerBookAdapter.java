package com.ua.plamber_android.adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.DetailBookActivity;
import com.ua.plamber_android.api.interfaces.PlamberAPI;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.RecyclerUserBooksUpdate;


import java.util.List;

public class RecyclerBookAdapter extends RecyclerView.Adapter<RecyclerBookAdapter.ViewHolder>{

    private List<Book.BookData> books;
    public static final String BOOKKEY = "BOOKKEY";

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView img;
        public TextView nameBook;
        public TextView authorBook;
        public ProgressBar userProgressImage;
        public View view;

        public ViewHolder(View v) {
            super(v);
            this.view = v;
            this.img = (ImageView) v.findViewById(R.id.book_item_image);
            this.nameBook = (TextView) v.findViewById(R.id.book_item_name);
            this.authorBook= (TextView) v.findViewById(R.id.book_item_author);
            this.userProgressImage = (ProgressBar) v.findViewById(R.id.pb_user_book_download);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = DetailBookActivity.startDetailActivity(view.getContext());
            intent.putExtra(BOOKKEY, new Gson().toJson(books.get(getAdapterPosition())));
            view.getContext().startActivity(intent);
        }
    }

    public RecyclerBookAdapter(List<Book.BookData> books) {
        this.books = books;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_book, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Book.BookData book = books.get(position);
        String url = PlamberAPI.ENDPOINT;
        String currentUrl = url.substring(0, url.length() - 1) + book.getPhoto();
        Glide.with(holder.view).load(currentUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.userProgressImage.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.img);
        holder.nameBook.setText(book.getBookName());
        holder.authorBook.setText(book.getIdAuthor());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateList(List<Book.BookData> newLsit) {
        DiffUtil.DiffResult diffResult =
                DiffUtil.calculateDiff(new RecyclerUserBooksUpdate(this.books, newLsit));
        this.books.clear();
        this.books.addAll(newLsit);
        diffResult.dispatchUpdatesTo(this);
    }
}
