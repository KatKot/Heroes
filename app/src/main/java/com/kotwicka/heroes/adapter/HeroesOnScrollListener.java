package com.kotwicka.heroes.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kotwicka.heroes.utils.HeroApiParameters;

public abstract class HeroesOnScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    public HeroesOnScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                HeroApiParameters.incrementOffset();
                loadMoreItems();
            }
        }
    }

    public abstract void loadMoreItems();

    public abstract boolean isLoading();

    private boolean isLastPage() {
        return HeroApiParameters.TOTAL - HeroApiParameters.OFFSET - HeroApiParameters.LIMIT <= 0;
    }
}

