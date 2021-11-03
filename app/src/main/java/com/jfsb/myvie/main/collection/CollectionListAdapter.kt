package com.jfsb.myvie.main.collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.ItemCollectionListBinding
import com.jfsb.myvie.databinding.ItemGenreBinding
import com.jfsb.myvie.main.movie.GenreMoviesDialog.Companion.GENRE_ID
import com.jfsb.myvie.main.movie.GenreMoviesDialog.Companion.GENRE_NAME
import com.jfsb.myvie.main.people.PeopleMoviesDialog

class CollectionListAdapter(
    private val dataSet: List<Collection>?,
    private val movieId:String,
    private val fragment: FragmentActivity) : RecyclerView.Adapter<CollectionListAdapter.ViewHolder>() {

    val context = fragment
    private val db = FirebaseFirestore.getInstance()
    class ViewHolder (val binding: ItemCollectionListBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(ItemCollectionListBinding.inflate(LayoutInflater.from(parent.context),parent,false))
     }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val collection =  dataSet!![position]

        viewHolder.binding.tvItemCollectionListName.text =collection.name
        val doc = db.collection("Collections").document(collection.id.toString())
        val movies = collection.movies!!.toMutableList()

        viewHolder.binding.cardItemCollectionContainer.setOnClickListener {
            movies.add(movieId)
            db.runTransaction {
                it.update(doc, "movies", movies)
                null
            }
            Toast.makeText(fragment, "Agregado a la colección ${collection.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount() = dataSet!!.size

}