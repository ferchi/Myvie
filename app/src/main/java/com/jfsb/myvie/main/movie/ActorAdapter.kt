package com.jfsb.myvie.main.movie

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jfsb.myvie.R
import com.jfsb.myvie.api.Cast
import de.hdodenhof.circleimageview.CircleImageView

class ActorAdapter (
    private var actors: MutableList<Cast>,
    private val onActorClick: (actor: Cast) -> Unit
    ) : RecyclerView.Adapter<ActorAdapter.ActorViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.item_actor, parent, false)
        return ActorViewHolder(view)
    }

    override fun getItemCount(): Int = actors.size

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(actors[position])
    }

    fun appendActors(actors: List<Cast>) {
        this.actors.addAll(actors)
        notifyItemRangeInserted(
            this.actors.size,
            actors.size - 1
        )
    }

    inner class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: CircleImageView = itemView.findViewById(R.id.civ_actor_profile)
        private val tvActorName: TextView = itemView.findViewById(R.id.tv_actor_name)

        fun bind(actor: Cast) {
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${actor.profilePath}")
                .transform(CenterCrop())
                .into(poster)

            Log.d("imagen", "https://image.tmdb.org/t/p/w342${actor.profilePath}")
            tvActorName.text = actor.nameCast

            itemView.setOnClickListener { onActorClick.invoke(actor) }
        }

    }
}