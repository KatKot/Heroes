package com.kotwicka.heroes.list.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class EmptyRecyclerView extends RecyclerView {

    private View emptyView;

    private final AdapterDataObserver dataObserver = new AdapterDataObserver() {
        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            changeVisibleView();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            changeVisibleView();
        }

        @Override
        public void onChanged() {
            changeVisibleView();
        }
    };

    public EmptyRecyclerView(Context context) {
        super(context);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    void changeVisibleView() {
        if (emptyView != null && getAdapter() != null) {
            final boolean shouldShowEmptyView = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(shouldShowEmptyView ? VISIBLE : GONE);
            setVisibility(shouldShowEmptyView ? GONE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(dataObserver);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(dataObserver);
        }
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }
}
