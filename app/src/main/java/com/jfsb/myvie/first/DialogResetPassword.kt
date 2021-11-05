package com.jfsb.myvie.first

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
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
import com.jfsb.myvie.databinding.DialogResetPasswordBinding
import java.text.SimpleDateFormat
import java.util.*


class DialogResetPassword() : DialogFragment(){

    private var _binding : DialogResetPasswordBinding? = null
    val binding get() = _binding!!
    private val auth = Firebase.auth

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val alertBuilder = AlertDialog.Builder(it)

            _binding = DialogResetPasswordBinding.inflate(LayoutInflater.from(context))
            alertBuilder.setView(binding.root)

            alertBuilder.setPositiveButton("Restablecer") { _, _ ->
                val email = binding.etResetEmail.text.toString()

                if(email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    auth.sendPasswordResetEmail(email)
                    Toast.makeText(requireContext(), "Te llegará un correo pronto", Toast.LENGTH_SHORT).show()
            }
                else{
                    Toast.makeText(requireContext(), "Ingresa un correo valido", Toast.LENGTH_SHORT).show()
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