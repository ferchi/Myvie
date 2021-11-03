package com.jfsb.myvie.first


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jfsb.myvie.databinding.ActivityRegisterBinding
import com.jfsb.myvie.main.MainActivity

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.tvLogin.setOnClickListener {
            this.finish()
        }

        binding.btnSignup.setOnClickListener {
            val db = FirebaseFirestore.getInstance()

            if(binding.etRegisterUsername.text.isNotEmpty() && binding.etRegisterEmail.text.isNotEmpty() && binding.etRegisterPassword.text.isNotEmpty() && binding.etRegisterRepassword.text.isNotEmpty() ){
                    if(binding.etRegisterPassword.text.toString() == binding.etRegisterRepassword.text.toString()){
                            val mAuth = FirebaseAuth.getInstance()

                            mAuth.createUserWithEmailAndPassword(binding.etRegisterEmail.text.toString(), binding.etRegisterPassword.text.toString()).addOnCompleteListener {

                                if (it.isSuccessful) {
                                    db.collection("Users").document(binding.etRegisterEmail.text.toString()).set(
                                        hashMapOf (
                                            "email" to binding.etRegisterEmail.text.toString(),
                                            "name" to binding.etRegisterUsername.text.toString(),
                                            "imgProfile" to "",
                                            "collections" to 0,
                                            "followers" to 0,
                                            "description" to ""
                                        )
                                    )
                                    login(it.result?.user?.email ?:"", binding.etRegisterPassword.text.toString())
                                }
                            }
                    }
                    else{
                        showAlert("Las contraseñas no coinciden")
                    }
            }
            else{
                showAlert("Favor de rellenar todos los campos")
            }
        }
    }

    private fun showAlert( message:String){
        val toast = Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
        toast.show()
    }

    private fun login(email: String, password: String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful)
                showHome(it.result?.user?.email ?:"")
            else
                showAlert("Correo o contraseña incorrecto")
        }
    }
    private fun showHome(email: String){
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
        }
        startActivity(homeIntent)
    }
}