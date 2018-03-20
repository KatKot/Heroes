package com.kotwicka.heroes.app;

import com.kotwicka.heroes.list.component.HeroesComponent;
import com.kotwicka.heroes.detail.component.HeroesDetailComponent;
import com.kotwicka.heroes.detail.module.HeroesDetailModule;
import com.kotwicka.heroes.list.module.HeroesModule;
import com.kotwicka.heroes.net.module.MarvelServiceModule;
import com.kotwicka.heroes.persistence.PersistenceModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, MarvelServiceModule.class, PersistenceModule.class})
public interface ApplicationComponent {

    HeroesComponent plusHeroesComponent(HeroesModule heroesModule);
    HeroesDetailComponent plusHeroesDetailComponent(HeroesDetailModule heroesDetailModule);
}
