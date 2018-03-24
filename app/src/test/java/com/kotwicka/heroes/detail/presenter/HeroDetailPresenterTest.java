package com.kotwicka.heroes.detail.presenter;

import com.google.common.base.Optional;
import com.kotwicka.heroes.detail.contract.HeroDetailContract;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.persistence.entity.Hero;
import com.kotwicka.heroes.test.utils.ImmediateSchedulersRule;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Single;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HeroDetailPresenterTest {

    @ClassRule
    public static ImmediateSchedulersRule immediateSchedulersRule = new ImmediateSchedulersRule();

    @Mock
    private HeroDetailContract.Model model;

    @Mock
    private HeroDetailContract.View view;

    @InjectMocks
    private HeroDetailPresenter heroDetailPresenter;

    @Test
    public void shouldSetFavouriteButtonForHeroInFavouties() {
        // given
        final Hero hero = new Hero();
        hero.setName("hero");
        hero.setDescription("first hero");
        hero.setPhotoPath("hero.jpg");
        final HeroViewModel heroViewModel = new HeroViewModel(hero);

        //when
        when(model.getFavourite(heroViewModel)).thenReturn(Single.just(Optional.of(hero)));
        heroDetailPresenter.checkFavouriteStatus(heroViewModel);

        //then
        verify(model).getFavourite(heroViewModel);
        verify(view).setFavouriteButton();
        assertThat(heroDetailPresenter.hero).isEqualTo(hero);
    }

    @Test
    public void shouldSetNotFavouriteButtonForHeroNotInFavouties() {
        // given
        final HeroViewModel heroViewModel = new HeroViewModel("hero", "first hero", "hero.jpg");

        //when
        when(model.getFavourite(heroViewModel)).thenReturn(Single.just(Optional.<Hero>absent()));
        heroDetailPresenter.checkFavouriteStatus(heroViewModel);

        //then
        verify(model).getFavourite(heroViewModel);
        verify(view).setNotFavouriteButton();
        assertThat(heroDetailPresenter.hero).isNull();
    }

    @Test
    public void shouldChangeFavouriteStatusToInFavourites() {
        // given
        heroDetailPresenter.hero = null;
        final Hero hero = new Hero();
        hero.setName("hero");
        hero.setDescription("first hero");
        hero.setPhotoPath("hero.jpg");
        final HeroViewModel heroViewModel = new HeroViewModel(hero);

        // when
        when(model.addToFavourites(heroViewModel)).thenReturn(Completable.complete());
        when(model.getFavourite(heroViewModel)).thenReturn(Single.just(Optional.of(hero)));
        heroDetailPresenter.changeFavouriteStatus(heroViewModel);

        // then
        verify(model).addToFavourites(heroViewModel);
        verify(view).displayAddedMessage();
        verify(view).setFavouriteButton();
        assertThat(heroDetailPresenter.hero).isEqualTo(hero);
    }

    @Test
    public void shouldChangeFavouriteStatusToNotInFavourites() {
        // given
        final Hero hero = new Hero();
        hero.setName("hero");
        hero.setDescription("first hero");
        hero.setPhotoPath("hero.jpg");
        final HeroViewModel heroViewModel = new HeroViewModel(hero);
        heroDetailPresenter.hero = hero;

        // when
        when(model.removeFromFavourites(hero)).thenReturn(Completable.complete());
        when(model.getFavourite(heroViewModel)).thenReturn(Single.just(Optional.<Hero>absent()));
        heroDetailPresenter.changeFavouriteStatus(heroViewModel);

        // then
        verify(model).removeFromFavourites(hero);
        verify(view).displayDeletedMessage();
        verify(view).setNotFavouriteButton();
        assertThat(heroDetailPresenter.hero).isNull();
    }

    @Test
    public void shouldDisplayErrorMessageWhenUnableToChangeFavouriteStatus() {
        // given
        final Hero hero = new Hero();
        hero.setName("hero");
        hero.setDescription("first hero");
        hero.setPhotoPath("hero.jpg");
        final HeroViewModel heroViewModel = new HeroViewModel(hero);
        heroDetailPresenter.hero = hero;

        // when
        when(model.removeFromFavourites(hero)).thenReturn(Completable.error(new NullPointerException()));
        when(model.getFavourite(heroViewModel)).thenReturn(Single.just(Optional.<Hero>absent()));
        heroDetailPresenter.changeFavouriteStatus(heroViewModel);

        // then
        verify(view).displayErrorMessage();
    }

    @Test
    public void shouldDisplayErrorMessageWhenUnableToCheckFavouriteStatus() {
        // given
        final Hero hero = new Hero();
        hero.setName("hero");
        hero.setDescription("first hero");
        hero.setPhotoPath("hero.jpg");
        final HeroViewModel heroViewModel = new HeroViewModel(hero);

        //when
        when(model.getFavourite(heroViewModel)).thenReturn(Single.<Optional<Hero>>error(new NullPointerException()));
        heroDetailPresenter.checkFavouriteStatus(heroViewModel);

        //then
        verify(view).displayErrorMessage();
    }


}
