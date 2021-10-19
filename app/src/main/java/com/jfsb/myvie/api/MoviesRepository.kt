package com.jfsb.myvie.api

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MoviesRepository {

    private val api: APIService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(APIService::class.java)
    }

    fun getMoviesList(
        page: Int = 1,
        onSuccess: (movies: List<MovieResponse>) -> Unit,
        onError: () -> Unit
    ) {
        api.getPopularMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getNewMovies(
        page: Int = 1,
        onSuccess: (movies: List<MovieResponse>) -> Unit,
        onError: () -> Unit
    ) {
        api.getNewMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getTopMovies(
        page: Int = 1,
        onSuccess: (movies: List<MovieResponse>) -> Unit,
        onError: () -> Unit
    ) {
        api.getTopMovies(page = page)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getCreditsMovie(
        movieId: Long,
        onSuccess: (cast: List<Cast>, crew: List<Crew>) -> Unit,
        onError: () -> Unit
    ){
        api.getCreditsMovie(movieId)
            .enqueue(object : Callback<GetCreditsResponse>{
                override fun onResponse(
                    call: Call<GetCreditsResponse>,
                    response: Response<GetCreditsResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.casts, responseBody.crews)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }
                override fun onFailure(call: Call<GetCreditsResponse>, t: Throwable) {

                }
            })
    }

    fun getRecommendations(
        page: Int = 1,
        movieId: Long = 0,
        onSuccess: (movies: List<MovieResponse>) -> Unit,
        onError: () -> Unit
    ) {
        api.getRecommendations(page = page, movieId = movieId)
            .enqueue(object : Callback<GetMoviesResponse> {
                override fun onResponse(
                    call: Call<GetMoviesResponse>,
                    response: Response<GetMoviesResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.movies)
                        } else {
                            onError.invoke()
                        }
                    } else {
                        onError.invoke()
                    }
                }
                override fun onFailure(call: Call<GetMoviesResponse>, t: Throwable) {
                    onError.invoke()
                }
            })
    }

    fun getTrailers(
        movieId: Long = 0,
        onSuccess: (trailers: List<Trailer>) -> Unit,
        onError: (error:String) -> Unit
    ) {
        api.getTrailers(movieId = movieId)
            .enqueue(object : Callback<GetTrailersResponse> {
                override fun onResponse(
                    call: Call<GetTrailersResponse>,
                    response: Response<GetTrailersResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        if (responseBody != null) {
                            onSuccess.invoke(responseBody.trailers)
                        } else {
                            onError.invoke("Null")
                        }
                    } else {
                        onError.invoke("Error")
                    }
                }
                override fun onFailure(call: Call<GetTrailersResponse>, t: Throwable) {
                    onError.invoke("No Disponible")
                }
            })
    }

}