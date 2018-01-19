package com.ua.plamber_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.ua.plamber_android.activitys.CategoryActivity;
import com.ua.plamber_android.activitys.UploadActivity;
import com.ua.plamber_android.interfaces.RecyclerViewClickListener;

public class CategoryUploadFragment extends LibraryFragment {

    @Override
    public void actionSelectCategory() {
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra(UploadFileFragment.CATEGORY_NAME, getCategoriesList().get(position).getCategoryName());
                getActivity().setResult(Activity.RESULT_OK,returnIntent);
                getActivity().finish();
            }
        };
        setAdapter(listener);
    }
}
