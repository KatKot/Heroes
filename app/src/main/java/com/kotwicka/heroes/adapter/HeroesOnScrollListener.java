package com.kotwicka.heroes.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kotwicka.heroes.utils.HeroApiParameters;

public abstract class HeroesOnScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;

    public HeroesOnScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    public abstract void loadMoreItems();

    public abstract boolean isLoading();

    public abstract void hideSearchBar();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if(dy > 0) {
            hideSearchBar();
        }

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= HeroApiParameters.LIMIT) {
                HeroApiParameters.incrementOffset();
                loadMoreItems();
            }
        }
    }

    private boolean isLastPage() {
        return HeroApiParameters.isLastPage();
    }
}

