package com.kotwicka.heroes.list.adapter;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kotwicka.heroes.model.HeroApiModel;

public abstract class HeroesOnScrollListener extends RecyclerView.OnScrollListener {

    private final LinearLayoutManager layoutManager;
    private final HeroApiModel heroApiModel;

    public HeroesOnScrollListener(LinearLayoutManager layoutManager, HeroApiModel heroApiModel) {
        this.layoutManager = layoutManager;
        this.heroApiModel = heroApiModel;
    }

    public abstract void loadMoreItems();

    public abstract boolean isLoading();

    public abstract void hideSearchBar();

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy != 0) {
            hideSearchBar();
        }

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading() && !isLastPage()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0 && totalItemCount >= heroApiModel.getLimit()) {
                heroApiModel.incrementOffset();
                loadMoreItems();
            }
        }
    }

    private boolean isLastPage() {
        return heroApiModel.isLastPage();
    }
}

