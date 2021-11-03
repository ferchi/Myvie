package com.jfsb.myvie.main.collection

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jfsb.myvie.databinding.ActivityCollectionBinding
import com.jfsb.myvie.databinding.ActivityLoginBinding

class CollectionActivity( val movieList:List<String>) : AppCompatActivity() {
    lateinit var binding: ActivityCollectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCollectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}