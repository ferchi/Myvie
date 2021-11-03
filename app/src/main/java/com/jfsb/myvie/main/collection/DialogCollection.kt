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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.jfsb.myvie.databinding.DialogCreateCollectionBinding
import java.text.SimpleDateFormat
import java.util.*


class DialogCollection(val movieId:String) : DialogFragment(){

    private var _binding : DialogCreateCollectionBinding? = null
    val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val current = Firebase.auth.currentUser!!.email!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = DialogCreateCollectionBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            alertBuilder.setPositiveButton("Crear") { _, _ ->
                val collectionName = binding.etCreateCollectionName.text.toString()
                val id: String = db.collection("Collections").document().id
                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
                val currentDate = sdf.format(Date())
                val movies = listOf(movieId)

                if(collectionName.isNotBlank()){
                db.collection("Collections").document(id).set(
                    hashMapOf (
                        "id" to id,
                        "author" to current,
                        "name" to collectionName,
                        "movies" to movies,
                        "image" to null,
                        "date" to currentDate.toString()
                    )
                )
                    db.collection("Users").document(current).update("collections", FieldValue.increment(1))
                    Toast.makeText(requireContext(), "Colección creada", Toast.LENGTH_SHORT).show()
            }
                else{
                    Toast.makeText(requireContext(), "Ingresa un nombre", Toast.LENGTH_SHORT).show()
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