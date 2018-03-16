package com.kotwicka.heroes;

import com.kotwicka.heroes.view.HeroDetailActivity;
import com.kotwicka.heroes.view.MainActivity;

import dagger.Subcomponent;

@Subcomponent(modules = HeroesDetailModule.class)
public interface HeroesDetailComponent {

    void inject(HeroDetailActivity heroDetailActivity);
}
