package com.ua.plamber_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;
import com.ua.plamber_android.model.Library;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerLibraryAdapter extends RecyclerView.Adapter<RecyclerLibraryAdapter.ViewHolder> {

    private List<Library.LibraryData> mCategories;
    private RecyclerViewClickListener mListener;

    public RecyclerLibraryAdapter(List<Library.LibraryData> categories, RecyclerViewClickListener listener) {
        this.mCategories = categories;
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public View view;
        @BindView(R.id.tv_library_name)
        TextView libraryName;
        private RecyclerViewClickListener mListener;

        public ViewHolder(View view, RecyclerViewClickListener listener) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_library, parent, false);
        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Library.LibraryData libraryItem = mCategories.get(position);
        holder.libraryName.setText(libraryItem.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }
}
