package com.kotwicka.heroes.app;

import com.kotwicka.heroes.HeroesComponent;
import com.kotwicka.heroes.HeroesModule;
import com.kotwicka.heroes.net.module.MarvelServiceModule;
import com.kotwicka.heroes.view.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = MarvelServiceModule.class)
public interface ApplicationComponent {

    HeroesComponent plusHeroesComponent(HeroesModule heroesModule);
}
