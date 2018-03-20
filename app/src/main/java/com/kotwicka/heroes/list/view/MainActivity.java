package com.kotwicka.heroes.list.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kotwicka.heroes.R;
import com.kotwicka.heroes.list.adapter.HeroesAdapter;
import com.kotwicka.heroes.list.adapter.HeroesOnScrollListener;
import com.kotwicka.heroes.app.HeroesApp;
import com.kotwicka.heroes.list.contract.HeroesContract;
import com.kotwicka.heroes.model.HeroApiModel;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.detail.view.HeroDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HeroesContract.View {

    @BindView(R.id.hero_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.heroes_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.search_et)
    EditText searchEditText;

    Toolbar toolbar;

    @Inject
    HeroesContract.Presenter heroesPresenter;

    @Inject
    HeroApiModel heroApiModel;

    private HeroesAdapter heroesAdapter;
    private List<HeroViewModel> heroes;
    private boolean isLoading = false;
    private int scrollPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        HeroesApp.get().plusHeroesComponent(this).inject(this);
        initiateData();
        loadHeroes();
    }

    private void initiateData() {
        this.heroes = new ArrayList<>();
        this.heroesAdapter = new HeroesAdapter(this, heroes, new HeroesAdapter.OnHeroClickListener() {
            @Override
            public void onHeroClick(HeroViewModel hero) {
                final Intent intent = new Intent(MainActivity.this, HeroDetailActivity.class);
                intent.putExtra(getString(R.string.hero_extra), hero);
                startActivity(intent);
            }
        });
        this.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    prepareViewForNewSearch();
                    hideSearchBarAndShowFloatingButton();
                    heroesPresenter.loadHeroData(v.getText().toString());
                    return true;
                }
                return false;
            }
        });
        this.recyclerView.setAdapter(heroesAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.addOnScrollListener(new HeroesOnScrollListener(linearLayoutManager, heroApiModel) {
            @Override
            public void loadMoreItems() {
                isLoading = true;
                heroesPresenter.loadNextPageOfHeroData();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public void hideSearchBar() {
                if (searchEditText.getVisibility() == View.VISIBLE) {
                    hideSearchBarAndShowFloatingButton();
                }
            }
        });
    }

    private void prepareViewForNewSearch() {
        heroesAdapter.clearData();
        heroesPresenter.resetApiModel();
        showProgressBar();
    }

    private void hideSearchBarAndShowFloatingButton() {
        searchEditText.setVisibility(View.GONE);
        floatingActionButton.show();
        hideSoftKeyboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (heroesPresenter.isShowingFavourites()) {
            prepareViewForNewSearch();
            this.heroesPresenter.loadFavouriteHeroes();
        }
    }

    private void loadHeroes() {
        prepareViewForNewSearch();
        this.heroesPresenter.loadHeroData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scrollPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.heroesPresenter.unsubscribeHeroData();
        this.heroesPresenter.resetApiOffset();
        HeroesApp.get().clearHeroesComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_home_view) {
            toolbar.setTitle(R.string.app_name);
            this.floatingActionButton.show();
            loadHeroes();
            return true;
        }
        if (id == R.id.action_favourites) {
            prepareViewForNewSearch();
            toolbar.setTitle(R.string.favourites_name);
            this.floatingActionButton.hide();
            this.heroesPresenter.loadFavouriteHeroes();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData(HeroViewModel heroViewModel) {
        this.heroesAdapter.add(heroViewModel);
    }

    @Override
    public void afterLoadingAllHeroes() {
        this.progressBar.setVisibility(View.GONE);
        if (heroesPresenter.isShowingFavourites() && scrollPosition > 0) {
            this.recyclerView.getLayoutManager().scrollToPosition(scrollPosition);
            this.scrollPosition = 0;
        }
    }

    @Override
    public void hideProgressItem() {
        this.heroesAdapter.removeLoadingItem();
        isLoading = false;
    }

    @Override
    public void showProgressItem() {
        this.heroesAdapter.addLoadingItem();
    }

    public void showSearchBar(View view) {
        floatingActionButton.hide();
        searchEditText.setVisibility(View.VISIBLE);
        searchEditText.requestFocus();
        showSoftKeyboard();
    }

    private void hideSoftKeyboard() {
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
            }
        }
    }

    private void showSoftKeyboard() {
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (manager != null) {
                manager.showSoftInput(focusedView, 0);
            }
        }
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }
}
