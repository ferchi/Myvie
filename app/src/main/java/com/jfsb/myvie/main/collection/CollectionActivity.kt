package com.jfsb.myvie.main.collection

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jfsb.myvie.R
import com.jfsb.myvie.api.Movie
import com.jfsb.myvie.api.MovieInfoResponse
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.databinding.ActivityCollectionBinding
import com.jfsb.myvie.main.movie.MoviesAdapter
import com.jfsb.myvie.main.search.MovieImageListAdapter
import com.jfsb.myvie.main.search.MovieTextListListener
import com.jfsb.myvie.objects.Utils
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class CollectionActivity : AppCompatActivity(), MovieTextListListener {
    lateinit var binding: ActivityCollectionBinding
    private var collectionId : String? = ""
    private val db = FirebaseFirestore.getInstance()

    private var movies: MutableList<Movie> = mutableListOf()

    // Variables para obtener y cambiar de las imagenes de perfil
    private val TAKE_IMG_CODE = 1046
    private lateinit var vista: View
    private var storageChild: String = "collectionsImages"
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
        binding.ivCollectionImage.setOnClickListener {
            changeImg()
        }

        rvMovies = binding.rvCollection

        moviesAdapter = MovieImageListAdapter(mutableListOf(),this){ movie -> Utils.showMovieDetails(movie, this) }

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

    private fun convertMovie(movieInfo: MovieInfoResponse): Movie {
        val id = movieInfo.id
        val title = movieInfo.title
        val overview = movieInfo.overview
        val posterPath = movieInfo.posterPath
        val backdropPath = movieInfo.backdropPath
        val rating = movieInfo.rating
        val releaseDate = movieInfo.releaseDate
        val genreList = movieInfo.genresList
        val genreIds: MutableList<String> = mutableListOf()

        genreList.forEach { genre ->
            genreIds.add(genre.id.toString())
        }
        return Movie(id, title, overview, posterPath, backdropPath, rating, releaseDate, genreIds)
    }

    private fun changeImg(): Boolean {
        vista = binding.ivCollectionImage

        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"

        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, TAKE_IMG_CODE)
        }

        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == TAKE_IMG_CODE) {
            when (resultCode) {
                RESULT_OK -> {
                    val bitmap: Bitmap =
                        MediaStore.Images.Media.getBitmap(contentResolver, data?.data)

                    binding.ivCollectionImage.setImageBitmap(bitmap)

                    handleUpload(bitmap)
                }
            }
        }
    }

    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val uid: String = collectionId.toString()
        val ref: StorageReference = FirebaseStorage.getInstance().reference
            .child(storageChild)
            .child("$uid.jpeg")

        ref.putBytes(baos.toByteArray())
            .addOnSuccessListener {
                getDownloadUrl(ref)
            }
            .addOnFailureListener() {
                Log.e("Errorimg", "onFailure", it.cause)
            }
    }

    private fun getDownloadUrl(ref: StorageReference) {
        ref.downloadUrl.addOnSuccessListener {
            setUserProfileUrl(it)
        }
    }

    private fun setUserProfileUrl(uri: Uri) {
        val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!

        val request: UserProfileChangeRequest = UserProfileChangeRequest
            .Builder()
            .setPhotoUri(uri)
            .build()

        user.updateProfile(request)
            .addOnSuccessListener {
                db.collection("Collections").document(collectionId.toString()).update(databaseChild,uri.toString())
                loadImg()
                Toast.makeText(this, "Actualización exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Actualización fallida", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadImg() {

        db.collection("Collections").document(collectionId.toString()).addSnapshotListener{
                result, error ->
            val urlImg = result!!.get(databaseChild).toString()

            try {
                Picasso.get().load(urlImg).into(binding.ivCollectionImage)

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
}