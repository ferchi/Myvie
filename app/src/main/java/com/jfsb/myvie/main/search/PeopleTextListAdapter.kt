package com.jfsb.myvie.main.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.jfsb.myvie.R
import com.jfsb.myvie.api.People
import com.jfsb.myvie.databinding.ItemPeopleListTextBinding
import androidx.recyclerview.widget.LinearLayoutManager




class PeopleTextListAdapter (private val peopleList: MutableList<People>,
                             private val onMovieClick: (people: People) -> Unit
                           ):

    RecyclerView.Adapter<PeopleTextListAdapter.ViewHolder>(){

    lateinit var mContext:Context
    var lastPosition = -1

    class ViewHolder (val binding: ItemPeopleListTextBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context
        return ViewHolder(ItemPeopleListTextBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount() = peopleList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animation = AnimationUtils.loadAnimation(mContext, R.anim.fall_down)
        animation.duration = 1000

        val people = peopleList[position]


        holder.binding.tvItemPeopleNameText.text =  people.namePeople
        holder.binding.cardItemPeopleContainerText.setOnClickListener { onMovieClick.invoke(people) }

        if(holder.adapterPosition > lastPosition){
            holder.binding.cardItemPeopleContainerText.startAnimation(animation)
            lastPosition = holder.adapterPosition
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