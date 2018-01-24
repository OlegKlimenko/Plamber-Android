package com.ua.plamber_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ua.plamber_android.R;
import com.ua.plamber_android.activitys.FilePickActivity;
import com.ua.plamber_android.activitys.ImagePickActivity;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.utils.FileUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerFileAdapter extends RecyclerView.Adapter<RecyclerFileAdapter.FileHolder> {

    private List<File> mFiles;
    private RecyclerViewClickListener mListener;

    public RecyclerFileAdapter(List<File> mFiles, RecyclerViewClickListener mListener) {
        this.mFiles = mFiles;
        this.mListener = mListener;
        sort(mFiles);
    }

    public class FileHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        View view;
        @BindView(R.id.file_type_img)
        ImageView fileImage;
        @BindView(R.id.tv_file_name)
        TextView fileName;
        @BindView(R.id.tv_file_type)
        TextView fileType;
        @BindView(R.id.tv_file_size_type)
        TextView fileSizeType;
        private RecyclerViewClickListener mListener;

        public FileHolder(View view, RecyclerViewClickListener listener) {
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
    public FileHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_file, parent, false);
        return new FileHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(FileHolder holder, int position) {
        File file = mFiles.get(position);
        holder.fileName.setText(file.getName());
        if (file.isFile()) {
            holder.fileType.setText(FileUtils.getFileSize(file));
            holder.fileSizeType.setText(R.string.file_size);
        } else {
            holder.fileType.setText(R.string.file_folder);
            holder.fileSizeType.setText(null);
        }

        if (FileUtils.isCorrectType(file, FilePickActivity.BOOK_FORMAT)) {
            Glide.with(holder.view).load(R.drawable.pdf).into(holder.fileImage);
        } else if (FileUtils.isCorrectType(file, ImagePickActivity.IMAGE_FORMAT)) {
            Glide.with(holder.view).load(file.getPath()).into(holder.fileImage);
        } else {
            Glide.with(holder.view).load(R.drawable.folder).into(holder.fileImage);
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    public void updateList(List<File> newList) {
        mFiles.clear();
        mFiles.addAll(newList);
        sort(mFiles);
        notifyDataSetChanged();
    }

    private void sort(List<File> list) {
        Collections.sort(list, (f1, f2) -> f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase()));
    }
}
