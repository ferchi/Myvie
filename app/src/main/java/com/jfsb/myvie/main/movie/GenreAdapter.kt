package com.jfsb.myvie.main.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.ItemGenreBinding

class GenreAdapter(private val dataSet: ArrayList<String>?) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {
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
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet!!.size

}