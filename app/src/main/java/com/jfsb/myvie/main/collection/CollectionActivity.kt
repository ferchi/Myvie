package com.jfsb.myvie.main.collection

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.myvie.R
import com.jfsb.myvie.api.Movie
import com.jfsb.myvie.api.MovieInfoResponse
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.databinding.ActivityCollectionBinding
import com.jfsb.myvie.main.search.MovieImageListAdapter
import com.jfsb.myvie.main.search.MovieTextListListener
import com.jfsb.myvie.objects.Utils
import com.jfsb.myvie.objects.Utils.convertMovie
import com.skydoves.powermenu.*
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class CollectionActivity : AppCompatActivity(), MovieTextListListener {
    lateinit var binding: ActivityCollectionBinding
    private var collectionId : String? = ""
    private val db = FirebaseFirestore.getInstance()

    private var movies: MutableList<Movie> = mutableListOf()
    private lateinit var popUpMenu : PowerMenu.Builder

    private var databaseChild: String =  "image"

    private lateinit var rvMovies:RecyclerView
    private lateinit var moviesAdapter: MovieImageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarCollection)
        collectionId = intent.getStringExtra("id")

        popUpMenu = PowerMenu.Builder(this)
        rvMovies = binding.rvCollection

        moviesAdapter = MovieImageListAdapter(mutableListOf(),this){ movie -> Utils.showMovieDetails(movie, this) }

        binding.cardCollectionMenu.setOnClickListener {
            popUpMenu.build().clearPreference()
            createPopUp()
            popUpMenu.build().showAsAnchorLeftTop(it)
        }

    }


    override fun onStart() {
        super.onStart()
        loadData()
    }


    @DelicateCoroutinesApi
    private fun loadData() {
        lateinit var collectionName:String
        var moviesIds: List<String>

        val collection = db.collection("Collections").document(collectionId!!)
        GlobalScope.launch (Dispatchers.IO){
            val collectionObj = collection.get().await().toObject(Collection::class.java)

            collectionName = collectionObj!!.name.toString()

            moviesIds = collectionObj.movies!!

            withContext(Dispatchers.Main){
                binding.collapsingToolbarCollection.title = collectionName
                loadImg()

                movies.clear()
                moviesIds.forEach { movieId ->
                    getMovie(movieId)
                }
            }
        }
    }


    private fun loadImg() {

        db.collection("Collections").document(collectionId.toString()).addSnapshotListener{
                result, error ->
            val urlImg = result!!.get(databaseChild).toString()

            try {
                Glide.with(this)
                    .load(urlImg)
                    .transform(CenterCrop())
                    .into(binding.ivCollectionImage)

            } catch (e: Exception) {
                Picasso.get().load(R.drawable.myvie_logo).into(binding.ivCollectionImage)
            }
        }
    }

    private fun getMovie(movieId:String) {
        MoviesRepository.getMoreInfoMovie(
            movieId.toLong(),
            ::onMoreInfoMovieFetched,
            ::onError
        )
    }

    private fun onMoreInfoMovieFetched(movieInfo: MovieInfoResponse) {

        moviesAdapter.appendMovie(convertMovie(movieInfo))

        rvMovies.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter

        }
    }

    private fun onError(error:String){
        Toast.makeText(this,"No se encontró la pelicula : $error", Toast.LENGTH_SHORT).show()
    }

    private fun createPopUp(){
        popUpMenu = PowerMenu.Builder(this)
        popUpMenu
            .addItem(PowerMenuItem("Editar", false))
            .addItem(PowerMenuItem("Eliminar", false))

            .setAnimation(MenuAnimation.ELASTIC_TOP_RIGHT) // Animation start point (TOP | LEFT).
            .setMenuRadius(10f) // sets the corner radius.
            .setMenuShadow(10f) // sets the shadow.
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setTextGravity(Gravity.CENTER)
            .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
            .setSelectedTextColor(Color.RED)
            .setMenuColor(ContextCompat.getColor(this, R.color.background_gray))
            .setSelectedMenuColor(ContextCompat.getColor(this, R.color.primaryColor))
            .setOnMenuItemClickListener(onMenuItemClickListener)
            .setCircularEffect(CircularEffect.BODY)
            .build()
    }

    private val onMenuItemClickListener: OnMenuItemClickListener<PowerMenuItem?> =
        OnMenuItemClickListener<PowerMenuItem?> { position, item ->

            when(position)
            {
                0 -> {
                    val intent = Intent(this,EditCollectionActivity::class.java).apply {
                        putExtra("id",collectionId)
                    }
                    startActivity(intent)
                }
                1 -> {
                    db.collection("Collections").document(collectionId.toString()).get().addOnCompleteListener {
                        db.collection("Users").document(it.result!!.get("author").toString()).update("collections", FieldValue.increment(-1))
                    }
                    db.collection("Collections").document(collectionId.toString()).delete()

                    this.finish()
                }
            }
            popUpMenu.setSelected(position)
            popUpMenu.setAutoDismiss(true)
        }
}