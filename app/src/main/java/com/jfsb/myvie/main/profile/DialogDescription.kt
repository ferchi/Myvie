package com.jfsb.myvie.main.profile

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
import com.jfsb.myvie.databinding.DialogEditDescriptionBinding
import java.text.SimpleDateFormat
import java.util.*


class DialogDescription() : DialogFragment(){

    private var _binding : DialogEditDescriptionBinding? = null
    val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()
    private val current = Firebase.auth.currentUser!!.email!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = DialogEditDescriptionBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            alertBuilder.setPositiveButton("Crear") { _, _ ->
                val description = binding.etEditDescription.text.toString()

                if(description.isNotBlank()){

                    db.collection("Users").document(current).update("description", description)
                    Toast.makeText(requireContext(), "Actualizado", Toast.LENGTH_SHORT).show()
            }
                else{
                    Toast.makeText(requireContext(), "Ok, suerte para la otra", Toast.LENGTH_SHORT).show()
                }
            }

            alertBuilder.create()

        } ?: throw IllegalStateException("Exception !! Activity is null")
    }

    override fun onStart() {
        super.onStart()

        val window = dialog!!.window
        window!!.setBackgroundDrawableResource(android.R.color.transparent)

    }
}