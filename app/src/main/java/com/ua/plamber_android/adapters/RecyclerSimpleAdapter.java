package com.ua.plamber_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerSimpleAdapter extends RecyclerView.Adapter<RecyclerSimpleAdapter.ViewHolder> {

    private List<String> itemsList;
    private RecyclerViewClickListener mListener;

    public RecyclerSimpleAdapter(List<String> categories, RecyclerViewClickListener listener) {
        this.itemsList = categories;
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
        return new ViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = itemsList.get(position);
        holder.libraryName.setText(item);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public List<String> getItemsList() {
        return itemsList;
    }
}
