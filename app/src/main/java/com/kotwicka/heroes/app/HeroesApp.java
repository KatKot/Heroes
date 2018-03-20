package com.kotwicka.heroes.app;

import android.app.Application;

import com.kotwicka.heroes.list.component.HeroesComponent;
import com.kotwicka.heroes.list.contract.HeroesContract;
import com.kotwicka.heroes.detail.component.HeroesDetailComponent;
import com.kotwicka.heroes.detail.module.HeroesDetailModule;
import com.kotwicka.heroes.list.module.HeroesModule;
import com.kotwicka.heroes.detail.contract.HeroDetailContract;

public class HeroesApp extends Application {

    private static HeroesApp APP_INSTANCE;

    private ApplicationComponent applicationComponent;
    private HeroesComponent heroesComponent;
    private HeroesDetailComponent heroesDetailComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        APP_INSTANCE = this;
        this.applicationComponent = DaggerApplicationComponent.builder()
                .appModule(new AppModule(APP_INSTANCE))
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

    public HeroesDetailComponent plusHeroesDetailComponent(final HeroDetailContract.View view) {
        if(heroesDetailComponent == null) {
            heroesDetailComponent = applicationComponent.plusHeroesDetailComponent(new HeroesDetailModule(view));
        }
        return heroesDetailComponent;
    }

    public void clearHeroesDetailComponent() {
        this.heroesDetailComponent = null;
    }

    public void clearHeroesComponent() {
        this.heroesComponent = null;
    }
}
