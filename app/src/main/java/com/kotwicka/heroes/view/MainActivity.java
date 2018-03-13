package com.kotwicka.heroes.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.kotwicka.heroes.HeroesContract;
import com.kotwicka.heroes.R;
import com.kotwicka.heroes.adapter.HeroesAdapter;
import com.kotwicka.heroes.adapter.HeroesOnScrollListener;
import com.kotwicka.heroes.app.HeroesApp;
import com.kotwicka.heroes.model.HeroViewModel;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initiateData() {
        this.heroes = new ArrayList<>();
        this.heroesAdapter = new HeroesAdapter(this, heroes);
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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "Loading data...");
        showProgressBar();
        loadHeroes();
    }

    private void loadHeroes() {
        this.heroesPresenter.loadHeroData();
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.heroesPresenter.unsubscribeHeroData();
        this.heroesAdapter.clearData();
        HeroesApp.get().clearHeroesComponent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
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
        this.heroesAdapter.addLoadingItem();
    }

    private void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }
}
