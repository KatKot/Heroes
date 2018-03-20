package com.kotwicka.heroes.list.component;

import com.kotwicka.heroes.list.module.HeroesModule;
import com.kotwicka.heroes.list.view.MainActivity;

import dagger.Subcomponent;

@Subcomponent(modules = HeroesModule.class)
public interface HeroesComponent {

    void inject(MainActivity mainActivity);
}
