package com.ua.plamber_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ua.plamber_android.R;
import com.ua.plamber_android.model.Library;

import java.util.List;

public class RecyclerLibraryAdapter extends RecyclerView.Adapter<RecyclerLibraryAdapter.ViewHolder> {

    private List<Library.LibraryData> categories;

    public RecyclerLibraryAdapter(List<Library.LibraryData> categories) {
        this.categories = categories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public View view;
        public TextView libraryName;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.libraryName = (TextView) view.findViewById(R.id.tv_library_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), categories.get(getAdapterPosition()).getCategoryName(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.list_item_library, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Library.LibraryData libraryItem = categories.get(position);
        holder.libraryName.setText(libraryItem.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
