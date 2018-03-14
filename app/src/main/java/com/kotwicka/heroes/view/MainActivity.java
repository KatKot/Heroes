package com.kotwicka.heroes.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kotwicka.heroes.HeroesContract;
import com.kotwicka.heroes.R;
import com.kotwicka.heroes.adapter.HeroesAdapter;
import com.kotwicka.heroes.adapter.HeroesOnScrollListener;
import com.kotwicka.heroes.app.HeroesApp;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.utils.HeroApiParameters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements HeroesContract.View {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.hero_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.heroes_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.search_et)
    EditText searchEditText;

    @Inject
    HeroesContract.Presenter heroesPresenter;

    private HeroesAdapter heroesAdapter;
    private List<HeroViewModel> heroes;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        HeroesApp.get().plusHeroesComponent(this).inject(this);
        initiateData();
        showProgressBar();
        loadHeroes();
    }

    private void initiateData() {
        this.heroes = new ArrayList<>();
        this.heroesAdapter = new HeroesAdapter(this, heroes, new HeroesAdapter.OnHeroClickListener() {
            @Override
            public void onHeroClick(HeroViewModel hero) {
                final Intent intent = new Intent(MainActivity.this, HeroDetailActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(getString(R.string.hero_extra), hero);
                startActivity(intent);
            }
        });
        this.searchEditText.setOnFocusChangeListener(new InputFocusListener());
        this.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    heroesAdapter.clearData();
                    progressBar.setVisibility(View.VISIBLE);
                    hideSoftKeyboard();
                    searchEditText.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    HeroApiParameters.NAME = v.getText().toString();
                    heroesPresenter.loadHeroData();
                    ;
                    return true;
                }
                return false;
            }
        });
        this.recyclerView.setAdapter(heroesAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        this.recyclerView.addOnScrollListener(new HeroesOnScrollListener(linearLayoutManager) {
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
                    searchEditText.setVisibility(View.GONE);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    hideSoftKeyboard();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadHeroes() {
        this.heroesPresenter.loadHeroData();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.heroesPresenter.unsubscribeHeroData();
        this.heroesAdapter.clearData();
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
            this.heroesAdapter.clearData();
            showProgressBar();
            this.heroesPresenter.loadHeroData();
            return true;
        }
        if (id == R.id.action_favourites) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData(HeroViewModel heroViewModel) {
        Log.d(TAG, "Adding new hero : " + heroViewModel.getName());
        this.heroesAdapter.add(heroViewModel);
        Log.d(TAG, "Hero size : " + this.heroesAdapter.getItemCount());
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideProgressItem() {
        this.heroesAdapter.removeLoadingItem();
        isLoading = false;
    }

    @Override
    public void showProgressItem() {
        if (!HeroApiParameters.isLastPage()) {
            this.heroesAdapter.addLoadingItem();
        }
    }

    public void showSearchBar(View view) {
        floatingActionButton.setVisibility(View.GONE);
        searchEditText.setVisibility(View.VISIBLE);
    }

    private void hideSoftKeyboard() {
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    private class InputFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (v.getId() == R.id.search_et && !hasFocus) {
                hideSoftKeyboard();
            }
        }
    }
}
