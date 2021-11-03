package com.jfsb.myvie.main.collection

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.jfsb.myvie.databinding.DialogAddToCollectionBinding
import com.jfsb.myvie.databinding.DialogCreateCollectionBinding
import java.text.SimpleDateFormat
import java.util.*


class DialogCollectionList(val movieId:String) : DialogFragment(){

    private var _binding : DialogAddToCollectionBinding? = null
    val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val current = Firebase.auth.currentUser!!.email!!
    private lateinit var rev:RecyclerView

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = DialogAddToCollectionBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            db.collection("Collections").whereEqualTo("author", current).get().addOnCompleteListener {
                val collections = it.result!!.toObjects(Collection::class.java)
                rev = binding.rvCollectionsList
                rev.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = CollectionListAdapter(collections,movieId,requireActivity())
                }
            }

            alertBuilder.create()

        } ?: throw IllegalStateException("Exception !! Activity is null")
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()

        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)

    }
}