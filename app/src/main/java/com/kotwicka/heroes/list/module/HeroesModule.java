package com.kotwicka.heroes.list.module;

import com.kotwicka.heroes.list.contract.HeroesContract;
import com.kotwicka.heroes.model.HeroApiModel;
import com.kotwicka.heroes.model.HeroModel;
import com.kotwicka.heroes.list.presenter.HeroPresenter;
import com.kotwicka.heroes.repository.HeroesRepository;

import dagger.Module;
import dagger.Provides;

@Module
public class HeroesModule {

    private final HeroesContract.View heroView;

    public HeroesModule(final HeroesContract.View heroView) {
        this.heroView = heroView;
    }

    @Provides
    public HeroesContract.Model providesHeroModel(final HeroesRepository heroesRepository, final HeroApiModel heroApiModel) {
        return new HeroModel(heroesRepository, heroApiModel);
    }

    @Provides
    public HeroesContract.Presenter providesHeroPresenter(final HeroesContract.Model heroModel, final HeroApiModel heroApiModel) {
        return new HeroPresenter(heroModel, heroView, heroApiModel);
    }
}
