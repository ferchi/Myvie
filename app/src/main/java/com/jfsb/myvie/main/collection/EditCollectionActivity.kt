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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.jfsb.myvie.R
import com.jfsb.myvie.api.MovieInfoResponse
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.databinding.ActivityCollectionBinding
import com.jfsb.myvie.databinding.ActivityEditCollectionBinding
import com.jfsb.myvie.main.movie.CollectionEditListAdapter
import com.jfsb.myvie.main.search.MovieImageListAdapter
import com.jfsb.myvie.main.search.MovieTextListListener
import com.jfsb.myvie.objects.Utils
import com.jfsb.myvie.objects.Utils.convertMovie
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class EditCollectionActivity : AppCompatActivity(), MovieTextListListener {
    lateinit var binding: ActivityEditCollectionBinding
    lateinit var collectionId:String
    private val db = FirebaseFirestore.getInstance()
    private lateinit var moviesAdapter: CollectionEditListAdapter


    // Variables para obtener y cambiar de las imagenes de perfil
    private val TAKE_IMG_CODE = 1046
    private lateinit var vista: View
    private var storageChild: String = "collectionsImages"
    private var databaseChild: String =  "image"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditCollectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        collectionId = intent.extras!!.getString("id").toString()
        binding.cardRivEditCollection.setOnClickListener {
            changeImg()
        }

        binding.btnEditCollection.setOnClickListener {
            if(!binding.etEditCollection.text.isNullOrBlank())
            {
                db.collection("Collections").document(collectionId).update("name" , binding.etEditCollection.text.toString())
                Toast.makeText(this,"Actualizado", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this,"Ingresa un nombre", Toast.LENGTH_SHORT).show()
            }
        }

        moviesAdapter = CollectionEditListAdapter(mutableListOf()){ movie -> deleteMovie(movie.id.toString()) }

    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    private fun loadData(){
        loadImg()
        db.collection("Collections").document(collectionId).get().addOnCompleteListener { collection ->
            val info = collection.result!!.toObject(Collection::class.java)
            binding.etEditCollection.hint = info!!.name

            info.movies!!.forEach { movieId ->
                getMovie(movieId)
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

        binding.rvEditCollection.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = moviesAdapter

        }
    }

    private fun onError(error:String){
        Toast.makeText(this,"No se encontró la pelicula : $error", Toast.LENGTH_SHORT).show()
    }

    private fun deleteMovie(movieId: String){
        db.collection("Collections").document(collectionId).get().addOnCompleteListener { collection ->
            val movies = collection.result!!.toObject(Collection::class.java)!!.movies as MutableList
            movies.remove(movieId)
            db.collection("Collections").document(collectionId).update("movies", movies)
            Toast.makeText(this,"Pelicula eliminada",Toast.LENGTH_SHORT).show()
            finish()
            startActivity(intent)
        }
    }

    private fun changeImg(): Boolean {
        vista = binding.rivEditCollectionImage

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

                    binding.rivEditCollectionImage.setImageBitmap(bitmap)

                    handleUpload(bitmap)
                }
            }
        }
    }

    private fun handleUpload(bitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        val uid: String = collectionId
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
                db.collection("Collections").document(collectionId).update(databaseChild,uri.toString())
                loadImg()
                Toast.makeText(this, "Actualización exitosa", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Actualización fallida", Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadImg() {

        db.collection("Collections").document(collectionId).addSnapshotListener{
                result, error ->
            val urlImg = result!!.get(databaseChild).toString()

            try {
                Glide.with(this)
                    .load(urlImg)
                    .transform(CenterCrop())
                    .into(binding.rivEditCollectionImage)
                //Picasso.get().load(urlImg).into(binding.ivCollectionImage)

            } catch (e: Exception) {
                Picasso.get().load(R.drawable.myvie_logo).into(binding.rivEditCollectionImage)
            }
        }
    }
}