package com.kotwicka.heroes.net.api;

import com.kotwicka.heroes.net.model.Heroes;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MarvelService {

    @GET("/v1/public/characters")
    Observable<Heroes> getHeroes(@Query("limit") int limit, @Query("offset") int offset);

    @GET("/v1/public/characters")
    Observable<Heroes> getHeroesWithNameStartingWith(@Query("nameStartsWith") String name, @Query("limit") int limit, @Query("offset") int offset );

}
