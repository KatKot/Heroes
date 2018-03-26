package com.kotwicka.heroes.repository;

import com.google.common.base.Optional;
import com.kotwicka.heroes.model.FavouriteHeroes;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.net.api.MarvelService;
import com.kotwicka.heroes.net.model.Data;
import com.kotwicka.heroes.net.model.Heroes;
import com.kotwicka.heroes.net.model.Result;
import com.kotwicka.heroes.persistence.dao.HeroDao;
import com.kotwicka.heroes.persistence.database.HeroDatabase;
import com.kotwicka.heroes.persistence.entity.Hero;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MarvelHeroesRepositoryTest {

    @Mock
    private MarvelService marvelService;

    @Mock
    private HeroDatabase heroDatabase;

    @Mock
    private HeroDao heroDao;

    @InjectMocks
    private MarvelHeroesRepository marvelHeroesRepository;

    @Before
    public void setUp() {
        when(heroDatabase.heroDao()).thenReturn(heroDao);
    }

    @Test
    public void shouldGetHeroes() {
        // given
        final int limit = 10;
        final int offset = 20;
        final Data data = new Data();
        data.setCount("3");
        data.setLimit(String.valueOf(limit));
        data.setOffset(String.valueOf(offset));
        data.setTotal("23");
        data.setResults(Lists.newArrayList(new Result(), new Result(), new Result()));
        final Heroes heroes = new Heroes();
        heroes.setData(data);
        final Observable<Heroes> heroesStream = Observable.just(heroes);

        // when
        when(marvelService.getHeroes(limit, offset)).thenReturn(heroesStream);
        final Observable<Data> actualData = marvelHeroesRepository.getHeroes(limit, offset);

        // then
        Mockito.verify(marvelService).getHeroes(limit, offset);
        assertThat(actualData.count().blockingGet()).isEqualTo(1);
        assertThat(actualData.blockingSingle()).isEqualTo(data);
    }

    @Test
    public void shouldGetHeroesForName() {
        // given
        final int limit = 10;
        final int offset = 20;
        final String name = "hero";
        final Data data = new Data();
        data.setCount("3");
        data.setLimit(String.valueOf(limit));
        data.setOffset(String.valueOf(offset));
        data.setTotal("23");
        data.setResults(Lists.newArrayList(new Result(), new Result(), new Result()));
        final Heroes heroes = new Heroes();
        heroes.setData(data);
        final Observable<Heroes> heroesStream = Observable.just(heroes);

        // when
        when(marvelService.getHeroesWithNameStartingWith(name, limit, offset)).thenReturn(heroesStream);
        final Observable<Data> actualData = marvelHeroesRepository.getHeroesWithName(name, limit, offset);

        // then
        Mockito.verify(marvelService).getHeroesWithNameStartingWith(name, limit, offset);
        assertThat(actualData.count().blockingGet()).isEqualTo(1);
        assertThat(actualData.blockingSingle()).isEqualTo(data);
    }

    @Test
    public void shouldGetFavouriteHeroes() {
        // given
        final int limit = 10;
        final int offset = 20;
        final Hero hero = new Hero();
        hero.setId(1);
        hero.setName("hero1");
        final Hero hero2 = new Hero();
        hero.setId(2);
        hero.setName("hero1");
        final List<Hero> heroes = Lists.newArrayList(hero, hero2);
        final int count = heroes.size();

        // when
        when(heroDao.getHeroes(limit, offset)).thenReturn(heroes);
        when(heroDao.getHeroesSize()).thenReturn(count);
        final Observable<FavouriteHeroes> actualData = marvelHeroesRepository.getFavouriteHeroes(limit, offset);

        // then
        assertThat(actualData.count().blockingGet()).isEqualTo(1);
        assertThat(actualData.blockingSingle().getFavouriteHeroesPage()).containsOnlyElementsOf(heroes);
        assertThat(actualData.blockingSingle().getTotalSize()).isEqualTo(count);
    }

    @Test
    public void shouldGetFavouriteHero() {
        // given
        final String name = "hero";
        final Hero hero = new Hero();
        hero.setName(name);
        hero.setDescription("desc");
        hero.setPhotoPath("hero.jpg");
        final HeroViewModel heroViewModel = new HeroViewModel(hero);

        // when
        when(heroDao.getForName(name)).thenReturn(hero);
        final Single<Optional<Hero>> actualHero = marvelHeroesRepository.getFavourite(heroViewModel);

        // then
        assertThat(actualHero.blockingGet().isPresent()).isTrue();
        assertThat(actualHero.blockingGet().get()).isEqualTo(hero);
    }

    @Test
    public void shouldAddHeroToFavourites() {
        // given
        final String name = "hero";
        final HeroViewModel heroViewModel = new HeroViewModel(name, "desc", "hero.jpg");

        // when
        final Completable actual = marvelHeroesRepository.addHeroToFavourites(heroViewModel);

        // then
        actual.blockingAwait();
        final ArgumentCaptor<Hero> captor = ArgumentCaptor.forClass(Hero.class);
        Mockito.verify(heroDao).insert(captor.capture());
        final Hero actualSavedHero = captor.getValue();
        assertThat(actualSavedHero.getName()).isEqualTo(name);
        assertThat(actualSavedHero.getDescription()).isEqualTo(heroViewModel.getDescription());
        assertThat(actualSavedHero.getPhotoPath()).isEqualTo(heroViewModel.getPhotoPath());
    }

    @Test
    public void shouldRemoveHeroFromFavourites() {
        // given
        final String name = "hero";
        final Hero hero = new Hero();
        hero.setName(name);
        hero.setDescription("desc");
        hero.setPhotoPath("hero.jpg");

        // when
        final Completable actual = marvelHeroesRepository.removeHeroFromFavourites(hero);

        // then
        actual.blockingAwait();
        Mockito.verify(heroDao).delete(hero);
    }
}
