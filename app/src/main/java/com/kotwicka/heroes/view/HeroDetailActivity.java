package com.kotwicka.heroes.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kotwicka.heroes.R;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.utils.HeroPictureUtil;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HeroDetailActivity extends AppCompatActivity {

    @BindView(R.id.hero_detail_image)
    ImageView photo;

    @BindView(R.id.hero_detail_description)
    TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        setHeroData();
    }

    private void setHeroData() {
        final HeroViewModel hero = (HeroViewModel) getIntent().getParcelableExtra(getString(R.string.hero_extra));
        getSupportActionBar().setTitle(hero.getName());

        HeroPictureUtil.loadPicture(this, photo, hero.getPhotoPath());
        description.setText(hero.getDescription());
    }

}
