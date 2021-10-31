package com.jfsb.myvie.main.movie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.ItemGenreBinding
import com.jfsb.myvie.main.movie.GenreMoviesDialog.Companion.GENRE_ID
import com.jfsb.myvie.main.movie.GenreMoviesDialog.Companion.GENRE_NAME
import com.jfsb.myvie.main.people.PeopleMoviesDialog

class GenreAdapter(private val dataSet: ArrayList<String>?, private val ids: ArrayList<String>, fragment: FragmentActivity) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    val context = fragment

    class ViewHolder (val binding: ItemGenreBinding) : RecyclerView.ViewHolder(binding.root)


     // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         return ViewHolder(ItemGenreBinding.inflate(LayoutInflater.from(parent.context),parent,false))
     }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.binding.tvGenre.text = dataSet!![position]
        viewHolder.binding.lyGenreContainer.setOnClickListener {
            getMoviesByGenre( ids[position], dataSet[position])
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet!!.size


    private fun getMoviesByGenre(idGenre: String, nameGenere:String) {
        val genreMoviesDialog = GenreMoviesDialog()
        val args = Bundle()

        args.putString(GENRE_ID, idGenre)
        args.putString(GENRE_NAME, nameGenere)

        genreMoviesDialog.arguments = args

        genreMoviesDialog.show(context.supportFragmentManager, "Crear")
    }
}