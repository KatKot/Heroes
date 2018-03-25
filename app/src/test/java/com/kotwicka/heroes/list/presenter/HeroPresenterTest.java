package com.kotwicka.heroes.list.presenter;

import com.kotwicka.heroes.list.contract.HeroesContract;
import com.kotwicka.heroes.model.HeroApiModel;
import com.kotwicka.heroes.model.HeroViewModel;
import com.kotwicka.heroes.test.utils.ImmediateSchedulersRule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.ConnectException;

import io.reactivex.Observable;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HeroPresenterTest {

    @ClassRule
    public static ImmediateSchedulersRule immediateSchedulersRule = new ImmediateSchedulersRule();

    @Mock
    private HeroesContract.Model model;

    @Mock
    private HeroesContract.View view;

    private HeroApiModel heroApiModel;

    private HeroPresenter heroPresenter;

    @Before
    public void setUp() {
        heroApiModel = spy(new HeroApiModel());
        heroPresenter = new HeroPresenter(model, view, heroApiModel);
    }

    @Test
    public void shouldLoadFirstPageOfFavouriteHeroes() {
        // given
        final HeroViewModel hero1 = new HeroViewModel("hero1", "hero 1 desc", "hero1.jpg");
        final HeroViewModel hero2 = new HeroViewModel("hero2", "hero 2 desc", "hero2.jpg");
        final HeroViewModel hero3 = new HeroViewModel("hero3", "hero 3 desc", "hero.,jpg");
        final Observable<HeroViewModel> favouriteHeroes = Observable.fromArray(hero1, hero2, hero3);

        // when
        when(model.favouriteHeroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(favouriteHeroes);
        heroPresenter.loadFavouriteHeroes();

        // then
        verify(model).favouriteHeroes(heroApiModel.getLimit(), heroApiModel.getOffset());
        verify(view).updateData(hero1);
        verify(view).updateData(hero2);
        verify(view).updateData(hero3);
        verify(view).afterLoadingAllHeroes();
    }

    @Test
    public void shouldLoadFirstPageOfHeroes() {
        // given
        final HeroViewModel hero1 = new HeroViewModel("hero1", "hero 1 desc", "hero1.jpg");
        final HeroViewModel hero2 = new HeroViewModel("hero2", "hero 2 desc", "hero2.jpg");
        final HeroViewModel hero3 = new HeroViewModel("hero3", "hero 3 desc", "hero.,jpg");
        final Observable<HeroViewModel> heroes = Observable.fromArray(hero1, hero2, hero3);

        // when
        when(model.heroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);
        heroPresenter.loadHeroes();

        // then
        verify(model).heroes(heroApiModel.getLimit(), heroApiModel.getOffset());
        verify(view).updateData(hero1);
        verify(view).updateData(hero2);
        verify(view).updateData(hero3);
        verify(view).afterLoadingAllHeroes();
    }

    @Test
    public void shouldLoadFirstPageOfHeroesForName() {
        // given
        final String name = "hero";
        final HeroViewModel hero1 = new HeroViewModel("hero1", "hero 1 desc", "hero1.jpg");
        final HeroViewModel hero2 = new HeroViewModel("hero2", "hero 2 desc", "hero2.jpg");
        final HeroViewModel hero3 = new HeroViewModel("hero3", "hero 3 desc", "hero.,jpg");
        final Observable<HeroViewModel> heroes = Observable.fromArray(hero1, hero2, hero3);

        // when
        when(model.heroesWithName(name, heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);
        heroPresenter.loadHeroesForName(name);

        // then
        verify(model).heroesWithName(name, heroApiModel.getLimit(), heroApiModel.getOffset());
        verify(view).updateData(hero1);
        verify(view).updateData(hero2);
        verify(view).updateData(hero3);
        verify(view).afterLoadingAllHeroes();
    }

    @Test
    public void shouldAddLoadingItemWhenFirstPageLoadedIsNotLastPage() {
        // given
        final HeroViewModel hero1 = new HeroViewModel("hero1", "hero 1 desc", "hero1.jpg");
        final HeroViewModel hero2 = new HeroViewModel("hero2", "hero 2 desc", "hero2.jpg");
        final HeroViewModel hero3 = new HeroViewModel("hero3", "hero 3 desc", "hero.,jpg");
        final Observable<HeroViewModel> heroes = Observable.fromArray(hero1, hero2, hero3);
        heroApiModel.setTotal(60);

        // when
        when(model.heroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);;
        heroPresenter.loadHeroes();

        // then
        verify(view).showProgressItem();
    }

    @Test
    public void shouldNotAddLoadingItemWhenFirstPageLoadedIsLastPage() {
        // given
        final HeroViewModel hero1 = new HeroViewModel("hero1", "hero 1 desc", "hero1.jpg");
        final HeroViewModel hero2 = new HeroViewModel("hero2", "hero 2 desc", "hero2.jpg");
        final HeroViewModel hero3 = new HeroViewModel("hero3", "hero 3 desc", "hero.,jpg");
        final Observable<HeroViewModel> heroes = Observable.fromArray(hero1, hero2, hero3);
        heroApiModel.setTotal(3);

        // when
        when(model.heroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);
        heroPresenter.loadHeroes();

        // then
        verify(view, never()).showProgressItem();
    }

    @Test
    public void shouldInformViewAboutErrorOnErrorWhenFetchingHeroes() {
        // given
        final Observable<HeroViewModel> heroes = Observable.error(new ConnectException());

        // when
        when(model.heroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);
        heroPresenter.loadHeroes();

        // then
        verify(view).onErrorFetchingData();
    }

    @Test
    public void shouldLoadNextPageOfHeroesThatIsNotLastPage() {
        // given
        final HeroViewModel hero1 = new HeroViewModel("hero1", "hero 1 desc", "hero1.jpg");
        final HeroViewModel hero2 = new HeroViewModel("hero2", "hero 2 desc", "hero2.jpg");
        final HeroViewModel hero3 = new HeroViewModel("hero3", "hero 3 desc", "hero.,jpg");
        final Observable<HeroViewModel> heroes = Observable.fromArray(hero1, hero2, hero3);
        heroApiModel.setOffset(20);
        heroApiModel.setTotal(100);

        // when
        when(model.heroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);
        heroPresenter.loadNextPageOfHeroData();

        // then
        verify(view, times(3)).hideProgressItem();
        verify(view).updateData(hero1);
        verify(view).updateData(hero2);
        verify(view).updateData(hero3);
        verify(view).showProgressItem();
    }

    @Test
    public void shouldLoadNextPageOfHeroesThatIsLastPage() {
        // given
        final HeroViewModel hero1 = new HeroViewModel("hero1", "hero 1 desc", "hero1.jpg");
        final HeroViewModel hero2 = new HeroViewModel("hero2", "hero 2 desc", "hero2.jpg");
        final HeroViewModel hero3 = new HeroViewModel("hero3", "hero 3 desc", "hero.,jpg");
        final Observable<HeroViewModel> heroes = Observable.fromArray(hero1, hero2, hero3);
        heroApiModel.setOffset(20);
        heroApiModel.setTotal(23);

        // when
        when(model.heroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);
        heroPresenter.loadNextPageOfHeroData();

        // then
        verify(view, times(3)).hideProgressItem();
        verify(view).updateData(hero1);
        verify(view).updateData(hero2);
        verify(view).updateData(hero3);
        verify(view, never()).showProgressItem();
    }

    @Test
    public void shouldNotifyViewAboutErrorWhenFetchingNextPageOfHeroesAndHideProgressItem() {
        // given
        final Observable<HeroViewModel> heroes = Observable.error(new ConnectException());

        // when
        when(model.heroes(heroApiModel.getLimit(), heroApiModel.getOffset())).thenReturn(heroes);
        heroPresenter.loadNextPageOfHeroData();

        // then
        verify(view).onErrorFetchingData();
        verify(view).hideProgressItem();
    }

    @Test
    public void shouldResetApiModel() {
        // when
        heroPresenter.resetApiModel();

        // then
        verify(heroApiModel).resetParameters();
    }

    @Test
    public void shouldResetOffsetOnApiModel() {
        // when
        heroPresenter.resetApiOffset();

        // then
        verify(heroApiModel).resetOffset();
    }

    @Test
    public void shouldCheckIfIsShowingFavourites() {
        // when
        heroPresenter.isShowingFavourites();

        //then
        verify(heroApiModel).shouldGetFavourites();
    }

}
