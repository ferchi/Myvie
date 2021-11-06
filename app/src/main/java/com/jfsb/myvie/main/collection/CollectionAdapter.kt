package com.jfsb.myvie.main.collection

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.jfsb.myvie.R
import com.jfsb.myvie.databinding.ItemCollectionBinding
import com.squareup.picasso.Picasso
import java.lang.Exception
import java.util.ArrayList

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

        viewHolder.binding.tvItemCollectionName.text = collection.name

        try {
            Glide.with(viewHolder.itemView)
                .load(collection.image)
                .transform(CenterCrop())
                .into(viewHolder.binding.rivItemCollectionPoster)

        } catch (e: Exception) {

            Glide.with(viewHolder.itemView)
                .load(R.drawable.myvie_logo)
                .transform(CenterCrop())
                .into(viewHolder.binding.rivItemCollectionPoster)
        }

        viewHolder.binding.cardItemCollectionContainer.setOnClickListener {
            val intent = Intent(context, CollectionActivity::class.java)
            intent.putExtra("id", collection.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = dataSet.size
}