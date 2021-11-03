package com.jfsb.myvie.main.movie

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jfsb.myvie.databinding.ActivityMovieInfoBinding
import android.graphics.text.LineBreaker
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.R
import com.jfsb.myvie.api.*
import com.jfsb.myvie.main.collection.DialogCollection
import com.jfsb.myvie.main.collection.DialogCollectionList
import com.jfsb.myvie.objects.Utils
import com.jfsb.myvie.objects.Utils.getGenre
import com.leinardi.android.speeddial.SpeedDialActionItem
import com.leinardi.android.speeddial.SpeedDialView
import java.lang.Exception


class MovieInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityMovieInfoBinding

    private lateinit var genresLayoutMgr: LinearLayoutManager
    private lateinit var genresAdapter: GenreAdapter

    private lateinit var actorsLayoutMgr: LinearLayoutManager
    private lateinit var actorsAdapter: ActorAdapter

    private lateinit var similarsLayoutMgr: LinearLayoutManager
    private lateinit var similarsAdapter: MoviesAdapter
    private var similarsMoviesPage = 1

    private var movieId:Long = 0
    private lateinit var directorName:String
    private  var directorId:Long = 0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val extras = intent.extras

        if (extras != null) {
            populateDetails(extras)
        } else {
            finish()
        }

        genresLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        actorsLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        similarsLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvGenres.layoutManager = genresLayoutMgr
        binding.rvActors.layoutManager = actorsLayoutMgr
        binding.rvSimilares.layoutManager = similarsLayoutMgr

        getSimilars()

        binding.ivPlayTrailer.setOnClickListener {
            getTrailers()
        }

        binding.tvDirector.setOnClickListener {
            Utils.showPeopleDetails(directorId.toInt(), directorName, this)
        }

        createFab()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun populateDetails(extras: Bundle) {
        extras.getString(MOVIE_BACKDROP)?.let { backdropPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w1280$backdropPath")
                .transform(CenterCrop())
                .into(binding.ivBannerMovie)
        }

        extras.getString(MOVIE_POSTER)?.let { posterPath ->
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/w342$posterPath")
                .transform(CenterCrop())
                .into(binding.ivAvatar)
        }

        //rating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2

        binding.tvName.text = extras.getString(MOVIE_TITLE, "") + " / " +extras.getString(MOVIE_RELEASE_DATE, "")
        binding.tvNameBanner.text = extras.getString(MOVIE_TITLE, "")
        binding.tvYearBanner.text = extras.getString(MOVIE_RELEASE_DATE, "")

        binding.expandableText.text = extras.getString(MOVIE_OVERVIEW, "")
        val expTv = binding.expandTextView
        expTv.text = binding.expandableText.text.toString()
        binding.tvName.justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
        binding.movieRating.rating = extras.getFloat(MOVIE_RATING, 0f) / 2
        binding.movieRatingBanner.rating = extras.getFloat(MOVIE_RATING, 0f) / 2


        val genres = extras.getStringArrayList(MOVIE_GENRES)
        val genresS : ArrayList<String> = ArrayList()
        genres?.forEach {
            genresS.add(getGenre(it))
        }

        genresAdapter = GenreAdapter(genresS, genres!!, this)
        binding.rvGenres.adapter = genresAdapter

        similarsAdapter = MoviesAdapter(mutableListOf()){ movie -> Utils.showMovieDetails(movie,this)}
        binding.rvSimilares.adapter = similarsAdapter


        movieId = extras.getLong(MOVIE_ID,0)
        getCreditsMovie()
        getDuration()
    }

    private fun getCreditsMovie() {
        MoviesRepository.getCreditsMovie(
            movieId,
            ::onGetCreditsMovieFetched,
            ::onError
        )
    }

    private fun getSimilars() {
        MoviesRepository.getRecommendations(
            similarsMoviesPage,
            movieId,
            ::onSimilarMoviesFetched,
            ::onError
        )
    }

    private fun getTrailers() {
        MoviesRepository.getTrailers(
            movieId,
            ::onTrailersFetched,
            ::onError
        )
    }

    private fun getDuration(){
        MoviesRepository.getMoreInfoMovie(
            movieId,
            ::onMoreInfoMovieFetched,
            ::onError
        )
    }

    private fun onGetCreditsMovieFetched(cast: List<Cast>, crew: List<Crew>) {
        crew.forEach { crewItem ->
            //Log.d("JOB", "JOB: "+crewItem.jobCrew)
            if(crewItem.jobCrew == "Director"){
                directorId = crewItem.idCrew
                directorName = crewItem.nameCrew
                binding.tvDirector.text = directorName
            }
        }

        actorsAdapter = ActorAdapter(mutableListOf()){ actor -> Utils.showPeopleDetails(actor.idCast.toInt(), actor.nameCast, this) }
        actorsAdapter.appendActors(cast)
        binding.rvActors.adapter = actorsAdapter
        //attachActorsOnScrollListener()
    }
    private fun onSimilarMoviesFetched(movies: List<Movie>) {
        similarsAdapter.appendMovies(movies)
        attachSimilarsMoviesOnScrollListener()
    }

    private fun onTrailersFetched(trailers: List<Trailer>) {
        try{DialogTrailerPlayer(trailers[0].linkTrailer).show(supportFragmentManager, "TRAILER")}
        catch (e:Exception){
            Toast.makeText(this,"Trailer no disponible", Toast.LENGTH_SHORT).show()
        }
    }

    private fun onError(error:String = ""){
        Toast.makeText(this,error, Toast.LENGTH_SHORT).show()
    }


    private fun attachActorsOnScrollListener() {
        binding.rvActors.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = actorsLayoutMgr.itemCount
                val visibleItemCount = actorsLayoutMgr.childCount
                val firstVisibleItem = actorsLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.rvActors.removeOnScrollListener(this)
                    getCreditsMovie()
                }
            }
        })
    }

    private fun attachSimilarsMoviesOnScrollListener() {
        binding.rvSimilares.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = similarsLayoutMgr.itemCount
                val visibleItemCount = similarsLayoutMgr.childCount
                val firstVisibleItem = similarsLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    binding.rvSimilares.removeOnScrollListener(this)
                    similarsMoviesPage++
                    getSimilars()
                }
            }
        })
    }

    private fun onMoreInfoMovieFetched(duration: Long) {

        binding.tvDurationMovieBanner.text = (duration).toString() + " Minutos"
    }

    private fun createFab(){
        val primaryColor = resources.getColor(R.color.primaryColor,theme)
        val white = resources.getColor(R.color.white,theme)

        val add = SpeedDialActionItem.Builder(R.id.addCollection,R.drawable.ic_add_24)
            .setFabBackgroundColor(primaryColor)
            .setFabImageTintColor(white)

        val create = SpeedDialActionItem.Builder(R.id.createCollection,R.drawable.ic_create_24)
            .setFabBackgroundColor(primaryColor)
            .setFabImageTintColor(white)

        binding.fabMovieInfo.addActionItem(add.create())
        binding.fabMovieInfo.addActionItem(create.create())

        binding.fabMovieInfo.setOnActionSelectedListener(SpeedDialView.OnActionSelectedListener { item ->
            when (item.id) {
                R.id.addCollection -> {
                    DialogCollectionList(movieId.toString()).show(supportFragmentManager,"Agregar")
                    binding.fabMovieInfo.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
                R.id.createCollection -> {
                    DialogCollection(movieId.toString()).show(supportFragmentManager, "Coleccion")
                    binding.fabMovieInfo.close() // To close the Speed Dial with animation
                    return@OnActionSelectedListener true // false will close it without animation
                }
            }
            false
        })
    }


    companion object {
        const val MOVIE_BACKDROP = "extra_movie_backdrop"
        const val MOVIE_POSTER = "extra_movie_poster"
        const val MOVIE_TITLE = "extra_movie_title"
        const val MOVIE_RATING = "extra_movie_rating"
        const val MOVIE_RELEASE_DATE = "extra_movie_release_date"
        const val MOVIE_OVERVIEW = "extra_movie_overview"
        const val MOVIE_GENRES = "extra_movie_genres"
        const val MOVIE_ID = "extra_movie_id"
    }

}