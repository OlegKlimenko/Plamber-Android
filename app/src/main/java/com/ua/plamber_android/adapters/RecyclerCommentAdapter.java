package com.ua.plamber_android.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ua.plamber_android.BuildConfig;
import com.ua.plamber_android.R;
import com.ua.plamber_android.api.PlamberAPI;
import com.ua.plamber_android.model.Comment;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerCommentAdapter extends RecyclerView.Adapter<RecyclerCommentAdapter.CommentHolder>{

    private List<Comment.CommentData> commentData;

    public RecyclerCommentAdapter(List<Comment.CommentData> commentData) {
        this.commentData = commentData;
    }

    public class CommentHolder extends RecyclerView.ViewHolder {

        View view;
        @BindView(R.id.profile_image_comment)
        ImageView profileImage;
        @BindView(R.id.comment_author_name)
        TextView authorName;
        @BindView(R.id.comment_date)
        TextView commentDate;
        @BindView(R.id.comment_text)
        TextView commentText;

        public CommentHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            this.view = view;
        }
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_comment, parent, false);
        return new CommentHolder(v);
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Comment.CommentData comment = commentData.get(position);
        holder.authorName.setText(comment.getUserName());
        holder.commentDate.setText(comment.getPostedDate());
        holder.commentText.setText(comment.getCommentText());
        String url = BuildConfig.END_POINT;
        String currentUrl = url.substring(0, url.length() - 1) + comment.getUserPhotoUrl();
        Glide.with(holder.view).load(currentUrl).apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.profileImage);
    }

    @Override
    public int getItemCount() {
        return commentData.size();
    }

    public void updateComments(List<Comment.CommentData> newList) {
        commentData.clear();
        commentData.addAll(newList);
        notifyDataSetChanged();
    }
}
