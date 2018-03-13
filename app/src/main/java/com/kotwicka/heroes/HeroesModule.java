package com.kotwicka.heroes;

import com.kotwicka.heroes.model.HeroModel;
import com.kotwicka.heroes.net.api.MarvelService;
import com.kotwicka.heroes.presenter.HeroPresenter;
import com.kotwicka.heroes.repository.HeroesRepository;
import com.kotwicka.heroes.repository.MarvelHeroesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HeroesModule {

    private final HeroesContract.View heroView;

    public HeroesModule(final HeroesContract.View heroView) {
        this.heroView = heroView;
    }

    @Provides
    public HeroesContract.Model providesHeroModel(final HeroesRepository heroesRepository) {
        return new HeroModel(heroesRepository);
    }

    @Provides
    public HeroesContract.Presenter providesHeroPresenter(final HeroesContract.Model heroModel) {
        return new HeroPresenter(heroModel, heroView);
    }
}
