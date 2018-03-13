package com.kotwicka.heroes.app;

import android.app.Application;

import com.kotwicka.heroes.HeroesComponent;
import com.kotwicka.heroes.HeroesContract;
import com.kotwicka.heroes.HeroesModule;

public class HeroesApp extends Application {

    private static HeroesApp APP_INSTANCE;

    private ApplicationComponent applicationComponent;
    private HeroesComponent heroesComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        APP_INSTANCE = this;
        this.applicationComponent = DaggerApplicationComponent.builder()
                .build();
    }

    public static HeroesApp get() {
        return APP_INSTANCE;
    }

    public HeroesComponent plusHeroesComponent(final HeroesContract.View heroesContract){
       if(heroesComponent == null) {
            heroesComponent = applicationComponent.plusHeroesComponent(new HeroesModule(heroesContract));
       }
       return heroesComponent;
    }

    public void clearHeroesComponent() {
        this.heroesComponent = null;
    }
}
