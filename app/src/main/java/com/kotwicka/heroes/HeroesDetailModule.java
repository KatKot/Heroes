package com.kotwicka.heroes;

import com.kotwicka.heroes.contract.HeroDetailContract;
import com.kotwicka.heroes.model.HeroModel;
import com.kotwicka.heroes.presenter.HeroDetailPresenter;
import com.kotwicka.heroes.repository.HeroesRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class HeroesDetailModule {

    private final HeroDetailContract.View heroDetailView;

    public HeroesDetailModule(final HeroDetailContract.View view) {
        this.heroDetailView = view;
    }

    @Provides
    public HeroDetailContract.Model providesHeroDetailModel(final HeroesRepository heroesRepository) {
        return new HeroModel(heroesRepository);
    }

    @Provides
    public HeroDetailContract.Presenter providesHeroDetailPresenter(final HeroDetailContract.Model heroModel) {
        return new HeroDetailPresenter(heroModel, heroDetailView);
    }
}
