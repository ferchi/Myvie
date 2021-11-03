package com.jfsb.myvie.main.collection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jfsb.myvie.databinding.ItemCollectionBinding
import java.lang.Exception

class CollectionAdapter(
    private val dataSet: List<Collection>,
    fragment: FragmentActivity) : RecyclerView.Adapter<CollectionAdapter.ViewHolder>() {

    val context = fragment

    class ViewHolder (val binding: ItemCollectionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCollectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val collection = dataSet[position]

        viewHolder.binding.tvItemCollectionName.text = collection.collectionName

        try {
            Glide.with(context)
                .load(collection.collectionImage)
                .transform(CenterCrop())
                .into(viewHolder.binding.rivItemCollectionPoster)
        } catch (e:Exception){

        }
    }

    override fun getItemCount() = dataSet.size
}