package com.ua.plamber_android.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ua.plamber_android.BuildConfig;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.database.utils.BookUtilsDB;
import com.ua.plamber_android.interfaces.OnLoadMoreListener;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.model.Book;
import com.ua.plamber_android.utils.RecyclerUserBooksUpdate;
import com.ua.plamber_android.utils.JumpToChangeItem;
import com.ua.plamber_android.utils.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerBookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Book.BookData> books;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private final int VIEW_TYPE_ITEM = 0;
    public final int VIEW_TYPE_LOADING = 1;
    private RecyclerViewClickListener mListener;
    private static final String TAG = "RecyclerBookAdapter";
    private RecyclerView recyclerView;

    public RecyclerBookAdapter(RecyclerView recyclerView, List<Book.BookData> books, RecyclerViewClickListener listener) {
        this.books = books;
        this.mListener = listener;
        this.recyclerView = recyclerView;
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

    class LoadProgressHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progressBar_item)
        ProgressBar progressBar;

        private LoadProgressHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return books.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class BookHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.book_item_image)
        ImageView img;
        @BindView(R.id.book_item_name)
        TextView nameBook;
        @BindView(R.id.book_item_author)
        TextView authorBook;
        @BindView(R.id.pb_user_book_download)
        ProgressBar userProgressImage;
        @BindView(R.id.book_offline_indicator)
        ImageView offlineIndicator;
        public View view;

        private RecyclerViewClickListener mListener;

        private BookHolder(View v, RecyclerViewClickListener listener) {
            super(v);
            ButterKnife.bind(this, v);
            this.view = v;
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            mListener = listener;
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            mListener.onLongClick(v, getAdapterPosition());
            return true;
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
            return new BookHolder(v, mListener);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadProgressHolder) {
            LoadProgressHolder loadProgressHolder = (LoadProgressHolder) holder;
            loadProgressHolder.progressBar.setIndeterminate(true);
        } else {
            final BookHolder bookHolder = (BookHolder) holder;
            Book.BookData book = books.get(position);
            if (!book.isOfflineBook()) {
                String url = BuildConfig.END_POINT;
                String currentUrl = url.substring(0, url.length() - 1) + book.getPhoto();
                viewPhotoBook(currentUrl, bookHolder);
            } else {
                BookUtilsDB bookUtilsDB = new BookUtilsDB(bookHolder.view.getContext());
                Utils utils = new Utils(bookHolder.view.getContext());
                viewPhotoBook(utils.getPngFileWithPath(bookUtilsDB.getBookPrimaryKey(book.getIdServerBook())), bookHolder);
                bookHolder.offlineIndicator.setVisibility(View.VISIBLE);
                Glide.with(bookHolder.view).load(R.drawable.ic_cloud_off_black_24dp).into(bookHolder.offlineIndicator);
            }

            bookHolder.nameBook.setText(book.getBookName());
            bookHolder.authorBook.setText(book.getIdAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void updateList(List<Book.BookData> newLsit) {
        JumpToChangeItem jumpToChange = new JumpToChangeItem();
        jumpToChange.bind(this);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RecyclerUserBooksUpdate(this.books, newLsit), false);
        books.clear();
        books.addAll(newLsit);
        diffResult.dispatchUpdatesTo(jumpToChange);
        recyclerView.scrollToPosition(jumpToChange.getFirstInsert());
    }

    private void viewPhotoBook(String path, final BookHolder bookHolder) {
        Glide.with(bookHolder.view).load(path)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        bookHolder.userProgressImage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        bookHolder.userProgressImage.setVisibility(View.GONE);
                        return false;
                    }
                }).into(bookHolder.img);
    }

    public void stopLoading() {
        isLoading = false;
    }
}
