package com.kotwicka.heroes.app;

import com.kotwicka.heroes.HeroesComponent;
import com.kotwicka.heroes.HeroesDetailComponent;
import com.kotwicka.heroes.HeroesDetailModule;
import com.kotwicka.heroes.HeroesModule;
import com.kotwicka.heroes.net.module.MarvelServiceModule;
import com.kotwicka.heroes.persistence.PersistenceModule;
import com.kotwicka.heroes.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, MarvelServiceModule.class, PersistenceModule.class})
public interface ApplicationComponent {

    HeroesComponent plusHeroesComponent(HeroesModule heroesModule);
    HeroesDetailComponent plusHeroesDetailComponent(HeroesDetailModule heroesDetailModule);
}
