package com.ua.plamber_android.adapters;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
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
import com.ua.plamber_android.api.interfaces.OnLoadMoreListener;
import com.ua.plamber_android.api.interfaces.PlamberAPI;
import com.ua.plamber_android.fragments.CategoryFragment;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.RecyclerUserBooksUpdate;


import java.util.ArrayList;
import java.util.List;

public class RecyclerBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Book.BookData> books;
    public static final String BOOKKEY = "BOOKKEY";
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_TYPE_ITEM = 0;
    public final int VIEW_TYPE_LOADING = 1;


    public RecyclerBookAdapter(RecyclerView recyclerView, List<Book.BookData> books) {
        this.books = books;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    public class LoadProgressHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        private LoadProgressHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar_item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return books.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView img;
        public TextView nameBook;
        public TextView authorBook;
        public ProgressBar userProgressImage;
        public View view;

        public BookHolder(View v) {
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_LOADING) {
            View v = inflater.inflate(R.layout.layout_loading_item, parent, false);
            return new LoadProgressHolder(v);
        } else {
            View v = inflater.inflate(R.layout.list_item_book, parent, false);
            return new BookHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadProgressHolder) {
            Log.i(CategoryFragment.TAG, "LoadHolder");
            LoadProgressHolder loadProgressHolder = (LoadProgressHolder) holder;
            loadProgressHolder.progressBar.setIndeterminate(true);
        } else {
            final BookHolder bookHolder = (BookHolder) holder;
            Book.BookData book = books.get(position);
            Log.i(CategoryFragment.TAG, book.getBookName());
            String url = PlamberAPI.ENDPOINT;
            String currentUrl = url.substring(0, url.length() - 1) + book.getPhoto();
            Glide.with(bookHolder.view).load(currentUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            bookHolder.userProgressImage.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(bookHolder.img);
            bookHolder.nameBook.setText(book.getBookName());
            bookHolder.authorBook.setText(book.getIdAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateList(List<Book.BookData> newLsit) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RecyclerUserBooksUpdate(this.books, newLsit));
        books.clear();
        books.addAll(newLsit);
        diffResult.dispatchUpdatesTo(this);
    }

    public void stopLoading() {
        isLoading = false;
    }
}
