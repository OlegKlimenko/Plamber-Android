package com.ua.plamber_android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.plamber_android.R;
import com.ua.plamber_android.model.Book;


import java.util.List;

public class RecyclerBookAdapter extends RecyclerView.Adapter<RecyclerBookAdapter.ViewHolder>{

    private List<Book> books;


    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView nameBook;
        public TextView authorBook;

        public ViewHolder(View v) {
            super(v);
            img = (ImageView) v.findViewById(R.id.book_item_image);
            nameBook = (TextView) v.findViewById(R.id.book_item_name);
            authorBook= (TextView) v.findViewById(R.id.book_item_author);
        }
    }

    public void add(int postion, Book book) {
        books.add(postion, book);
        notifyItemInserted(postion);
    }

    public void remove(int posstion) {
        books.remove(posstion);
        notifyItemRemoved(posstion);
    }

    public RecyclerBookAdapter(List<Book> books) {
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
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book book = books.get(position);
//        holder.img.setImageResource(book.getImage());
//        holder.nameBook.setText(book.getName());
//        holder.authorBook.setText(book.getAuthor());
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

}
