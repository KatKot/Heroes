package com.kotwicka.heroes.net.api;

import com.kotwicka.heroes.net.model.Heroes;


import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface MarvelService {

    @GET("/v1/public/characters")
    Observable<Heroes> getHeroes(@Query("limit") int limit, @Query("offset") int offset);

}
