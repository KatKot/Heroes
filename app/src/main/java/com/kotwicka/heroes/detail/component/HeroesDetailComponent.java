package com.kotwicka.heroes.detail.component;

import com.kotwicka.heroes.detail.module.HeroesDetailModule;
import com.kotwicka.heroes.detail.view.HeroDetailActivity;

import dagger.Subcomponent;

@Subcomponent(modules = HeroesDetailModule.class)
public interface HeroesDetailComponent {

    void inject(HeroDetailActivity heroDetailActivity);
}
