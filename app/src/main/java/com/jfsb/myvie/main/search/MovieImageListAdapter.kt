package com.jfsb.myvie.main.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jfsb.myvie.R
import com.jfsb.myvie.api.Movie
import com.jfsb.myvie.api.MoviesRepository
import com.jfsb.myvie.objects.Utils.getGenre
import com.jfsb.myvie.databinding.ItemMovieListImageBinding

class MovieImageListAdapter (private val movies: MutableList<Movie>,
                             private var movieTextListListener: MovieTextListListener,
                             private val onMovieClick: (movies: Movie) -> Unit):

    RecyclerView.Adapter<MovieImageListAdapter.ViewHolder>(){

    lateinit var mContext:Context
    var lastPosition = -1
    var durationS = ""
    var moviesAux:MutableList<Movie> = mutableListOf()

    class ViewHolder (val binding: ItemMovieListImageBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(ItemMovieListImageBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var isSelected:Boolean = false
        val animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row)
        val animationR = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row_reverse)
        val movie = movies[position]
        val genders = movie.genresIds

        var gendersName : MutableList<String> = mutableListOf()

        genders.forEach { gender ->
            gendersName.add(getGenre(gender))
        }
        getDuration(movie.id)
        holder.binding.tvItemMovieNameImage.text =  "${movie.title} (${movie.releaseDate})"
        try{holder.binding.tvItemMovieGenderImage.text = gendersName.joinToString(" , ") }
        catch (e:Exception){
            holder.binding.tvItemMovieGenderImage.text = "No disponible"
        }

        holder.binding.tvItemMovieRankingImage.text = ((movie.rating/2).toString() + "/5")
        holder.binding.tvItemMovieDurationImage.text = durationS
        holder.binding.cardItemMovieContainerImage.setOnClickListener { onMovieClick.invoke(movie) }
        holder.binding.moviePosterContainer.setOnClickListener { onMovieClick.invoke(movie) }

        if(holder.adapterPosition > lastPosition){
            holder.binding.cardItemMovieContainerImage.startAnimation(animation)
            holder.binding.rivMoviePoster.startAnimation(animation)
            lastPosition = holder.adapterPosition
        }

        if(movie.posterPath != null){
        Glide.with(holder.itemView)
            .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
            .transform(CenterCrop())
            .into(holder.binding.rivMoviePoster)}
        else{
            Glide.with(holder.itemView)
                .load("https://firebasestorage.googleapis.com/v0/b/myvieapp.appspot.com/o/camera_0.png?alt=media&token=530d3f07-5c1f-4243-9301-169ce948f691")
                .fitCenter()
                .into(holder.binding.rivMoviePoster)
        }

    }
    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    fun clearMovies() {
        this.movies.clear()
        notifyItemRangeInserted(0, 0)
    }

    private fun getDuration(movieId:Long){
        MoviesRepository.getMoreInfoMovie(
            movieId,
            ::onMoreInfoMovieFetched,
            ::onError
        )
    }

    private fun onMoreInfoMovieFetched(duration: Long) {

        durationS = (duration).toString() + " Minutos"
    }

    private fun onError(error:String){
        Toast.makeText(mContext,"No se obtuvo duración : $error", Toast.LENGTH_SHORT).show()
    }
}