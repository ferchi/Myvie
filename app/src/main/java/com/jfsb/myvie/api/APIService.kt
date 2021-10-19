package com.jfsb.myvie.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {
    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") apiKey: String = "d51156038b94f2f43784a91dd97ef46d",
        @Query("page") page: Int,
        @Query("language") language: String = "es-MX"
    ): Call<GetMoviesResponse>

    @GET("movie/upcoming")
    fun getNewMovies(
        @Query("api_key") apiKey: String = "d51156038b94f2f43784a91dd97ef46d",
        @Query("page") page: Int,
        @Query("language") language: String = "es-MX"
    ):Call<GetMoviesResponse>

    @GET("movie/top_rated")
    fun getTopMovies(
        @Query("api_key") apiKey: String = "d51156038b94f2f43784a91dd97ef46d",
        @Query("page") page: Int,
        @Query("language") language: String = "es-MX"
    ):Call<GetMoviesResponse>

    @GET("movie/{id}/credits")
    fun getCreditsMovie(
        @Path("id") movieId: Long,
        @Query("api_key") apiKey: String = "d51156038b94f2f43784a91dd97ef46d",
        @Query("language") language: String = "es-MX"
    ):Call<GetCreditsResponse>

    @GET ("movie/{id}/recommendations")
    fun getRecommendations(
        @Path("id") movieId: Long,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String = "d51156038b94f2f43784a91dd97ef46d",
        @Query("language") language: String = "es-MX"
    ):Call<GetMoviesResponse>

    @GET ("movie/{id}/videos")
    fun getTrailers(
        @Path("id") movieId: Long,
        @Query("api_key") apiKey: String = "d51156038b94f2f43784a91dd97ef46d",
        @Query("language") language: String = "es-MX"
    ):Call<GetTrailersResponse>
}