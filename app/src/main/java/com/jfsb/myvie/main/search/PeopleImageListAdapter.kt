package com.jfsb.myvie.main.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jfsb.myvie.R
import com.jfsb.myvie.api.People
import com.jfsb.myvie.databinding.ItemPeopleListImageBinding
import com.jfsb.myvie.databinding.ItemPeopleListTextBinding

class PeopleImageListAdapter (private val peopleList: MutableList<People>,
                              private val onMovieClick: (people: People) -> Unit
                           ):

    RecyclerView.Adapter<PeopleImageListAdapter.ViewHolder>(){

    lateinit var mContext:Context
    var lastPosition = -1

    class ViewHolder (val binding: ItemPeopleListImageBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(ItemPeopleListImageBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount() = peopleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation = AnimationUtils.loadAnimation(mContext, R.anim.fall_down)
        animation.duration = 1000

        val people = peopleList[position]


        holder.binding.tvItemPeopleNameImage.text =  people.namePeople
        holder.binding.cardItemPeopleContainerImage.setOnClickListener { onMovieClick.invoke(people) }
        holder.binding.cardCivContainer.setOnClickListener { onMovieClick.invoke(people) }

        if(holder.adapterPosition > lastPosition){
            holder.binding.cardItemPeopleContainerImage.startAnimation(animation)
            holder.binding.cardCivContainer.startAnimation(animation)
            lastPosition = holder.adapterPosition
        }
        if(people.profilePath != null){
            Glide.with(holder.itemView)
                .load("https://image.tmdb.org/t/p/w342${people.profilePath}")
                .transform(CenterCrop())
                .into(holder.binding.civPeopleProfile)}
        else{
            Glide.with(holder.itemView)
                .load("https://firebasestorage.googleapis.com/v0/b/myvieapp.appspot.com/o/camera_0.png?alt=media&token=530d3f07-5c1f-4243-9301-169ce948f691")
                .fitCenter()
                .into(holder.binding.civPeopleProfile)
        }
    }

    fun appendPeople(peopleList: List<People>) {
        this.peopleList.addAll(peopleList)
        notifyItemRangeInserted(
            this.peopleList.size,
            peopleList.size - 1
        )
    }

    fun clearPeople() {
        this.peopleList.clear()
        notifyItemRangeInserted(0, 0)
    }

}