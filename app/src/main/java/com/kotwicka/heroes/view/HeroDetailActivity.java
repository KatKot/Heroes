package com.kotwicka.heroes.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kotwicka.heroes.R;
import com.kotwicka.heroes.app.HeroesApp;
import com.kotwicka.heroes.contract.HeroDetailContract;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.utils.HeroPictureUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeroDetailActivity extends AppCompatActivity implements HeroDetailContract.View {

    private static final String TAG = HeroDetailActivity.class.getSimpleName();

    @BindView(R.id.hero_detail_image)
    ImageView photo;

    @BindView(R.id.hero_detail_description)
    TextView description;

    @BindView(R.id.fab_detail)
    FloatingActionButton fab;

    @Inject
    HeroDetailContract.Presenter heroDetailPresenter;

    private HeroViewModel hero;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        HeroesApp.get().plusHeroesDetailComponent(this).inject(this);

        this.hero = getIntent().getParcelableExtra(getString(R.string.hero_extra));
        setHeroData();
        setFabView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HeroesApp.get().clearHeroesDetailComponent();
    }

    private void setFabView() {
        Log.d(TAG, "Setting initial fab view...");
        heroDetailPresenter.checkFavouriteStatus(hero);
    }

    private void setHeroData() {
        if (hero != null) {
            getSupportActionBar().setTitle(hero.getName());
            HeroPictureUtil.loadPicture(this, photo, hero.getPhotoPath());
            description.setText(hero.getDescription());
        }
    }

    @Override
    public void displayAddedMessage() {
        Snackbar.make(description, "Added to favourites!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayDeletedMessage() {
        Snackbar.make(description, "Deleted from favourites!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void displayErrorMessage() {
        Snackbar.make(description, "Error occurred!", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setFavouriteButton() {
        Log.d(TAG, "Setting icon as full...");
        fab.setImageResource(R.drawable.ic_favourite);
        Log.d(TAG, "Icon full set!");
    }

    @Override
    public void setNotFavouriteButton() {
        Log.d(TAG, "Setting icon as only border....");
        fab.setImageResource(R.drawable.ic_favorite_border);
        Log.d(TAG, "Icon only border set!");
    }

    public void changeHeroFavouriteStatus(View view) {
        heroDetailPresenter.changeFavouriteStatus(hero);
    }
}
