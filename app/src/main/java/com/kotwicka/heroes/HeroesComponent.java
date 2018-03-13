package com.kotwicka.heroes;

import com.kotwicka.heroes.view.MainActivity;

import dagger.Component;
import dagger.Subcomponent;

@Subcomponent(modules = HeroesModule.class)
public interface HeroesComponent {

    void inject(MainActivity mainActivity);
}
