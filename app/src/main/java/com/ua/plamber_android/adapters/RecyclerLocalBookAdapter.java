package com.ua.plamber_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.utils.FileUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerLocalBookAdapter extends RecyclerView.Adapter<RecyclerLocalBookAdapter.LocalBookHolder>{

    private List<File> mFiles;
    private RecyclerViewClickListener mListener;


    public RecyclerLocalBookAdapter(List<File> mFiles, RecyclerViewClickListener mListener) {
        this.mFiles = mFiles;
        this.mListener = mListener;
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
        File file = mFiles.get(position);
        holder.bookName.setText(file.getName());
        holder.bookSize.setText(FileUtils.getFileSize(file));
        Glide.with(holder.view).load(R.drawable.pdf_book).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }
}
