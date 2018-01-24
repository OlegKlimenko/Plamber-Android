package com.ua.plamber_android.utils;

import android.support.v7.util.ListUpdateCallback;
import static android.support.v7.widget.RecyclerView.*;

public class JumpToChangeItem implements ListUpdateCallback {
    private int firstInsert = -1;
    private Adapter adapter = null;

    public void bind(Adapter adapter) {
        this.adapter = adapter;
    }

    public void onChanged(int position, int count, Object payload) {
        adapter.notifyItemRangeChanged(position, count, payload);
    }

    public void onInserted(int position, int count) {
        if (firstInsert == -1 || firstInsert > position) {
            firstInsert = position;
        }
        adapter.notifyItemRangeInserted(position, count);
    }

    public void onMoved(int fromPosition, int toPosition) {
        adapter.notifyItemMoved(fromPosition, toPosition);
    }

    public void onRemoved(int position, int count) {
        adapter.notifyItemRangeRemoved(position, count);
    }

    public int getFirstInsert() {
        return firstInsert;
    }

    public Adapter getAdapter() {
        return adapter;
    }
}
